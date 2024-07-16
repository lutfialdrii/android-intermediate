package com.lutfi.storykuy

import com.lutfi.storykuy.data.models.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "test",
                "test",
                "test",
                "test",
                1.0,
                "1"
            )
            items.add(story)
        }
        return items
    }
}