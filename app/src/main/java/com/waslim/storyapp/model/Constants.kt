package com.waslim.storyapp.model

import com.waslim.storyapp.model.response.story.Story

object Constants {
    const val BEARER = "Bearer "
    const val PAGE_SIZE = 5
    const val INITIAL_PAGE_INDEX = 1
    const val LOCATION = 1
    const val DELAY = 2000L

    fun mapStory(story: Story) : Story = Story(
        story.id,
        story.createdAt,
        story.description,
        story.lat,
        story.lon,
        story.name,
        story.photoUrl
    )
}