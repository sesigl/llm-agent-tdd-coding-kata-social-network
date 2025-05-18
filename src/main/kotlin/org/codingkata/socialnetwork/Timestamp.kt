package org.codingkata.socialnetwork

import java.time.Instant

/**
 * Value object representing a moment in time.
 * Wraps java.time.Instant for domain clarity.
 */
data class Timestamp(
    val value: Instant = Instant.now(),
) : Comparable<Timestamp> {
    override fun compareTo(other: Timestamp): Int = value.compareTo(other.value)
}
