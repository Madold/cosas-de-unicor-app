package com.markusw.cosasdeunicorapp.core.data

import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.MessageContent
import com.markusw.cosasdeunicorapp.home.domain.model.News
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeRemoteDatabase: RemoteDatabase {


    private val messages = MutableStateFlow(
        listOf(
            Message(
                sender = User(
                    displayName = "Markus Water",
                    email = "marcopor@gmail.com",
                    photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocJ6cyg00GmGpEEDz-WvBspnSwQwrUg5UJ1nnIV7mlb6p6E=s96-c",
                    uid = "4V1p1O0e5YMYkDxar4wB1ibrjC63"
                ),
                content = MessageContent(
                    text = "Hello, I'm Markus"
                ),
                timestamp = randomTimestamp()
            ),
            Message(
                sender = User(
                    displayName = "Fire in the hole",
                    email = "finthehole@gmail.com",
                    photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.myinstants.com%2Fen%2Finstant%2Ffire-in-the-hole-geometry-dash-52280%2F&psig=AOvVaw1tZF0PE4QsrRjPrpC4wET_&ust=1707237426783000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCIivoJTRlIQDFQAAAAAdAAAAABAQ",
                    uid = "vxcvsd8fqrmdsfi43"
                ),
                content = MessageContent(
                    text = "Fire in the hole"
                ),
                timestamp = randomTimestamp()
            )
        )
    )

    private val messagesToSend = listOf(
        Message(
            sender = User(
                displayName = "Markus Water",
                email = "marcopor@gmail.com",
                photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocJ6cyg00GmGpEEDz-WvBspnSwQwrUg5UJ1nnIV7mlb6p6E=s96-c",
                uid = "4V1p1O0e5YMYkDxar4wB1ibrjC63"
            ),
            content = MessageContent(
                text = "Hi bro"
            ),
            timestamp = randomTimestamp()
        ),
        Message(
            sender = User(
                displayName = "Fire in the hole",
                email = "finthehole@gmail.com",
                photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.myinstants.com%2Fen%2Finstant%2Ffire-in-the-hole-geometry-dash-52280%2F&psig=AOvVaw1tZF0PE4QsrRjPrpC4wET_&ust=1707237426783000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCIivoJTRlIQDFQAAAAAdAAAAABAQ",
                uid = "vxcvsd8fqrmdsfi43"
            ),
            content = MessageContent(
                text = "Fire in the hole"
            ),
            timestamp = randomTimestamp()
        ),
        Message(
            sender = User(
                displayName = "Fire in the hole",
                email = "finthehole@gmail.com",
                photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.myinstants.com%2Fen%2Finstant%2Ffire-in-the-hole-geometry-dash-52280%2F&psig=AOvVaw1tZF0PE4QsrRjPrpC4wET_&ust=1707237426783000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCIivoJTRlIQDFQAAAAAdAAAAABAQ",
                uid = "vxcvsd8fqrmdsfi43"
            ),
            content = MessageContent(
                text = "Fire in the hole"
            ),
            timestamp = randomTimestamp()
        ),
        Message(
            sender = User(
                displayName = "Fire in the hole",
                email = "finthehole@gmail.com",
                photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.myinstants.com%2Fen%2Finstant%2Ffire-in-the-hole-geometry-dash-52280%2F&psig=AOvVaw1tZF0PE4QsrRjPrpC4wET_&ust=1707237426783000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCIivoJTRlIQDFQAAAAAdAAAAABAQ",
                uid = "vxcvsd8fqrmdsfi43"
            ),
            content = MessageContent(
                text = "Fire in the hole"
            ),
            timestamp = randomTimestamp()
        ),
        Message(
            sender = User(
                displayName = "Markus Water",
                email = "marcopor@gmail.com",
                photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocJ6cyg00GmGpEEDz-WvBspnSwQwrUg5UJ1nnIV7mlb6p6E=s96-c",
                uid = "4V1p1O0e5YMYkDxar4wB1ibrjC63"
            ),
            content = MessageContent(
                text = "WTF"
            ),
            timestamp = randomTimestamp()
        ),
        Message(
            sender = User(
                displayName = "Fire in the hole",
                email = "finthehole@gmail.com",
                photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.myinstants.com%2Fen%2Finstant%2Ffire-in-the-hole-geometry-dash-52280%2F&psig=AOvVaw1tZF0PE4QsrRjPrpC4wET_&ust=1707237426783000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCIivoJTRlIQDFQAAAAAdAAAAABAQ",
                uid = "vxcvsd8fqrmdsfi43"
            ),
            content = MessageContent(
                text = "Fire in the hole"
            ),
            timestamp = randomTimestamp()
        ),
        Message(
            sender = User(
                displayName = "Fire in the hole",
                email = "finthehole@gmail.com",
                photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.myinstants.com%2Fen%2Finstant%2Ffire-in-the-hole-geometry-dash-52280%2F&psig=AOvVaw1tZF0PE4QsrRjPrpC4wET_&ust=1707237426783000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCIivoJTRlIQDFQAAAAAdAAAAABAQ",
                uid = "vxcvsd8fqrmdsfi43"
            ),
            content = MessageContent(
                text = "Fire in the hole"
            ),
            timestamp = randomTimestamp()
        ),
        Message(
            sender = User(
                displayName = "Markus Water",
                email = "marcopor@gmail.com",
                photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocJ6cyg00GmGpEEDz-WvBspnSwQwrUg5UJ1nnIV7mlb6p6E=s96-c",
                uid = "4V1p1O0e5YMYkDxar4wB1ibrjC63"
            ),
            content = MessageContent(
                text = "This dude is crazy"
            ),
            timestamp = randomTimestamp()
        ),

    )

    private fun randomTimestamp(): Long = System.currentTimeMillis() * (0..234).random() - (1..679).random()

    override suspend fun loadPreviousMessages(): List<Message> {
        return messages.value
    }

    override suspend fun sendMessageToGlobalChat(message: Message): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun saveUserInDatabase(user: User): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun onNewMessage(): Flow<Message> {
        return flow {
            messagesToSend.forEach {
                emit(it)
                delay((6000..7000).random().toLong())
            }
        }
    }

    override suspend fun loadPreviousNews(): List<News> {
        return emptyList()
    }

    override suspend fun onNewNews(): Flow<News> {
        return flow {  }
    }

    override suspend fun removeUserFromLikedByList(newsId: String, user: User): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun addUserToLikedByList(newsId: String, user: User): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun getUsersCount(): Result<Int> {
        return Result.Success(34)
    }

    override suspend fun updateUserInfo(id: String, data: ProfileUpdateData): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun onUserInfoUpdate(): Flow<User> {
        return flow {  }
    }

    override suspend fun getUser(id: String): User {
        return User()
    }
}