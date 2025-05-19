package org.codingkata.socialnetwork

class AggregatedTimeline(
    val user: User,
    private val userTimeline: Timeline,
    private val userFollowing: UserFollowing,
    private val timelines: Map<User, Timeline>,
) {
    fun getMessages(): List<Message> {
        val userMessages = getUserMessages()
        val followedUsersMessages = getFollowedUsersMessages()
        val combinedMessages = userMessages + followedUsersMessages

        return sortMessagesByTimestampDescending(combinedMessages)
    }

    private fun getUserMessages(): List<Message> = userTimeline.getMessages()

    private fun getFollowedUsersMessages(): List<Message> =
        userFollowing
            .getFollowedUsers()
            .mapNotNull { followedUser -> timelines[followedUser] }
            .flatMap { timeline -> timeline.getMessages() }

    private fun sortMessagesByTimestampDescending(messages: List<Message>): List<Message> = messages.sortedByDescending { it.timestamp }
}
