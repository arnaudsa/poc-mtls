package com.example.ntgateway.config

import java.security.KeyStore
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class BeansConfig {

    @Bean
    fun restTemplate(): RestTemplate {

        val keyStore = KeyStore.getInstance("pkcs12")
        val classPathResource = ClassPathResource("keystore/keystore-client.p12")

        val inputStream = classPathResource.inputStream
        keyStore.load(inputStream, "1234".toCharArray())

        val socketFactory = SSLConnectionSocketFactory(
            SSLContextBuilder.create()
                .loadTrustMaterial(null, TrustSelfSignedStrategy())
                .loadKeyMaterial(keyStore, "1234".toCharArray())
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