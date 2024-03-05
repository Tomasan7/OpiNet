package me.tomasan7.databaseprogram.feedscreen

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FeedScreenState(
    val posts: ImmutableList<Post> = persistentListOf()
)
