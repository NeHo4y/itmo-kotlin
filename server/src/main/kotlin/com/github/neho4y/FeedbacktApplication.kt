package com.github.neho4y

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class FeedbacktApplication

@Configuration
@EnableJpaAuditing
class JpaConfiguration

fun main(args: Array<String>) {
    runApplication<FeedbacktApplication>(*args)
}
