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
        val messageContent = "Hello, world!"
        
        // Alice posts the message
        timelineService.publish(user, messageContent)
        
        // Get Alice's timeline
        val aliceTimeline = timelineService.getTimeline(user)
        
        // Assert that Alice's timeline contains the published message
        assertTrue(
            aliceTimeline.any { it.user == user && it.content == messageContent },
            "Alice's timeline should contain the published message"
        )
    }
    
    @Test
    fun aliceTimelineShowsMessagesInReverseChronologicalOrder() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Define a user "Alice"
        val user = "Alice"
        
        // Alice publishes "First message"
        val firstMessageContent = "First message"
        timelineService.publish(user, firstMessageContent)
        
        // Sleep to ensure distinct timestamps for reliable testing
        Thread.sleep(5)
        
        // Alice then publishes "Second message"
        val secondMessageContent = "Second message"
        timelineService.publish(user, secondMessageContent)
        
        // Get Alice's timeline
        val aliceTimeline = timelineService.getTimeline(user)
        
        // Assert that "Second message" appears before "First message"
        assertEquals(secondMessageContent, aliceTimeline[0].content, 
            "Second message should be first in the timeline")
        assertEquals(firstMessageContent, aliceTimeline[1].content, 
            "First message should be second in the timeline")
    }
    
    @Test
    fun bobCanViewAlicesTimeline() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Alice publishes a message
        val alice = "Alice"
        val aliceMessageContent = "Alice's message"
        timelineService.publish(alice, aliceMessageContent)
        
        // Bob views Alice's timeline
        val bob = "Bob"
        val aliceTimelineViewedByBob = timelineService.getTimeline(alice)
        
        // Assert that Bob sees Alice's message
        assertTrue(
            aliceTimelineViewedByBob.any { it.user == alice && it.content == aliceMessageContent },
            "Bob should be able to see Alice's message when viewing her timeline"
        )
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