package com.markusw.cosasdeunicorapp.core.domain

import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.News
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.teacher_rating.data.model.ReviewDto
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
    /**
     * Loads the previous messages from the database
     * @return a list of messages
     */
    suspend fun loadPreviousMessages(): List<Message>

    /**
     * Sends a message to the global chat
     * @param message the message to send
     * @return a Result object
     */
    suspend fun sendMessageToGlobalChat(message: Message): Result<Unit>

    /**
     * Saves the user in the database
     * @param user the user to save
     * @return a Result object
     * @see User
     * @see Result
     */
    suspend fun saveUserInDatabase(user: User): Result<Unit>

    /**
     * Returns a flow of messages that are sent to the global chat
     * @return a flow of messages
     * @see Message
     * @see Flow
     */
    suspend fun onNewMessage(): Flow<Message>

    /**
     * Loads the previous news from the database
     * @return a list of news
     */
    suspend fun loadPreviousNews(): List<News>

    suspend fun onNewNews(): Flow<News>

    /**
     * Removes a user from the likedBy list of a news
     * @param newsId the id of the news
     * @param user the user to remove
     * @return a Result object
     * @see Result
     */
    suspend fun removeUserFromLikedByList(newsId: String, userId: String): Result<Unit>

    /**
     * Adds a user to the likedBy list of a news
     * @param newsId the id of the news
     * @param user the user to add
     * @return a Result object
     * @see Result
     */
    suspend fun addUserToLikedByList(newsId: String, userId: String): Result<Unit>

    /**
     * Returns the number of users in the database
     * @return the number of users
     */
    suspend fun getUsersCount(): Result<Int>

    suspend fun updateUserInfo(id: String, data: ProfileUpdateData): Result<Unit>

    suspend fun onUserInfoUpdate(): Flow<User>

    suspend fun getUser(id: String): User

    /**
     * Returns a the flow of academic programs from the database in real time
     * @return a flow of academic programs
     * @see AcademicProgram
     */
    fun getAcademicPrograms(): Flow<List<AcademicProgram>>

    fun getTeachers(): Flow<List<TeacherReview>>

    suspend fun saveReview(review: ReviewDto, teacherId: String)

    suspend fun deleteReview(review: Review, teacherId: String)

}