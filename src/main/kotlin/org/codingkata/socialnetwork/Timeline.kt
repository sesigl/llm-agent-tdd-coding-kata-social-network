package org.codingkata.socialnetwork

class Timeline {
    private var messages: List<String> = emptyList()

    fun publishMessage(messageContent: String) {
        this.messages = this.messages + messageContent
    }

    fun getAllMessages(): List<String> {
        return this.messages
    }
}