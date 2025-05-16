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
    
    @Test
    fun bobCanViewAlicesTimeline() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Alice publishes a message
        val alice = "Alice"
        val aliceMessage = "Alice's message"
        timelineService.publish(alice, aliceMessage)
        
        // Bob views Alice's timeline
        val bob = "Bob"
        val aliceTimelineViewedByBob = timelineService.getTimeline(alice)
        
        // Assert that Bob sees Alice's message
        assertTrue(aliceTimelineViewedByBob.contains(aliceMessage), 
            "Bob should be able to see Alice's message when viewing her timeline")
    }
    
    @Test
    fun charlieCanSubscribeToAlicesTimeline() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Charlie follows Alice
        val charlie = "Charlie"
        val alice = "Alice"
        timelineService.follow(charlie, alice)
        
        // Get Charlie's subscriptions
        val charlieSubscriptions = timelineService.getSubscriptions(charlie)
        
        // Assert that Charlie is following Alice
        assertTrue(charlieSubscriptions.contains(alice), 
            "Charlie's subscriptions should include Alice")
    }
}