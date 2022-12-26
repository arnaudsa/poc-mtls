package com.example.serverpoctls.controller

import java.util.UUID
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/nt-ms")
class Controller {

    @GetMapping("/clients")
    fun getClient(): Client {

        return Client(
            id = UUID.randomUUID().toString(),
            name = "Ana Luiza"
        ).also { println("Executado com sucesso") }
    }

    @GetMapping("/data")
    fun getData(): String {
        println("Returning data from nt-ms data method")
        return "Hello from NT-MS-data method"
    }

}

data class Client(
    val id: String,
    val name: String
)