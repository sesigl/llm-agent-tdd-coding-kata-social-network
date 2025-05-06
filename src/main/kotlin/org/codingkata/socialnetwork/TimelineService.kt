package org.codingkata.socialnetwork

class TimelineService {
    private var messages: MutableMap<String, Timeline> = mutableMapOf()
    private var subscriber: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun publishMessage(ownerUserId: String, content: String) {
        this.messages.getOrPut(ownerUserId, { Timeline() }).publish(content)

        subscriber.getOrPut(ownerUserId, { mutableListOf() }).forEach {
            this.messages.getOrPut(it, { Timeline() }).publish(content)
        }
    }
    fun getMessages(userId: String, requesterUserId: String = userId): List<String> {
        return this.messages.getOrDefault(userId, Timeline()).getMessages()
    }

    fun subscribeTo(timelineUserId: String, subscriberUserId: String) {
        this.subscriber.getOrPut(timelineUserId, { mutableListOf() }).add(subscriberUserId)
    }
}