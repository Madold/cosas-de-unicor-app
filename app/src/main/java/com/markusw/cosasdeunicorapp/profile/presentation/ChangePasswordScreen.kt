package com.markusw.cosasdeunicorapp.profile.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.presentation.DoneAnimation
import com.markusw.cosasdeunicorapp.core.presentation.ErrorAnimation
import com.markusw.cosasdeunicorapp.core.presentation.LoadingAnimation
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

@Composable
fun ChangePasswordScreen(
    mainNavController: NavController,
    state: ProfileState,
    onEvent: (ProfileScreenEvent) -> Unit,
) {

    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Text(
                        text = "Cambiar contraseña",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.surface
                    )

                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onEvent(ProfileScreenEvent.SendPasswordResetByEmail)
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(text = "Enviar correo")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.secure_login_ilustration),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Text(
                text = "Al correo por el cual creaste la cuenta, te llegará un email con instrucciones para cambiar la contraseña de tu cuenta.",
                textAlign = TextAlign.Center
            )
        }

        if (state.passwordResetState != AsyncOperationState.IDLE) {
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
                            targetState = state.passwordResetState,
                            label = "",
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            }
                        ) {
                            when (it) {
                                AsyncOperationState.LOADING -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LoadingAnimation(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.4f)
                                        )
                                    }
                                }

                                AsyncOperationState.SUCCESS -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        DoneAnimation()
                                        Text(text = "Correo enviado exitosamente")
                                    }
                                }

                                AsyncOperationState.ERROR -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Error al enviar el correo",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        ErrorAnimation()
                                        Text(
                                            text = state.passwordUpdateError?.asString() ?: "",
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


@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    CosasDeUnicorAppTheme {
        ChangePasswordScreen(
            mainNavController = rememberNavController(),
            state = ProfileState(
                passwordResetState = AsyncOperationState.LOADING
            ),
            onEvent = {}
        )
    }
}