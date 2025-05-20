package org.codingkata.socialnetwork.domain.message.model

import java.time.Instant

data class Message(
    val content: String,
    val author: String,
    val timestamp: Instant,
)
