package org.codingkata.socialnetwork

class Timeline(
    val owner: User,
) {
    private val messages: MutableList<Message> = mutableListOf()

    fun addMessage(message: Message) {
        if (message.isPostedBy(owner)) {
            messages.add(message)
        }
    }

    fun getMessages(): List<Message> = messages.toList()

    fun getMessagesInReverseChronologicalOrder(): List<Message> = messages.sortedByDescending { it.timestamp }
}
