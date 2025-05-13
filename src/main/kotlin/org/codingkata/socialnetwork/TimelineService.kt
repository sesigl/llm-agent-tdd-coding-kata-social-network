package org.codingkata.socialnetwork

class TimelineService(
    private val timelineRepository: TimelineRepository = TimelineRepository(),
) {
    private val subscribers: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun postMessage(authorUserId: String, messageContent: String) {
        val authorTimeline = timelineRepository.getOrCreateTimeline(authorUserId)
        authorTimeline.postMessage(messageContent)
    }

    fun getAllMessages(authorUserId: String, requesterUserId: String = authorUserId): List<String> {
        return timelineRepository.getOrCreateTimeline(authorUserId).getAllMessages()
    }

    fun subscribe(subscriberUserId: String, targetTimelineUserId: String) {
        val targetTimeline = timelineRepository.getOrCreateTimeline(targetTimelineUserId)
        val subscriberTimeline = timelineRepository.getOrCreateTimeline(subscriberUserId)

        targetTimeline.publishContentOnChangeTo(subscriberTimeline)
    }

}