package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test

class TimelineServiceTest {

    @Test
    fun example() {
        val timelineService = TimelineService()
        timelineService.publishMessage(
            messageContent = "Happy coding",
            userId = "alice"
        )
    }
}