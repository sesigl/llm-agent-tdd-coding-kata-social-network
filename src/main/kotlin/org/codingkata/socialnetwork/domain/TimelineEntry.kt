package org.codingkata.socialnetwork.domain

import java.time.Instant

data class TimelineEntry(
    val message: Message,
    val timestamp: Instant,
)
