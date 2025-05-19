package org.codingkata.socialnetwork

import java.time.Instant
import java.time.temporal.ChronoUnit

data class Message(
    val content: String,
    val author: User,
    val timestamp: Instant,
) {
    fun isPostedBefore(other: Message): Boolean = this.timestamp.isBefore(other.timestamp)

    fun isPostedBy(user: User): Boolean = this.author == user

    fun getAgeInSeconds(): Long = ChronoUnit.SECONDS.between(timestamp, Instant.now())
}
