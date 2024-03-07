package me.tomasan7.databaseprogram.post

import kotlinx.collections.immutable.ImmutableList

interface PostService
{
    suspend fun createPost(postDto: PostDto): Int
    suspend fun getPostById(id: Int): PostDto?
    suspend fun getAllPostsOrderedByUploadDateDesc(): ImmutableList<PostDto>
    suspend fun getPostsByAuthorIdOrderedByUploadDateDesc(authorId: Int): ImmutableList<PostDto>
    suspend fun updatePost(postDto: PostDto): Boolean
    suspend fun deletePost(id: Int): Boolean
}
