package org.codingkata.socialnetwork

import kotlin.collections.getOrPut

class Timeline {
    private val messages: MutableList<String> = mutableListOf()
    private val subscriberTimelines: MutableList<Timeline> = mutableListOf()

    fun postMessage(messageContent: String) {
        this.messages.add(messageContent)

        subscriberTimelines.forEach { it.postMessage(messageContent) }
    }

    fun getAllMessages(): List<String> {
        return this.messages
    }

    fun publishContentOnChangeTo(subscriberTimeline: Timeline) {
        subscriberTimelines.add(subscriberTimeline)
    }
}