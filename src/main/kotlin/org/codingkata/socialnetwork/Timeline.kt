package org.codingkata.socialnetwork

import kotlin.collections.getOrPut

class Timeline {
    private val messages: MutableList<String> = mutableListOf()


    fun postMessage(messageContent: String) {
        this.messages.add(messageContent)
    }

    fun getAllMessages(): List<String> {
        return this.messages
    }
}