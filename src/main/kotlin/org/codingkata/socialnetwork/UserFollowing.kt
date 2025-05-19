package org.codingkata.socialnetwork

class UserFollowing(
    val user: User,
) {
    private val followings: MutableSet<Following> = mutableSetOf()

    fun follow(userToFollow: User) {
        val following = Following(user, userToFollow)
        followings.add(following)
    }

    fun getFollowedUsers(): List<User> = followings.map { it.followee }

    fun getFollowings(): List<Following> = followings.toList()
}
