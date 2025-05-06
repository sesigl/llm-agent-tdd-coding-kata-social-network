package org.codingkata.socialnetwork

class TimelineService(
    private val timelineRepository: TimelineRepository = TimelineRepository()
) {

    private var subscriber: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun publishMessage(ownerUserId: String, content: String) {
        val timeline = timelineRepository.findOrCreate(ownerUserId)

        timeline.publish(content)
    }

    fun getMessages(userId: String, requesterUserId: String = userId): List<String> {
        return timelineRepository.findOrCreate(userId).getMessages()
    }

    fun subscribeTo(timelineUserId: String, subscriberUserId: String) {
        val timelineOfSubscriber = timelineRepository.findOrCreate(subscriberUserId)
        val timelineOfInterest = timelineRepository.findOrCreate(timelineUserId)

        timelineOfInterest.crossPublishValuesTo(timelineOfSubscriber)
    }
}