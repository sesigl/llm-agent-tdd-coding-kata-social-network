package org.codingkata.socialnetwork.repository

import org.codingkata.socialnetwork.Message
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
}
