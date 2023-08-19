@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import kotlinx.coroutines.launch

@Composable
fun HomeScreenContent() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isContextMenuVisible by remember { mutableStateOf(true) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }

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
                content = { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CenterAlignedTopAppBar(
                            title = { Text(text = "Inicio") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Hamburger menu"
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .pointerInput(true) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    isContextMenuVisible = true
                                                    pressOffset = DpOffset(
                                                        x = it.x.toDp(),
                                                        y = it.y.toDp()
                                                    )
                                                },
                                                onPress = {
                                                    isContextMenuVisible = true
                                                    pressOffset = DpOffset(
                                                        x = it.x.toDp(),
                                                        y = it.y.toDp()
                                                    )
                                                }
                                            )
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile"
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    )
}