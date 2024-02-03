package com.markusw.cosasdeunicorapp.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.presentation.ProfileAvatar
import com.markusw.cosasdeunicorapp.core.presentation.Screens

@Composable
fun ProfileScreen(
    mainNavController: NavController,
    state: ProfileState,
    onEvent: (ProfileScreenEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Text(
                        text = "Mi perfil",
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            ProfileAvatar(
                imageUrl = state.user.photoUrl,
                size = 200.dp
            )

            Text(
                text = state.user.displayName,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = state.user.email,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Editar perfil",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                selected = false,
                onClick = { mainNavController.navigate(Screens.EditProfile.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null
                    )
                },
                badge = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_variant),
                        contentDescription = null
                    )
                }
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Cambiar contraseña",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                selected = false,
                onClick = { mainNavController.navigate(Screens.ResetPassword.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null
                    )
                },
                badge = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_variant),
                        contentDescription = null
                    )
                }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            mainNavController = NavController(LocalContext.current),
            state = ProfileState(
                user = User(
                    displayName = "pepe",
                    email = "pepe@gmail.com",
                    photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Funsplash.com%2Fs%2Fphotos%2Frandom-people&psig=AOvVaw0Br22P6J7Uf_1dHbTbae9D&ust=1705520660660000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCOjYxtrV4oMDFQAAAAAdAAAAABAD"
                )
            ),
            onEvent = {},
        )
    }
}