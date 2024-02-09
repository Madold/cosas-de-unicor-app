@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.maryesateregg.presentation

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.pop
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MaryEasterEggScreen(mainNavController: NavHostController) {
    val fireworks = remember { mutableStateListOf<Firework>() }
    var size: IntSize by remember { mutableStateOf(IntSize.Zero) }
    var isImageVisible by remember { mutableStateOf(false) }
    var isTitleVisible by remember { mutableStateOf(false) }
    var isBodyVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val player = remember {
        MediaPlayer.create(context, R.raw.calm_ambient).apply { isLooping = true }
    }

    fun handleOnBack() {
        player.stop()
        mainNavController.pop()
    }

    LaunchedEffect(key1 = Unit) {
        delay(7000)
        while (true) {
            delay(30)
            fireworks.forEach { it.update() }
        }
    }

    LaunchedEffect(key1 = size) {
        delay(7000)
        while (true) {
            delay(100)
            fireworks.add(Firework(size.width.toFloat(), size.height.toFloat()))
        }
    }

    LaunchedEffect(key1 = Unit) {
        player.start()
        delay(5000)
        isTitleVisible = true
        delay(2300)
        isImageVisible = true
        delay(8000)
        isBodyVisible = true
    }

    BackHandler(onBack = ::handleOnBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = ::handleOnBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Canvas(
                modifier = Modifier
                    .onSizeChanged { size = it }
                    .fillMaxSize()
            ) {
                fireworks.removeIf { it.done() }

                for (firework in fireworks) {
                    firework.show(this)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnimatedVisibility(
                    visible = isTitleVisible,
                    enter = fadeIn(animationSpec = tween(durationMillis = 1200)),
                ) {
                    Text(
                        "Dedicatoria para una personita muy especial",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }

                AnimatedVisibility(
                    visible = isImageVisible,
                    enter = slideInVertically(
                        initialOffsetY = { fullWidth -> fullWidth / 2 },
                        animationSpec = tween(durationMillis = 2000),
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 2000)
                    ),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.someone_special),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxWidth(0.5f),
                        contentScale = ContentScale.FillWidth
                    )
                }

                AnimatedVisibility(
                    visible = isBodyVisible,
                    enter = fadeIn(animationSpec = tween(durationMillis = 1200)),
                ) {
                    Text(
                        "Mereces toda la felicidad y lo bueno de este mundo :)",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}