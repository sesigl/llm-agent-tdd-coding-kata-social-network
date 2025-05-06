package org.codingkata.socialnetwork

class TimelineService(
    private val timelineRepository: TimelineRepository = TimelineRepository()
) {

    private var subscriber: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun publishMessage(ownerUserId: String, content: String) {
        timelineRepository.findOrCreate(ownerUserId).publish(content)

        subscriber.getOrPut(ownerUserId, { mutableListOf() }).forEach {
            timelineRepository.findOrCreate(it).publish(content)
        }
    }

    fun getMessages(userId: String, requesterUserId: String = userId): List<String> {
        return timelineRepository.findOrCreate(userId).getMessages()
    }

    fun subscribeTo(timelineUserId: String, subscriberUserId: String) {
        this.subscriber.getOrPut(timelineUserId, { mutableListOf() }).add(subscriberUserId)
    }
}