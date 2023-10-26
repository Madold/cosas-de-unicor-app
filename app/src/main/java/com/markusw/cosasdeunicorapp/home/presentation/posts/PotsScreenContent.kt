@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.presentation.posts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.home.domain.model.Post
import com.markusw.cosasdeunicorapp.home.presentation.posts.composables.PostCard

@Composable
fun NewsScreenContent() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Noticias")
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp)
            ) {
                PostCard(
                    post = Post(
                        title = "La universidad de córdoba, ubicada entre las 5 mejores del país",
                        body = "La universidad de Córdoba se encuentra entre las 5 mejores universidades del país, según el ranking de universidades de Colombia 2021. Esto se debe a la gran calidad de sus estudiantes y profesores, además de la gran infraestructura que posee la universidad.",
                        imageURL = "https://cloudfront-us-east-1.images.arcpublishing.com/prisaradioco/KQDBYECJKNPVBMJJY2IERUVTOI.jpg",
                        timestamp = 0
                    )
                )
            }
        }
    )

}