package org.example.timeline

import org.example.Message
import org.example.UserId

typealias MentionListenerFunction = (UserId) -> Unit

data class Timeline(
    val ownerUserId: UserId,
    val allowedUserIds: List<UserId> = emptyList(),
    val messages: MutableList<Message> = mutableListOf()
) {
    private val onMentionListenerFunctions: MutableList<MentionListenerFunction> = mutableListOf()
    private val subscribedTimelines: MutableList<Timeline> = mutableListOf()

    fun publish(message: Message) {
        messages.add(message)

        message.extractMentionedUsersFromMessage()
            .forEach { mentionedUserId ->
                onMentionListenerFunctions.forEach { it.invoke(mentionedUserId) }
            }

        subscribedTimelines.forEach { it.publish(message) }
    }

    fun getAllMessages(requestedByUserId: UserId = ownerUserId): List<Message> {
        if (requestedByUserId != ownerUserId && requestedByUserId !in allowedUserIds) {
            throw NotAllowed(requestedByUserId)
        }
        return messages
    }

    fun alsoPublishMessagesTo(timeline: Timeline) {
        this.subscribedTimelines.add(timeline)
    }

    fun subscribeTo(timelineToSubscribeTo: Timeline) {
        timelineToSubscribeTo.alsoPublishMessagesTo(this)
    }

    fun onMention(function: MentionListenerFunction) {
        onMentionListenerFunctions.add(function)
    }

    class NotAllowed(userId: UserId) : Exception("User $userId is not allowed to view this timeline.")
}
