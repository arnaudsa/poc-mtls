package com.example.ntgateway.config

import java.security.KeyFactory
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Duration
import java.util.Base64
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import okhttp3.OkHttpClient
import org.apache.http.conn.ssl.NoopHostnameVerifier.INSTANCE
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
const val TIMEOUT = 1000
const val MAX_CONNECTION = 5

@Configuration
class BeansConfig(
    val certificateConfig: CertificateConfig
) {

    @Bean
    fun restTemplate(): RestTemplate {
        val certificate = buildCertificate()
        val privateKey = buildPrivateKey()
        val keyStore = buildKeyStore(privateKey, certificate)
            .also {
                buildTrustManager(it)
                initKeyStoreManager(it)
            }

        val httpClient = HttpClients.custom()
            .setSSLSocketFactory(SSLConnectionSocketFactory(buildSSLContext(keyStore), INSTANCE))
            .setMaxConnTotal(MAX_CONNECTION)
            .setMaxConnPerRoute(MAX_CONNECTION)
            .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
            .let {
                it.setReadTimeout(TIMEOUT)
                it.setConnectTimeout(TIMEOUT)
                it
            }

        return RestTemplate(requestFactory)
    }
    @Bean
    fun okHttp(): OkHttpClient {
        val certificate = buildCertificate()
        val privateKey = buildPrivateKey()
        val keyStore = buildKeyStore(privateKey, certificate)

        initKeyStoreManager(keyStore)
        val trustManager = buildTrustManager(keyStore)
        val sslContext = buildSSLContext(keyStore)

        return OkHttpClient
            .Builder()
            .sslSocketFactory(sslContext.socketFactory ,trustManager)
            .connectTimeout(Duration.ofMillis(TIMEOUT.toLong()))
            .readTimeout(Duration.ofMillis(TIMEOUT.toLong()))
            .hostnameVerifier(INSTANCE)
            .build()
    }

    private fun buildPrivateKey(): PrivateKey {
        val decode = Base64.getDecoder().decode(certificateConfig.normalizeCertKey())
        val keySpec = PKCS8EncodedKeySpec(decode)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    private fun buildKeyStore(privateKey: PrivateKey, certificate: Certificate): KeyStore {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        keyStore.setKeyEntry("client", privateKey,null, arrayOf(certificate))
        return keyStore
    }

    private fun buildCertificate() = CertificateFactory
        .getInstance("X.509")
        .generateCertificate(certificateConfig.certificateStream()) as X509Certificate

    private fun initKeyStoreManager(keyStore: KeyStore): KeyManagerFactory {
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, null)
        return keyManagerFactory
    }

    private fun buildTrustManager(keyStore: KeyStore) : X509TrustManager = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm())
        .let {
            it.init(keyStore)
            it.trustManagers.first() as X509TrustManager
        }

    private fun buildSSLContext(keyStore: KeyStore) = SSLContextBuilder
        .create()
        .loadTrustMaterial(keyStore, TrustSelfSignedStrategy())
        .loadKeyMaterial(keyStore, null)
        .build()
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



