package org.codingkata.socialnetwork

/**
 * Value object representing a timeline message with content and metadata.
 */
data class Message(
    val content: String,
    val author: UserId,
    val timestamp: Timestamp = Timestamp(),
) {
    init {
        require(content.isNotBlank()) { "Message content cannot be empty or blank" }
    }
}
