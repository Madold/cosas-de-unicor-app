package com.markusw.cosasdeunicorapp.home.presentation.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.home.presentation.more.composables.FeatureCard
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

@Composable
fun MoreScreenContent(
    mainNavController: NavController
) {

    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Text(
                        text = "Más",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(all = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(FeatureDestination.values()) { feature ->
                    FeatureCard(
                        label = {
                            Text(
                                text = feature.label,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = feature.iconSrc),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(0.5f)
                            )
                        },
                        modifier = Modifier.aspectRatio(1f),
                        onClick = {
                            mainNavController.navigate(feature.route)
                        }
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoreScreenPreview() {
    CosasDeUnicorAppTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        MoreScreenContent(mainNavController = rememberNavController())
    }
}