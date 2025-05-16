package org.codingkata.socialnetwork

class TimelineService {
    // A map to store user timelines, where the key is the username and the value is the list of messages
    private val timelines: MutableMap<String, MutableList<String>> = mutableMapOf()

    /**
     * Publishes a message from a user to their timeline
     * 
     * @param user The username of the person publishing the message
     * @param message The content of the message
     */
    fun publish(user: String, message: String) {
        // Get or create the user's timeline
        val userTimeline = timelines.getOrPut(user) { mutableListOf() }
        
        // Add the message to the user's timeline
        userTimeline.add(message)
    }

    /**
     * Retrieves a user's timeline
     * 
     * @param user The username whose timeline to retrieve
     * @return List of messages in the user's timeline
     */
    fun getTimeline(user: String): List<String> {
        // Return the user's timeline or an empty list if the user has no timeline
        return timelines.getOrDefault(user, mutableListOf())
    }
}