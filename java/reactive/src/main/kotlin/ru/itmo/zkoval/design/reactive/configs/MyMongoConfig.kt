package ru.itmo.zkoval.design.reactive.configs

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import ru.itmo.zkoval.design.reactive.repositories.ProductRepository
import ru.itmo.zkoval.design.reactive.repositories.UserRepository

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = [UserRepository::class, ProductRepository::class])
class MyMongoConfig : AbstractReactiveMongoConfiguration() {
    override fun getDatabaseName() = "reactive_task_db"

    override fun reactiveMongoClient() = mongoClient()

    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create()
    }
}
