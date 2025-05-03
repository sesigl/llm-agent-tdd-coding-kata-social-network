package org.example.privatmessage

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PrivateMessageServiceTest {

    private lateinit var privateMessageService: PrivateMessageService

    @BeforeEach
    fun setUp() {
        privateMessageService = PrivateMessageService()
    }

    @Test
    fun `mallory can send a private message to alice`() {
        privateMessageService.sendPrivateMessage(fromUserId = "mallory", toUserId = "alice", message = "how are you alice?")
        assertEquals(listOf("how are you alice?"), privateMessageService.getAllPrivateMessages(ownerUserId = "alice"))
    }
}