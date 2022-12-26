package com.example.ntgateway.controller

import java.net.URI
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("nt-gateway")
class Controller(
    val restTemplate: RestTemplate,
    val env: Environment
) {

    @GetMapping("/data")
    fun getData(): String {
        println("Returning data from nt-gateway own data method")
        return "Hello from NT-GATEWAY-data method"
    }

    @GetMapping("/ms-data")
    fun getMsData(): Client? {
        println("Got inside NT-GATEWAY-ms-data method")

        val endpoint = env.get("endpoint.ms-service") ?: throw IllegalArgumentException("Endpoint não configurado.")
        println("MS Endpoint name: [ $endpoint ]")

        return run {
            restTemplate.getForEntity(URI(endpoint), Client::class.java).body
        }
    }


}

data class Client(
    val id: String,
    val name:String
)