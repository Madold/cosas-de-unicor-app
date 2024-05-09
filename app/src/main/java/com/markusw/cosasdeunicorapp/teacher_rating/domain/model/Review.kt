package com.markusw.cosasdeunicorapp.teacher_rating.domain.model

import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.teacher_rating.data.model.ReviewDto

data class Review(
    val content: String = "",
    val vote: TeacherRating = TeacherRating.Supportive,
    val author: User = User(),
    val likes: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val timestamp: Long = 0L
)

fun Review.toRemoteDatabaseModel() = ReviewDto(
    content = content,
    vote = vote,
    authorId = author.uid,
    likes = likes,
    dislikes = dislikes,
    timestamp = timestamp
)
