package ru.itmo.zkoval.design.reactive.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.itmo.zkoval.design.reactive.entities.Product

@Repository
interface ProductRepository: ReactiveMongoRepository<Product, String> {
    fun findByUserId(userId: String): Flux<Product>
}