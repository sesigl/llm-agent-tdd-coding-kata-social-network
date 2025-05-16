package org.codingkata.socialnetwork

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimelineServiceTest {

    @Test
    fun aliceCanPublishAMessageToHerTimeline() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Define a user "Alice" and a message "Hello, world!"
        val user = "Alice"
        val message = "Hello, world!"
        
        // Alice posts the message
        timelineService.publish(user, message)
        
        // Get Alice's timeline
        val aliceTimeline = timelineService.getTimeline(user)
        
        // Assert that Alice's timeline contains the published message
        assertTrue(aliceTimeline.contains(message), "Alice's timeline should contain the published message")
    }
}