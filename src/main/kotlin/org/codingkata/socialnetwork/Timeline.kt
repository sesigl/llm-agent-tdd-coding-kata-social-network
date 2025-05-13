package org.codingkata.socialnetwork

class Timeline {
    private val messages: MutableList<String> = mutableListOf()
    private val subscriberTimelines: MutableList<Timeline> = mutableListOf()

    fun postMessage(messageContent: String) {

        val encodedMessageContentWithUserLinks = encodeUserLinks(messageContent)
        val encodedMessageContentWithUrls = encodeUrls(encodedMessageContentWithUserLinks)

        this.messages.add(encodedMessageContentWithUrls)

        subscriberTimelines.forEach { it.postMessage(messageContent) }
    }

    private fun encodeUrls(encodedMessageContentWithUserLinks: String): String {
        val regex = Regex("https?://[\\w.-]+(?:\\.[\\w.-]+)+[/\\w.-]*")
        val encodedMessageContent = encodedMessageContentWithUserLinks.replace(regex) { matchResult ->
            "<url:${matchResult.value}>"
        }
        return encodedMessageContent
    }

    private fun encodeUserLinks(messageContent: String): String {
        val regex = Regex("@(\\w+)")
        val encodedMessageContent = messageContent.replace(regex) { matchResult ->
            "<user:${matchResult.groupValues[1]}>"
        }
        return encodedMessageContent
    }

    fun getAllMessages(): List<String> {
        return this.messages
    }

    fun publishContentOnChangeTo(subscriberTimeline: Timeline) {
        subscriberTimelines.add(subscriberTimeline)
    }
}