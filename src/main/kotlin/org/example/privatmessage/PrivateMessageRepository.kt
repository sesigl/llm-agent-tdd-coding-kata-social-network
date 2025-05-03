package org.example.privatmessage

import org.example.Message
import org.example.UserId

class PrivateMessageRepository {
    private val messages: MutableMap<UserId, MutableList<Message>> = mutableMapOf()

    fun sendPrivateMessage(messageAuthor: UserId, to: UserId, messageContent: String) {
        this.messages.getOrPut(to) { mutableListOf() }
            .add(Message(messageAuthor, messageContent))
    }

    fun findAllMessagesFor(userId: UserId): List<Message> {
        return this.messages[userId] ?: return emptyList()
    }

}