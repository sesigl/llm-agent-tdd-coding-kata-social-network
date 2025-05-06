package org.codingkata.socialnetwork

class Timeline {
    private val subscriberTimelines: MutableList<Timeline> = mutableListOf()
    private val messages: MutableList<String> = mutableListOf()

    fun getMessages(): List<String> {
        return this.messages
    }

    fun publish(content: String) {
        this.messages.add(content)

        this.subscriberTimelines.forEach {
            it.publish(content)
        }
    }

    fun crossPublishValuesTo(timelineOfSubscriber: Timeline) {
        this.subscriberTimelines.add(timelineOfSubscriber)
    }
}