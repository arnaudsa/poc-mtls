package com.example.serverpoctls.controller

import java.util.UUID
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cora")
class Controller {

    @GetMapping("/clients")
    fun getClient(): Client {

        return Client(
            id = UUID.randomUUID().toString(),
            name = "Ana Luiza"
        ).also { println("Executado com sucesso") }
    }

    @GetMapping("/health")
    fun getHealth(): String {
        println("Cora está funcionando")
        return "Pong"
    }

}

data class Client(
    val id: String,
    val name: String
)