package org.example.privatmessage

import org.example.UserId

class PrivateMessageService(
    private val privateMessageRepository: PrivateMessageRepository = PrivateMessageRepository()
) {
    fun sendPrivateMessage(fromUserId: String, toUserId: String, message: String) {
        privateMessageRepository.sendPrivateMessage(
            messageAuthor = UserId(fromUserId),
            to = UserId(toUserId),
            message
        )
    }

    fun getAllPrivateMessages(ownerUserId: String): List<String> {
        return privateMessageRepository.findAllMessagesFor(UserId(ownerUserId))
            .map { it.toMarkdown() }
    }

}
