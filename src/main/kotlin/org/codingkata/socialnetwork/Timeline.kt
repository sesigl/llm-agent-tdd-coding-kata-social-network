package org.codingkata.socialnetwork

class Timeline(
    val id: TimelineId,
) {
    private val messages = mutableListOf<Message>()

    fun addMessage(message: Message) {
        if (message.author == id.userId) {
            messages.add(message)
        }
    }

    fun getMessages(): List<Message> = messages.sortedByDescending { it.timestamp }

    fun getMessages(query: TimelineQuery): List<Message> {
        var filteredMessages = messages.toList()

        query.afterTimestamp?.let { afterTimestamp ->
            filteredMessages = filteredMessages.filter { message -> message.timestamp > afterTimestamp }
        }

        query.beforeTimestamp?.let { beforeTimestamp ->
            filteredMessages = filteredMessages.filter { message -> message.timestamp < beforeTimestamp }
        }

        filteredMessages = filteredMessages.sortedByDescending { it.timestamp }

        query.limit?.let { limit ->
            filteredMessages = filteredMessages.take(limit)
        }

        return filteredMessages
    }
}
