package org.codingkata.socialnetwork

class Timeline {
    private val messages: MutableList<String> = mutableListOf()
    private val subscriberTimelines: MutableList<Timeline> = mutableListOf()

    fun postMessage(messageContent: String) {

        val encodedMessageContent = encodeUserLinks(messageContent)

        this.messages.add(encodedMessageContent)

        subscriberTimelines.forEach { it.postMessage(messageContent) }
    }

    /**
     * Encodes all user mentions int <user:$username>
     * Input: "message from bob about @charlie!"
     * Output: "message from bob about <user:charlie>!"
     */
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