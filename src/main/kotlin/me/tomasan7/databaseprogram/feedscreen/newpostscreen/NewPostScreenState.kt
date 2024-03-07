package me.tomasan7.databaseprogram.feedscreen.newpostscreen

data class NewPostScreenState(
    val isEditing: Boolean = false,
    val title: String = "",
    val content: String = "",
    val goBackToFeedEvent: Boolean = false
)
