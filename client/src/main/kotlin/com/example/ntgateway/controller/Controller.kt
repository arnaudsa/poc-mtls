package com.example.ntgateway.controller

import java.net.URI
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/serasa")
class Controller(
    val restTemplate: RestTemplate,
    val okHttpClient: OkHttpClient,
    val env: Environment
) {

    @GetMapping("/health")
    fun getData(): String {
        println("Serasa está funcionando")
        return "Pong"
    }

    @GetMapping("/ms-data")
    fun getMsData(): Client? {
        val endpoint = env["endpoint.cora-service"] ?: throw IllegalArgumentException("Endpoint não configurado.")
        println("Consultando os dados de clientes na Cora Get: $endpoint")

        val response = okHttpClient.newCall(Request.Builder().url(endpoint).build())
            .execute()

        val responseBody = response.body?.byteString()?.utf8()
        println(responseBody)


        return run {
            restTemplate.getForEntity(URI(endpoint), Client::class.java).body
        }
    }


}

data class Client(
    val id: String,
    val name:String
)