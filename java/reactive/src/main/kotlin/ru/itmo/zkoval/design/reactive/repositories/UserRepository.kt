package ru.itmo.zkoval.design.reactive.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.itmo.zkoval.design.reactive.entities.User

@Repository
interface UserRepository: ReactiveMongoRepository<User, String> {
    fun findByLogin(login: String): Mono<User>
}