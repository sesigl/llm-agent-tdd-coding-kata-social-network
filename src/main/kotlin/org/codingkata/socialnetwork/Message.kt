package org.codingkata.socialnetwork

import java.time.Instant

data class Message(
    val content: String,
    val author: User,
    val timestamp: Instant,
)
