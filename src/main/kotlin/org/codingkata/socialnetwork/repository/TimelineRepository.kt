package org.codingkata.socialnetwork.repository

import org.codingkata.socialnetwork.Message
import org.codingkata.socialnetwork.TimelineQuery
import org.codingkata.socialnetwork.UserId
import java.util.concurrent.ConcurrentHashMap

class TimelineRepository {
    private val messagesByUser = ConcurrentHashMap<UserId, MutableList<Message>>()

    fun addMessage(message: Message) {
        messagesByUser
            .computeIfAbsent(message.author) { mutableListOf() }
            .add(message)
    }

    fun getMessagesFor(userId: UserId): List<Message> = messagesByUser[userId]?.toList() ?: emptyList()

    fun getMessagesChronologicallyDescending(userId: UserId): List<Message> = getMessagesFor(userId).sortedByDescending { it.timestamp }

    fun getMessagesWithQuery(
        userId: UserId,
        query: TimelineQuery,
    ): List<Message> {
        var messages = getMessagesFor(userId)

        query.afterTimestamp?.let { afterTimestamp ->
            messages = messages.filter { message -> message.timestamp > afterTimestamp }
        }

        query.beforeTimestamp?.let { beforeTimestamp ->
            messages = messages.filter { message -> message.timestamp < beforeTimestamp }
        }

        messages = messages.sortedByDescending { it.timestamp }

        query.limit?.let { limit ->
            messages = messages.take(limit)
        }

        return messages
    }
}
