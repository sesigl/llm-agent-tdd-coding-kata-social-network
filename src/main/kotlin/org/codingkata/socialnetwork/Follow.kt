package org.codingkata.socialnetwork

data class Follow(
    val follower: UserId,
    val followee: UserId,
    val timestamp: Timestamp = Timestamp(),
) {
    init {
        require(follower != followee) { "Users cannot follow themselves" }
    }
}
