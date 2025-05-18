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
        validate(content)
    }

    companion object {
        private const val MAX_CONTENT_LENGTH = 280

        /**
         * Creates a new message with the given content and author.
         * Uses the current time as the timestamp.
         *
         * @param content The message content
         * @param author The user who authored the message
         * @return A new Message instance
         * @throws IllegalArgumentException if the content is blank or exceeds maximum length
         */
        fun create(
            content: String,
            author: UserId,
        ): Message {
            validate(content)
            return Message(content, author)
        }

        /**
         * Creates a new message with the given content, author, and timestamp.
         *
         * @param content The message content
         * @param author The user who authored the message
         * @param timestamp The timestamp when the message was created
         * @return A new Message instance
         * @throws IllegalArgumentException if the content is blank or exceeds maximum length
         */
        fun create(
            content: String,
            author: UserId,
            timestamp: Timestamp,
        ): Message {
            validate(content)
            return Message(content, author, timestamp)
        }

        private fun validate(content: String) {
            require(content.isNotBlank()) { "Message content cannot be empty or blank" }
            require(content.length <= MAX_CONTENT_LENGTH) { "Message content cannot exceed $MAX_CONTENT_LENGTH characters" }
        }
    }
}
