package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class TimelineServiceTest {
    @Test
    fun `user can post message to timeline`() {
        val timelineService = TimelineService()
        val username = "Alice"
        val message = "Hello, world!"

        timelineService.post(username, message)
        val messages = timelineService.getMessages(username)

        assertEquals(1, messages.size)
        assertEquals(message, messages[0])
    }

    @Test
    fun `messages should have timestamps`() {
        val timelineService = TimelineService()
        val username = "Alice"
        val message = "Hello, world!"
        val beforePost = Instant.now()

        timelineService.post(username, message)
        val messages = timelineService.getMessagesWithTime(username)
        val afterPost = Instant.now()

        assertEquals(1, messages.size)
        val (content, timestamp) = messages[0]
        assertEquals(message, content)
        assertNotNull(timestamp)
        assert(timestamp >= beforePost) { "Timestamp should be after the post was created" }
        assert(timestamp <= afterPost) { "Timestamp should be before the assertion was executed" }
    }

    @Test
    fun `timeline should display messages in chronological order with newest first`() {
        val timelineService = TimelineService()
        val username = "Alice"
        val message1 = "First message"
        val message2 = "Second message"
        val message3 = "Third message"

        timelineService.post(username, message1)
        Thread.sleep(10) // Ensure different timestamps
        timelineService.post(username, message2)
        Thread.sleep(10) // Ensure different timestamps
        timelineService.post(username, message3)

        val messagesWithTime = timelineService.getTimelineWithTime(username)
        val messages = timelineService.getTimeline(username)

        assertEquals(3, messagesWithTime.size)
        assertEquals(3, messages.size)

        assertEquals(message3, messages[0])
        assertEquals(message2, messages[1])
        assertEquals(message1, messages[2])

        assert(messagesWithTime[0].second > messagesWithTime[1].second)
        assert(messagesWithTime[1].second > messagesWithTime[2].second)
    }

    @Test
    fun `should retrieve timeline messages as domain objects`() {
        val timelineService = TimelineService()
        val username = "Alice"
        val message1 = "First message"
        val message2 = "Second message"

        timelineService.post(username, message1)
        Thread.sleep(10) // Ensure different timestamps
        timelineService.post(username, message2)

        val messages = timelineService.getTimelineMessages(username)

        assertEquals(2, messages.size)
        assertEquals(message2, messages[0].content)
        assertEquals(username, messages[0].author)
        assertEquals(message1, messages[1].content)
        assertEquals(username, messages[1].author)
    }

    @Test
    fun `user should be able to follow another user`() {
        val timelineService = TimelineService()
        val follower = "Alice"
        val followee = "Bob"

        timelineService.follow(follower, followee)

        val following = timelineService.getFollowing(follower)

        assertEquals(1, following.size)
        assertTrue(following.contains(followee))
    }

    @Test
    fun `should get aggregated timeline from followed users`() {
        val timelineService = TimelineService()
        val alice = "Alice"
        val bob = "Bob"
        val charlie = "Charlie"

        // Alice follows Bob and Charlie
        timelineService.follow(alice, bob)
        timelineService.follow(alice, charlie)

        // Each user posts messages
        timelineService.post(alice, "Alice's message")
        Thread.sleep(10)
        timelineService.post(bob, "Bob's message")
        Thread.sleep(10)
        timelineService.post(charlie, "Charlie's message")

        // Get Alice's aggregated timeline
        val aggregatedTimeline = timelineService.getAggregatedTimeline(alice)

        // Should contain messages from Bob and Charlie, but not Alice's own message
        assertEquals(2, aggregatedTimeline.size)
        assertEquals("Charlie's message", aggregatedTimeline[0].content)
        assertEquals(charlie, aggregatedTimeline[0].author)
        assertEquals("Bob's message", aggregatedTimeline[1].content)
        assertEquals(bob, aggregatedTimeline[1].author)
    }

    @Test
    fun `following edge cases should be handled correctly`() {
        val timelineService = TimelineService()
        val alice = "Alice"

        // Self-following should be ignored
        timelineService.follow(alice, alice)
        val following = timelineService.getFollowing(alice)
        assertEquals(0, following.size, "Self-following should be ignored")

        // Duplicate follows should be ignored
        val bob = "Bob"
        timelineService.follow(alice, bob)
        timelineService.follow(alice, bob)
        assertEquals(1, timelineService.getFollowing(alice).size, "Duplicate follows should be ignored")

        // Aggregated timeline with no followees should return empty list
        val charlie = "Charlie" // Charlie doesn't follow anyone
        assertEquals(
            0,
            timelineService.getAggregatedTimeline(charlie).size,
            "Aggregated timeline with no followees should return empty list",
        )

        // Aggregated timeline with followees who have no posts should return empty list
        val david = "David"
        timelineService.follow(david, alice) // Alice has no posts yet in this test context
        assertEquals(
            0,
            timelineService.getAggregatedTimeline(david).size,
            "Aggregated timeline with followees who have no posts should return empty list",
        )
    }

    @Test
    fun `should detect mentions in message content`() {
        val timelineService = TimelineService()
        val alice = "Alice"
        val bob = "Bob"
        val charlie = "Charlie"

        // Post a message with mentions
        timelineService.post(alice, "Hey @Bob, have you talked to @Charlie recently?")

        // Get the message
        val message = timelineService.getTimelineMessages(alice).first()

        // Get the mentions from the message
        val mentions = timelineService.getMentionsFromMessage(message)

        // Verify mentions were detected
        assertEquals(2, mentions.size)
        assertTrue(mentions.contains(bob))
        assertTrue(mentions.contains(charlie))
    }

    @Test
    fun `should find messages where a user is mentioned`() {
        val timelineService = TimelineService()
        val alice = "Alice"
        val bob = "Bob"
        val charlie = "Charlie"
        val david = "David"

        // Post messages with mentions
        timelineService.post(alice, "Hey @Bob, how are you?")
        Thread.sleep(10)
        timelineService.post(bob, "Hi @Alice, I'm good. Have you seen @Charlie?")
        Thread.sleep(10)
        timelineService.post(charlie, "Hello @Alice and @Bob, yes I'm here!")
        Thread.sleep(10)
        timelineService.post(david, "No mentions here")

        // Find messages where Alice is mentioned
        val mentionsForAlice = timelineService.getMentionsForUser(alice)

        // Verify messages where Alice is mentioned
        assertEquals(2, mentionsForAlice.size)
        assertEquals(charlie, mentionsForAlice[0].author) // Newest first
        assertEquals(bob, mentionsForAlice[1].author)

        // Find messages where Bob is mentioned
        val mentionsForBob = timelineService.getMentionsForUser(bob)

        // Verify messages where Bob is mentioned
        assertEquals(2, mentionsForBob.size)
        assertEquals(charlie, mentionsForBob[0].author) // Newest first
        assertEquals(alice, mentionsForBob[1].author)

        // Find messages where David is mentioned (should be empty)
        val mentionsForDavid = timelineService.getMentionsForUser(david)
        assertEquals(0, mentionsForDavid.size)
    }

    @Test
    fun `should detect links in message content`() {
        val timelineService = TimelineService()
        val alice = "Alice"

        // Post a message with links
        timelineService.post(alice, "Check out https://example.com and http://test.org")

        // Get the message
        val message = timelineService.getTimelineMessages(alice).first()

        // Get the links from the message
        val links = timelineService.getLinksFromMessage(message)

        // Verify links were detected
        assertEquals(2, links.size)
        assertTrue(links.contains("https://example.com"))
        assertTrue(links.contains("http://test.org"))
    }
}
