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
    
    @Test
    fun aliceTimelineShowsMessagesInReverseChronologicalOrder() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Define a user "Alice"
        val user = "Alice"
        
        // Alice publishes "First message"
        val firstMessage = "First message"
        timelineService.publish(user, firstMessage)
        
        // Alice then publishes "Second message"
        val secondMessage = "Second message"
        timelineService.publish(user, secondMessage)
        
        // Get Alice's timeline
        val aliceTimeline = timelineService.getTimeline(user)
        
        // Assert that "Second message" appears before "First message"
        assertEquals(secondMessage, aliceTimeline[0], "Second message should be first in the timeline")
        assertEquals(firstMessage, aliceTimeline[1], "First message should be second in the timeline")
    }
}