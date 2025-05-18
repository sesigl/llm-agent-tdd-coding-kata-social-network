package org.codingkata.socialnetwork

/**
 * Value object representing a unique user identifier.
 */
data class UserId(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "User ID cannot be empty or blank" }
    }
}
