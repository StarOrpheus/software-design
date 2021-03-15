package ru.itmo.zkoval.design.reactive.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import ru.itmo.zkoval.design.reactive.enums.Currency

@Document(collection = "product")
data class Product(
        @Id @Field("_id") var id: String? = null,
        var productName: String? = null,
        var userId: String? = null,
        var price: Double? = null,
        var currency: Currency? = null
)