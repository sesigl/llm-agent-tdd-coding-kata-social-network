package org.codingkata.socialnetwork

class TimelineService(
    private val timelineRepository: TimelineRepository = TimelineRepository(),
) {
    private val subscribers: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun postMessage(authorUserId: String, messageContent: String) {
        timelineRepository.getOrCreateTimeline(authorUserId).postMessage(messageContent)

        subscribers.getOrPut(authorUserId, { mutableListOf() }).forEach {
            timelineRepository.getOrCreateTimeline(it).postMessage(messageContent)
        }
    }

    fun getAllMessages(authorUserId: String, requesterUserId: String = authorUserId): List<String> {
        return timelineRepository.getOrCreateTimeline(authorUserId).getAllMessages()
    }

    fun subscribe(subscriberUserId: String, targetTimelineUserId: String) {
        subscribers.getOrPut(targetTimelineUserId, { mutableListOf() }).add(subscriberUserId)
    }

}