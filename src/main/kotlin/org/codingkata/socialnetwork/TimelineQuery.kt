package org.codingkata.socialnetwork

data class TimelineQuery private constructor(
    val limit: Int? = null,
    val beforeTimestamp: Timestamp? = null,
    val afterTimestamp: Timestamp? = null,
) {
    companion object {
        fun default(): TimelineQuery = TimelineQuery()

        fun withLimit(limit: Int): TimelineQuery {
            require(limit > 0) { "Limit must be greater than zero" }
            return TimelineQuery(limit = limit)
        }

        fun before(timestamp: Timestamp): TimelineQuery = TimelineQuery(beforeTimestamp = timestamp)

        fun after(timestamp: Timestamp): TimelineQuery = TimelineQuery(afterTimestamp = timestamp)

        fun between(
            after: Timestamp,
            before: Timestamp,
        ): TimelineQuery {
            require(after.value.isBefore(before.value)) { "After timestamp must be earlier than before timestamp" }
            return TimelineQuery(
                beforeTimestamp = before,
                afterTimestamp = after,
            )
        }

        fun builder(): Builder = Builder()
    }

    class Builder {
        private var limit: Int? = null
        private var beforeTimestamp: Timestamp? = null
        private var afterTimestamp: Timestamp? = null

        fun limit(limit: Int): Builder {
            require(limit > 0) { "Limit must be greater than zero" }
            this.limit = limit
            return this
        }

        fun before(timestamp: Timestamp): Builder {
            this.beforeTimestamp = timestamp
            return this
        }

        fun after(timestamp: Timestamp): Builder {
            this.afterTimestamp = timestamp
            return this
        }

        fun build(): TimelineQuery {
            if (beforeTimestamp != null && afterTimestamp != null) {
                require(afterTimestamp!!.value.isBefore(beforeTimestamp!!.value)) {
                    "After timestamp must be earlier than before timestamp"
                }
            }
            return TimelineQuery(
                limit = limit,
                beforeTimestamp = beforeTimestamp,
                afterTimestamp = afterTimestamp,
            )
        }
    }
}
