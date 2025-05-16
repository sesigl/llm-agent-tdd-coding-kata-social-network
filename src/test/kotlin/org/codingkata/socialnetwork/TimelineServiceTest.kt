package org.codingkata.socialnetwork

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
    
    @Test
    fun charlieSeesAggregatedWallInReverseChronologicalOrder() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Users
        val alice = "Alice"
        val bob = "Bob"
        val charlie = "Charlie"
        val david = "David"
        
        // Alice's posts
        val aliceFirstPost = "Alice's first post"
        timelineService.publish(alice, aliceFirstPost)
        Thread.sleep(5)
        
        // Bob's first post
        val bobFirstPost = "Bob's first post"
        timelineService.publish(bob, bobFirstPost)
        Thread.sleep(5)
        
        // Charlie follows Alice and Bob
        timelineService.follow(charlie, alice)
        timelineService.follow(charlie, bob)
        
        // Alice's second post
        val aliceSecondPost = "Alice's second post"
        timelineService.publish(alice, aliceSecondPost)
        Thread.sleep(5)
        
        // Bob's second post
        val bobSecondPost = "Bob's second post"
        timelineService.publish(bob, bobSecondPost)
        Thread.sleep(5)
        
        // David's post (Charlie doesn't follow David)
        val davidPost = "David's post"
        timelineService.publish(david, davidPost)
        
        // Charlie gets his wall
        val charlieWall = timelineService.getWall(charlie)
        
        // Assert correct order and content
        assertEquals(4, charlieWall.size, "Charlie's wall should have 4 messages")
        
        // Messages should be in reverse chronological order
        assertEquals(bobSecondPost, charlieWall[0].content, "Bob's second post should be first")
        assertEquals(aliceSecondPost, charlieWall[1].content, "Alice's second post should be second")
        assertEquals(bobFirstPost, charlieWall[2].content, "Bob's first post should be third")
        assertEquals(aliceFirstPost, charlieWall[3].content, "Alice's first post should be fourth")
        
        // David's post should not be on Charlie's wall
        assertFalse(
            charlieWall.any { it.user == david && it.content == davidPost },
            "David's post should not appear on Charlie's wall"
        )
    }
    
    @Test
    fun messageCanContainMentions() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Bob publishes a message mentioning Charlie
        val bob = "Bob"
        val messageWithMention = "Hello @Charlie, how are you?"
        timelineService.publish(bob, messageWithMention)
        
        // Retrieve Bob's timeline
        val bobTimeline = timelineService.getTimeline(bob)
        
        // Assert that the message with the mention is in Bob's timeline
        assertTrue(
            bobTimeline.any { it.user == bob && it.content == messageWithMention },
            "Bob's timeline should contain the message with the mention"
        )
        
        // Assert that the message content is preserved exactly as it was entered
        assertEquals(messageWithMention, bobTimeline[0].content,
            "The message content should be preserved exactly, including the @mention")
    }
    
    @Test
    fun messageCanContainLinks() {
        // Create an instance of TimelineService
        val timelineService = TimelineService()
        
        // Alice publishes a message with a link
        val alice = "Alice"
        val messageWithLink = "Check out this site: http://example.com"
        timelineService.publish(alice, messageWithLink)
        
        // Retrieve Alice's timeline
        val aliceTimeline = timelineService.getTimeline(alice)
        
        // Assert that the message with the link is in Alice's timeline
        assertTrue(
            aliceTimeline.any { it.user == alice && it.content == messageWithLink },
            "Alice's timeline should contain the message with the link"
        )
        
        // Assert that the message content is preserved exactly as it was entered
        assertEquals(messageWithLink, aliceTimeline[0].content,
            "The message content should be preserved exactly, including the link")
    }
}