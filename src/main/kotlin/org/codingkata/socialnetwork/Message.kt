package org.codingkata.socialnetwork

/**
 * Data class representing a social network message
 * 
 * @property user The username of the message sender
 * @property content The text content of the message
 * @property timestamp The time the message was created (in milliseconds since epoch)
 */
data class Message(
    val user: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)