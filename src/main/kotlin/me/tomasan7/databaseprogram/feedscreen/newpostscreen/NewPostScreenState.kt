package me.tomasan7.databaseprogram.feedscreen.newpostscreen

data class NewPostScreenState(
    val title: String = "",
    val content: String = "",
    val goBackToFeedEvent: Boolean = false
)
