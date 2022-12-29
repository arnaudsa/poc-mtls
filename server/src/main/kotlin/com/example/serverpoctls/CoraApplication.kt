package com.example.serverpoctls

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [CoraApplication::class])
class CoraApplication

fun main(args: Array<String>) {
    runApplication<CoraApplication>(*args)
}
