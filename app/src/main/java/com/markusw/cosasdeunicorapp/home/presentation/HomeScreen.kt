package com.markusw.cosasdeunicorapp.home.presentation

import android.Manifest
import android.app.Activity
import android.content.ContextWrapper
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.core.ext.openAppSettings
import com.markusw.cosasdeunicorapp.core.presentation.PermissionDialog
import com.markusw.cosasdeunicorapp.core.presentation.ReadExternalStoragePermission
import com.markusw.cosasdeunicorapp.core.presentation.WriteExternalStoragePermission
import com.markusw.cosasdeunicorapp.home.presentation.chat.ChatScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.composables.BottomNavigationBar
import com.markusw.cosasdeunicorapp.home.presentation.docs.DocsScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.main.HomeScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.posts.NewsScreenContent
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

/**
 * Permissions to request when the user tries to download a document.
 */
val permissionsToRequest = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

@Composable
fun HomeScreen() {

    val navController = rememberNavController()
    val viewModel: HomeViewModel = hiltViewModel()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Timber.d("Permission granted")
            } else {
                Timber.d("Permission denied")
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        content = {
            HomeHost(
                navController = navController,
                contentPadding = it,
                viewModel = viewModel
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        }
    )
}

@Composable
private fun HomeHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    contentPadding: PaddingValues,
    viewModel: HomeViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        navController = navController,
        startDestination = HomeScreens.Home.route
    ) {
        composable(route = HomeScreens.Home.route) {
            HomeScreenContent(
                onEvent = viewModel::onEvent
            )
        }

        composable(route = HomeScreens.Chat.route) {

            val scrollState = rememberLazyListState()

            LaunchedEffect(key1 = viewModel.chatListEvents) {
                viewModel.chatListEvents.collectLatest { chatListEvent ->
                    when (chatListEvent) {
                        is ChatListEvent.MessageAdded -> {
                            Timber.d("Message added scrolling to bottom")
                            scrollState.animateScrollToItem(0)
                        }
                    }
                }
            }

            ChatScreenContent(
                state = uiState,
                onEvent = viewModel::onEvent,
                scrollState = scrollState
            )
        }

        composable(route = HomeScreens.News.route) {
            NewsScreenContent()
        }

        composable(route = HomeScreens.Documents.route) {
            val context = LocalContext.current

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    permissionsToRequest.forEach { permission ->
                        viewModel.onPermissionResult(
                            permission = permission,
                            isGranted = permissions[permission] == true
                        )
                    }
                }
            )

            DocsScreenContent(
                onEvent = viewModel::onEvent,
                multiplePermissionLauncher = permissionLauncher,
                state = uiState
            )

            viewModel.visiblePermissionDialogQueue
                .asReversed()
                .forEach { permission ->
                    PermissionDialog(
                        permissionTextProvider = when (permission) {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE -> WriteExternalStoragePermission()
                            Manifest.permission.READ_EXTERNAL_STORAGE -> ReadExternalStoragePermission()
                            else -> return@forEach
                        },
                        isPermanentlyDeclined = !((context as ContextWrapper).baseContext as Activity).shouldShowRequestPermissionRationale(permission).not(),
                        onDismiss = viewModel::dismissDialog,
                        onDone = {
                            viewModel.dismissDialog()
                            permissionLauncher.launch(arrayOf(permission))
                        },
                        onGotToSettings = {
                            viewModel.dismissDialog()
                            context.openAppSettings()
                        })
                }

        }

        composable(route = HomeScreens.More.route) {
            Text(text = "Coming soon...")
        }
    }
}

