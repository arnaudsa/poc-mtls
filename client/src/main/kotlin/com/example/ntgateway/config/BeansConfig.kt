package com.example.ntgateway.config

import java.security.KeyFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

const val PEM_PRIVATE_START = "-+BEGIN [A-Z ]+-+"
const val PEM_PRIVATE_END = "-+END [A-Z ]+-+"

@Configuration
class BeansConfig {

    @Bean
    fun restTemplate(certificateConfig: CertificateConfig): RestTemplate {
        val certificate: X509Certificate = CertificateFactory
            .getInstance("X.509")
            .generateCertificate(certificateConfig.certificateStream()) as X509Certificate

        val decode = Base64.getDecoder().decode(certificateConfig.normalizeCertKey())
        val keySpec = PKCS8EncodedKeySpec(decode)

        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(keySpec)

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        keyStore.setKeyEntry("client", privateKey,null, arrayOf(certificate))

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, null)

        val socketFactory = SSLConnectionSocketFactory(
            SSLContextBuilder.create()
                .loadTrustMaterial(keyStore, TrustSelfSignedStrategy())
                .loadKeyMaterial(keyStore, null)
                .build(),
            NoopHostnameVerifier.INSTANCE
        )

        val httpClient = HttpClients.custom()
            .setSSLSocketFactory(socketFactory)
            .setMaxConnTotal(5)
            .setMaxConnPerRoute(5)
            .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
        requestFactory.setReadTimeout(10000)
        requestFactory.setConnectTimeout(10000)

        val restTemplate = RestTemplate()
        restTemplate.requestFactory = requestFactory

        return restTemplate
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "client.transport")
data class CertificateConfig(
    val cert: String,
    val certKey: String
){
    fun certificateStream() = cert.decode().byteInputStream()

    fun normalizeCertKey() = certKey
        .decode()
        .replace(Regex(PEM_PRIVATE_START), "")
        .replace(Regex(PEM_PRIVATE_END), "")
        .replace(Regex("\\s"), "")
}

private fun String.decode(): String = Base64.getDecoder().decode(this).toString(Charsets.UTF_8)



