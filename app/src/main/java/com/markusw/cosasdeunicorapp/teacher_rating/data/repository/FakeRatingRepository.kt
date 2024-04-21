package com.markusw.cosasdeunicorapp.teacher_rating.data.repository

import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import com.markusw.cosasdeunicorapp.teacher_rating.domain.repository.TeacherRatingRepository

class FakeRatingRepository : TeacherRatingRepository {
    override suspend fun getTeachers(): List<TeacherReview> {
        return listOf(
            TeacherReview(
                teacherName = "Ñengele",
                id = "1",
                reviews = listOf(
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Homie,
                        author = User(
                            uid = "1",
                            displayName = "Markus",
                            email = "m@gmail.com",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Mal profesor, no explica bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Pushy,
                        author = User(
                            uid = "2",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Homie,
                        author = User(
                            uid = "3",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Homie,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),

                    )
            ),
            TeacherReview(
                teacherName = "Andrés Escala",
                id = "2",
                reviews = listOf(
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Ruthless,
                        author = User(
                            uid = "1",
                            displayName = "Markus",
                            email = "m@gmail.com",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Mal profesor, no explica bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Ruthless,
                        author = User(
                            uid = "2",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Homie,
                        author = User(
                            uid = "3",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Supportive,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Supportive,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Ruthless,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),


                    )
            ),
            TeacherReview(
                teacherName = "José Gaspar",
                id = "3",
                reviews = listOf(
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Pushy,
                        author = User(
                            uid = "1",
                            displayName = "Markus",
                            email = "m@gmail.com",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Mal profesor, no explica bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Pushy,
                        author = User(
                            uid = "2",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Pushy,
                        author = User(
                            uid = "3",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Pushy,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Supportive,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Homie,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),
                    Review(
                        content = "Muy buen profesor, explica muy bien",
                        timestamp = 2324344,
                        vote = TeacherRating.Ruthless,
                        author = User(
                            uid = "4",
                            displayName = "Markus",
                            email = "asasasasfsdf",
                            photoUrl = "https://www.google.com"
                        ),
                        likes = emptyList(),
                        dislikes = emptyList()
                    ),


                    )
            )
        )
    }

    override suspend fun saveReview(review: Review, teacherId: String): Result<Unit> {
        return Result.Success(Unit)
    }
}