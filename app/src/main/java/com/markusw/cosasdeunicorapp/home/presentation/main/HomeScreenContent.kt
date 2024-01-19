@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.presentation.Screens
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.DOCX
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.PDF
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.XLSX
import com.markusw.cosasdeunicorapp.home.domain.model.News
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.ChatBubble
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.ChatItem
import com.markusw.cosasdeunicorapp.home.presentation.docs.DocumentReference
import com.markusw.cosasdeunicorapp.home.presentation.news.composables.NewsCard
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background
import kotlinx.coroutines.launch

@Composable
fun HomeScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    mainNavController: NavController,
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isUserInfoDialogVisible by rememberSaveable { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Text(text = "Drawer content")
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = home_bottom_bar_background
                        ),
                        title = {
                            Text(
                                text = "Inicio",
                                color = MaterialTheme.colorScheme.surface,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { isUserInfoDialogVisible = true }) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(state.currentUser.photoUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    )
                },
                content = { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                    ) {

                        Section(
                            title = {
                                Text(
                                    text = "Documentos populares",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            trailingIcon = {
                                TextButton(onClick = { }) {
                                    Text(text = "Ver todo")
                                }
                            },
                            content = {
                                Row {
                                    DocumentCard(
                                        document = DocumentReference(
                                            name = "Solicitud de doble programa",
                                            documentName = "solicitud_doble_programa.docx"
                                        ),
                                        onClick = {},
                                        modifier = Modifier.weight(1f)
                                    )
                                    DocumentCard(
                                        document = DocumentReference(
                                            name = "Simulador de ingreso a la universidad",
                                            documentName = "simulador_promedio_ponderado_programa.xlsx"
                                        ),
                                        onClick = {},
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        )

                        Section(
                            title = {
                                Text(
                                    text = "¿Qué esta pasando?",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            trailingIcon = {
                                TextButton(onClick = { }) {
                                    Text(text = "Ver todo")
                                }
                            },
                            content = {
                                LazyRow {
                                    items(state.newsList.take(3)) { news ->
                                        NewsCard(
                                            news = news,
                                            onNewsLiked = {  },
                                            modifier = Modifier
                                                .width(290.dp)
                                                .weight(1f)
                                        )
                                    }
                                }
                            })

                        Section(
                            title = {
                                Text(
                                    text = "Mantente comunicado",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            trailingIcon = {
                                TextButton(onClick = { }) {
                                    Text(text = "Ir al chat general")
                                }
                            },
                            content = {

                                if (state.globalChatList.isNotEmpty()) {
                                    val lastMessage = state.globalChatList.first()

                                    ChatItem(message = lastMessage, swipeEnabled = false)
                                }

                            })

                        if (isUserInfoDialogVisible) {
                            UserInfoDialog(
                                state = state,
                                onEvent = onEvent,
                                onDismiss = { isUserInfoDialogVisible = false },
                                mainNavController = mainNavController
                            )
                        }

                    }
                }
            )
        }
    )
}

@Composable
private fun Section(
    title: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            title()
            trailingIcon()
        }
        content()
    }
}

@Composable
private fun DocumentCard(
    document: DocumentReference,
    modifier: Modifier = Modifier,
    onClick: (DocumentReference) -> Unit
) {

    val documentCover = when (TextUtils.getFileExtensionFromName(document.documentName)) {
        DOCX -> painterResource(id = R.drawable.word_icon)
        PDF -> painterResource(id = R.drawable.pdf_icon)
        XLSX -> painterResource(id = R.drawable.excel_icon)
        else -> painterResource(id = R.drawable.file_icon)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = { onClick(document) })
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = documentCover,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = document.name,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun UserInfoDialog(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    onDismiss: () -> Unit,
    mainNavController: NavController,
    modifier: Modifier = Modifier,
) {

    val user = state.currentUser

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sesión iniciada como:",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                )

                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = user.email,
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserInfoDialogAction(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(text = "Perfil")
                    },
                    onClick = {
                        onDismiss()
                        mainNavController.navigate(Screens.Profile.route)
                    }
                )

                Box(
                    modifier = Modifier.padding(top = 20.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = { onEvent(HomeUiEvent.CloseSession) },
                        enabled = !state.isClosingSession
                    ) {
                        AnimatedContent(
                            targetState = state.isClosingSession,
                            label = "",
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            }
                        ) { isClosingSession ->
                            if (isClosingSession) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_exit),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Cerrar sesión")
                    }
                }

            }
        }
    }
}

@Composable
private fun UserInfoDialogAction(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            icon()
            text()
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_variant),
            contentDescription = null
        )
    }
}