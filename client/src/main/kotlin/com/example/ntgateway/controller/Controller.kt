package com.example.ntgateway.controller

import java.net.URI
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
    val env: Environment
) {

    @GetMapping("/health")
    fun getData(): String {
        println("Serasa está funcionando")
        return "Pong"
    }

    @GetMapping("/ms-data")
    fun getMsData(): Client? {
        println("Consultando os dados de clientes na Cora")

        val endpoint = env.get("endpoint.cora-service") ?: throw IllegalArgumentException("Endpoint não configurado.")
        println("Cora endpoint: [ $endpoint ]")

        return run {
            restTemplate.getForEntity(URI(endpoint), Client::class.java).body
        }
    }


}

data class Client(
    val id: String,
    val name:String
)