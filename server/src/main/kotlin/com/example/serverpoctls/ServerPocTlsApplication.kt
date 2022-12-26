package com.example.serverpoctls

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(scanBasePackageClasses = [ServerPocTlsApplication::class])
class ServerPocTlsApplication

fun main(args: Array<String>) {
    runApplication<ServerPocTlsApplication>(*args)
}
