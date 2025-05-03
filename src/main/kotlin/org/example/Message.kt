package org.example

data class Message(val author: UserId, val content: String) {
    fun toMarkdown(): String {
        val contentWithEncodedMentions = this.replaceMentionsWithUserLinks(content)
        val contentWithEncodedUrls = this.replaceUrlsWithEncodedHttpsLinks(contentWithEncodedMentions)

        return contentWithEncodedUrls
    }

    /**
     * Example 1
     *
     * Input: "link: www.google.de !"
     * Output: "link: <url:https://www.google.de> !"
     *
     * Example 2
     *
     * Input: "link: https://www.google.de !"
     * Output: "link: <url:https://www.google.de> !"
     *
     * Example 3
     *
     * Input: "link: google.de !"
     * Output: "link: google.de !"
     */
    private fun replaceUrlsWithEncodedHttpsLinks(contentWithEncodedMentions: String): String {
        val urlRegex = Regex("""\b(www\.[\w\-.]+\.[a-z]{2,}|https?://[\w\-.]+\.[a-z]{2,})""", RegexOption.IGNORE_CASE)
        return urlRegex.replace(contentWithEncodedMentions) { matchResult ->
            val url = matchResult.value
            val encodedUrl = if (url.startsWith("http")) url else "https://$url"
            "<url:$encodedUrl>"
        }
    }

    /**
     * Example
     *
     * Input "Hello @bob !"
     * Output "Hello @<user:bob> !"
     */
    private fun replaceMentionsWithUserLinks(content: String): String {
        val regex = Regex("""@([a-zA-Z0-9_]+)""")
        return regex.replace(content) { matchResult ->
            val userId = matchResult.groupValues[1]
            "@<user:$userId>"
        }
    }

    /**
     * For a given string, extract all @name , returning the name without "@".
     *
     * For example:
     * Input: "Hello @alice and @bob, how are you @bob?"
     * Output: ["alice", "bob"]
     */
    fun extractMentionedUsersFromMessage(): List<UserId> {
        val regex = Regex("""@([a-zA-Z0-9_]+)""")
        return regex.findAll(content)
            .map { matchResult -> UserId(matchResult.groupValues[1]) }
            .distinct()
            .toList()
    }
}