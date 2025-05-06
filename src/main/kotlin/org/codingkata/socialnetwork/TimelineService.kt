package org.codingkata.socialnetwork

class TimelineService {
    private var messages: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun publishMessage(ownerUserId: String, content: String) {
        this.messages.getOrPut(ownerUserId, { mutableListOf() }).add(content)
    }
    fun getMessages(userId: String, requesterUserId: String = userId): List<String> {
        return this.messages.getOrDefault(userId, arrayListOf())
    }
}