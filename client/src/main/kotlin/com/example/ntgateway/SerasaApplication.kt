package com.example.ntgateway

import com.example.ntgateway.config.BeansConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = [BeansConfig::class])
class SerasaApplication

fun main(args: Array<String>) {
    runApplication<SerasaApplication>(*args)
}
