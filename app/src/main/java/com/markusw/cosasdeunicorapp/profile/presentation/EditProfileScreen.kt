package com.markusw.cosasdeunicorapp.profile.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.presentation.Button
import com.markusw.cosasdeunicorapp.core.presentation.DoneAnimation
import com.markusw.cosasdeunicorapp.core.presentation.ErrorAnimation
import timber.log.Timber

@Composable
fun EditProfileScreen(
    mainNavController: NavController,
    state: ProfileState,
    onEvent: (ProfileScreenEvent) -> Unit,
) {

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imageUri ->
            imageUri ?: return@rememberLauncherForActivityResult
            onEvent(ProfileScreenEvent.ChangeProfilePhoto(imageUri.toString()))
        }
    )
    val areChanges = remember(state.name, state.email, state.profilePhoto) {
        state.name != state.user.displayName ||
                state.email != state.user.email ||
                state.profilePhoto != null
    }

    Timber.d("areChanges: $areChanges")

    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Text(
                        text = "Editar perfil",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    enabled = areChanges,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    onClick = {
                        onEvent(ProfileScreenEvent.SaveChanges)
                    }
                ) {
                    Text(text = "Guardar cambios")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.profilePhoto == null) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_upload),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )

                    }
                } else {
                    AsyncImage(
                        model = Uri.parse(state.profilePhoto),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillWidth
                    )
                }
                TextButton(onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(text = "Cambiar foto +")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(ProfileScreenEvent.ChangeName(it))
                    },
                    isError = state.nameError != null,
                    label = {
                        Text(text = "Nombre")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                state.nameError?.let { error ->
                    Text(
                        text = error.asString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = {
                        onEvent(ProfileScreenEvent.ChangeEmail(it))
                    },
                    isError = state.emailError != null,
                    label = {
                        Text(text = "Correo")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                state.emailError?.let { error ->
                    Text(
                        text = error.asString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

        }

        if (state.profileSaveState != AsyncOperationState.IDLE) {
            Dialog(onDismissRequest = {}) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AnimatedContent(
                            targetState = state.profileSaveState,
                            label = "",
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            }
                        ) {
                            when (it) {
                                AsyncOperationState.LOADING -> {
                                    CircularProgressIndicator()
                                }

                                AsyncOperationState.SUCCESS -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        DoneAnimation()
                                        Text(text = "Cambios guardados exitosamente")
                                    }
                                }

                                AsyncOperationState.ERROR -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Error al guardar los cambios",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        ErrorAnimation()
                                        Text(
                                            text = state.profileUpdateError?.asString() ?: "",
                                            textAlign = TextAlign.Center
                                        )
                                        Button(onClick = { onEvent(ProfileScreenEvent.DismissProfileUpdatedDialog) }) {
                                            Text(text = "Aceptar")
                                        }
                                    }
                                }

                                else -> return@AnimatedContent
                            }
                        }
                    }
                }
            }
        }
    }
}