package ru.itmo.zkoval.design.reactive.utils

import org.springframework.web.reactive.function.server.ServerResponse

fun dummyView(page: String, context: Map<String, Any> = emptyMap()) = ServerResponse.ok().render(page, context)

fun dummyErrorView(errMsg: String) = dummyView("error", mapOf("errorMessage" to errMsg))
