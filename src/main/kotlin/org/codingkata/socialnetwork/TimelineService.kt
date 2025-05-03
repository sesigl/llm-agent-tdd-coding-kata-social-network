package org.codingkata.socialnetwork

class TimelineService (
    private val timelineRepository: TimelineRepository = TimelineRepository()
){
    fun publishMessage(messageContent: String, userId: String) {
        timelineRepository.findBy(userId).publishMessage(messageContent)
    }

    fun getAllMessages(userId: String): List<String> {
        return timelineRepository.findBy(userId).getAllMessages()
    }
}