package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class TimelineTest {
    @Test
    fun `timeline is created for a specific user`() {
        val user = User("alice")
        val timeline = Timeline(user)

        assertEquals(user, timeline.owner)
        assertTrue(timeline.getMessages().isEmpty())
    }

    @Test
    fun `timeline can add messages`() {
        val user = User("bob")
        val timeline = Timeline(user)
        val message = Message("Hello", user, Instant.now())

        timeline.addMessage(message)

        assertEquals(1, timeline.getMessages().size)
        assertEquals(message, timeline.getMessages()[0])
    }

    @Test
    fun `timeline returns messages in order they were added`() {
        val user = User("charlie")
        val timeline = Timeline(user)
        val first = Message("First", user, Instant.now())
        val second = Message("Second", user, Instant.now().plusSeconds(1))

        timeline.addMessage(first)
        timeline.addMessage(second)

        val messages = timeline.getMessages()
        assertEquals(2, messages.size)
        assertEquals(first, messages[0])
        assertEquals(second, messages[1])
    }

    @Test
    fun `timeline can return messages in reverse chronological order`() {
        val user = User("dave")
        val timeline = Timeline(user)
        val older = Message("Older", user, Instant.now().minus(1, ChronoUnit.HOURS))
        val newer = Message("Newer", user, Instant.now())

        timeline.addMessage(older)
        timeline.addMessage(newer)

        val messages = timeline.getMessagesInReverseChronologicalOrder()
        assertEquals(2, messages.size)
        assertEquals(newer, messages[0])
        assertEquals(older, messages[1])
    }

    @Test
    fun `timeline enforces that messages are from the timeline owner`() {
        val alice = User("alice")
        val bob = User("bob")
        val timeline = Timeline(alice)

        val aliceMessage = Message("Alice's message", alice, Instant.now())
        timeline.addMessage(aliceMessage)

        // Bob's message should not be added
        val bobMessage = Message("Bob's message", bob, Instant.now())
        timeline.addMessage(bobMessage)

        assertEquals(1, timeline.getMessages().size)
        assertEquals(aliceMessage, timeline.getMessages()[0])
    }
}
