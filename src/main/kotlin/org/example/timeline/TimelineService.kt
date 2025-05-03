package org.example.timeline

import org.example.Message
import org.example.UserId

class TimelineService(
    private val timelineRepository: TimelineRepository = TimelineRepository(),
) {

    fun publish(ownerUserId: String, messageContent: String) {
        val timeline = timelineRepository.findOrCreateTimelineFor(userId = UserId(ownerUserId))

        val message = Message(UserId(ownerUserId), messageContent)
        setupTimelineToPublishToMentionedUsersTimelines(timeline, message)

        timeline.publish(message)
    }

    private fun setupTimelineToPublishToMentionedUsersTimelines(timeline: Timeline, message: Message) {
        timeline.onMention { mentionedUserId: UserId ->
            val mentionedUserTimeline = timelineRepository.findOrCreateTimelineFor(mentionedUserId)
            mentionedUserTimeline.publish(message)
        }
    }

    fun getAllMessages(ownerUserId: String, requestedByUserId: String = ownerUserId): List<String> {
        val timeline = timelineRepository.findOrCreateTimelineFor(UserId(ownerUserId))
        return timeline.getAllMessages(UserId(requestedByUserId))
            .map { it.toMarkdown() }
    }

    fun subscribeTo(ownerUserId: String, subscriberUserId: String) {
        val ownerTimeline = timelineRepository.findOrCreateTimelineFor(UserId(ownerUserId))
        val subscriberTimeline = timelineRepository.findOrCreateTimelineFor(UserId(subscriberUserId))
        ownerTimeline.subscribeTo(subscriberTimeline)
    }

}
