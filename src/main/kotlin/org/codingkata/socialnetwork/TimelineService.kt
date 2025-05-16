package org.codingkata.socialnetwork

class TimelineService {
    // A map to store user timelines, where the key is the username and the value is the list of messages
    private val timelines: MutableMap<String, MutableList<Message>> = mutableMapOf()
    
    // A map to store user subscriptions, where the key is the follower and the value is a set of users they follow
    private val subscriptions: MutableMap<String, MutableSet<String>> = mutableMapOf()

    /**
     * Publishes a message from a user to their timeline
     * Messages are added to the beginning of the list to maintain reverse chronological order
     * 
     * @param user The username of the person publishing the message
     * @param messageContent The content of the message
     */
    fun publish(user: String, messageContent: String) {
        // Get or create the user's timeline
        val userTimeline = timelines.getOrPut(user) { mutableListOf() }
        
        // Create a Message object with the current timestamp
        val message = Message(user, messageContent)
        
        // Add the message to the beginning of the user's timeline (index 0)
        // This ensures newer messages appear first (reverse chronological order)
        userTimeline.add(0, message)
    }
    
    /**
     * Sends a direct message from one user to another
     * 
     * @param sender The username of the person sending the message
     * @param recipient The username of the person receiving the message
     * @param messageContent The content of the message
     */
    fun sendDirectMessage(sender: String, recipient: String, messageContent: String) {
        // Get or create the sender's timeline
        val senderTimeline = timelines.getOrPut(sender) { mutableListOf() }
        
        // Create a Message object with the recipient field set
        val message = Message(sender, messageContent, recipient = recipient)
        
        // Add the message to the beginning of the sender's timeline
        senderTimeline.add(0, message)
    }

    /**
     * Retrieves a user's public timeline (messages not sent as direct messages)
     * The timeline is in reverse chronological order (newest first)
     * 
     * @param user The username whose timeline to retrieve
     * @return List of public messages in the user's timeline in reverse chronological order
     */
    fun getTimeline(user: String): List<Message> {
        // Get the user's timeline or an empty list if the user has no timeline
        val userTimeline = timelines.getOrDefault(user, mutableListOf())
        
        // Filter out direct messages (only include messages where recipient is null)
        return userTimeline.filter { it.recipient == null }
    }
    
    /**
     * Retrieves direct messages sent to or by a user
     * The messages are in reverse chronological order (newest first)
     * 
     * @param user The username whose direct messages to retrieve
     * @return List of direct messages to or from the user in reverse chronological order
     */
    fun getDirectMessages(user: String): List<Message> {
        // Create a mutable list to collect all direct messages
        val directMessages = mutableListOf<Message>()
        
        // Check all user timelines for direct messages to or from this user
        for ((poster, messages) in timelines) {
            for (message in messages) {
                // Include messages where this user is the recipient
                if (message.recipient == user) {
                    directMessages.add(message)
                }
                // Include messages sent by this user to someone else
                else if (poster == user && message.recipient != null) {
                    directMessages.add(message)
                }
            }
        }
        
        // Sort direct messages by timestamp in descending order (newest first)
        return directMessages.sortedByDescending { it.timestamp }
    }
    
    /**
     * Records that a user is following another user
     * 
     * @param follower The username of the person who wants to follow another user
     * @param followee The username of the person being followed
     */
    fun follow(follower: String, followee: String) {
        // Get or create the follower's subscription set
        val userSubscriptions = subscriptions.getOrPut(follower) { mutableSetOf() }
        
        // Add the followee to the follower's subscriptions
        userSubscriptions.add(followee)
    }
    
    /**
     * Retrieves the set of users a given user is following
     * 
     * @param user The username whose subscriptions to retrieve
     * @return Set of usernames the user is following
     */
    fun getSubscriptions(user: String): Set<String> {
        // Return the user's subscriptions or an empty set if the user is not following anyone
        return subscriptions.getOrDefault(user, mutableSetOf())
    }
    
    /**
     * Retrieves an aggregated wall of messages from all users that the specified user follows
     * The wall contains only public messages from followed users, sorted by timestamp in descending order
     * 
     * @param user The username whose wall to retrieve
     * @return List of public messages from followed users, sorted by timestamp (newest first)
     */
    fun getWall(user: String): List<Message> {
        // Get the list of users that the user follows
        val followedUsers = getSubscriptions(user)
        
        // If the user is not following anyone, return an empty list
        if (followedUsers.isEmpty()) {
            return emptyList()
        }
        
        // Create a mutable list to collect all public messages from followed users
        val wallMessages = mutableListOf<Message>()
        
        // For each followed user, get their public timeline and add all messages to the wall
        for (followedUser in followedUsers) {
            val userTimeline = getTimeline(followedUser)
            wallMessages.addAll(userTimeline)
        }
        
        // Sort all messages by timestamp in descending order (newest first)
        return wallMessages.sortedByDescending { it.timestamp }
    }
}