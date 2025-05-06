package org.codingkata.socialnetwork

class TimelineService {
    private var messages: MutableMap<String, MutableList<String>> = mutableMapOf()
    private var subscriber: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun publishMessage(ownerUserId: String, content: String) {
        this.messages.getOrPut(ownerUserId, { mutableListOf() }).add(content)

        subscriber.getOrPut(ownerUserId, { mutableListOf() }).forEach {
            this.messages.getOrPut(it, { mutableListOf() }).add(content)
        }
    }
    fun getMessages(userId: String, requesterUserId: String = userId): List<String> {
        return this.messages.getOrDefault(userId, arrayListOf())
    }

    fun subscribeTo(timelineUserId: String, subscriberUserId: String) {
        this.subscriber.getOrPut(timelineUserId, { mutableListOf() }).add(subscriberUserId)
    }
}