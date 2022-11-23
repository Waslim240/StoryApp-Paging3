package com.waslim.storyapp

import com.waslim.storyapp.model.response.login.LoginResponse
import com.waslim.storyapp.model.response.login.LoginResult
import com.waslim.storyapp.model.response.register.RegisterResponse
import com.waslim.storyapp.model.response.story.AddNewStoryResponse
import com.waslim.storyapp.model.response.story.GetAllStoryResponse
import com.waslim.storyapp.model.response.story.Story

object DataDummy {

    fun showDataDummyStory(): Story {
        return Story(
            "dsa13",
            "create date",
            "This is description",
            -6.159839,
            107.489743,
            "name",
            "file"
        )
    }

    fun showDataDummyStoryResponse(): GetAllStoryResponse {
        val listStory = ArrayList<Story>()
        for (i in 0 until 10) {
            val story = Story(
                "dsa13$i",
                "create date",
                "This is description",
                -6.159839,
                107.489743,
                "name",
                "file"
            )
            listStory.add(story)
        }
        return GetAllStoryResponse(false, listStory, "success")
    }

    fun showDataDummyListStories(): List<Story> {
        val storyList = arrayListOf<Story>()
        for (i in 0..5) {
            val listStory = Story(
                "story-yhrENjW8EgBXLXuy$i",
                "2022-11-22T08:45:49.085Z",
                "haloo",
                -6.158394,
                107.4919035,
                "Lim",
                "https://story-api.dicoding.dev/images/stories/photos-1669106749083_sT2s6eM9.jpg"
            )
            storyList.add(listStory)
        }
        return storyList
    }


    fun showDataDummyLoginResponse(): LoginResponse{
        return LoginResponse(
            LoginResult(
                "Lim",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDdWxlLVVPVlBmQzhxQVYiLCJpYXQiOjE2NjgxNzU5OTB9.fVSTZnmZBLauSxiVuPvqgi9B_q0_HmFDBIs__QNRKw8",
                "id"
            ),
            false,
            "success"
        )
    }

    fun showDataDummyRegisterResponse(): RegisterResponse{
        return RegisterResponse(
            false,
            "success"
        )
    }

    fun showDataDummyAddStoryResponse(): AddNewStoryResponse{
        return AddNewStoryResponse(
            false,
            "success"
        )
    }
}