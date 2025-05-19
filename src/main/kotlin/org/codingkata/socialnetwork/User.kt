package org.codingkata.socialnetwork

data class User(
    val username: String,
) {
    fun isSameUser(other: User): Boolean = this.username == other.username

    fun isNamed(name: String): Boolean = this.username == name

    fun getUsernameDisplay(): String = "@$username"
}
