package org.codingkata.socialnetwork

data class TimelineId(
    val userId: UserId,
) {
    companion object {
        fun fromUserId(userId: UserId): TimelineId = TimelineId(userId)
    }
}
