package org.codingkata.socialnetwork

class TimelineService {
    // A map to store user timelines, where the key is the username and the value is the list of messages
    private val timelines: MutableMap<String, MutableList<String>> = mutableMapOf()

    /**
     * Publishes a message from a user to their timeline
     * Messages are added to the beginning of the list to maintain reverse chronological order
     * 
     * @param user The username of the person publishing the message
     * @param message The content of the message
     */
    fun publish(user: String, message: String) {
        // Get or create the user's timeline
        val userTimeline = timelines.getOrPut(user) { mutableListOf() }
        
        // Add the message to the beginning of the user's timeline (index 0)
        // This ensures newer messages appear first (reverse chronological order)
        userTimeline.add(0, message)
    }

    /**
     * Retrieves a user's timeline
     * The timeline is already in reverse chronological order (newest first)
     * 
     * @param user The username whose timeline to retrieve
     * @return List of messages in the user's timeline in reverse chronological order
     */
    fun getTimeline(user: String): List<String> {
        // Return the user's timeline or an empty list if the user has no timeline
        return timelines.getOrDefault(user, mutableListOf())
    }
}