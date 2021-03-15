package ru.itmo.zkoval.design.reactive.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.itmo.zkoval.design.reactive.enums.Currency

@Document(collection = "user")
data class User(
        @Id var id: String? = null,
        var login: String? = null,
        var name: String? = null,
        var preferredCurrency: Currency? = null
)
