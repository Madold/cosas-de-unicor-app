package com.markusw.cosasdeunicorapp.auth.presentation.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.LocalDataStore
import com.markusw.cosasdeunicorapp.core.ext.showDialog
import com.markusw.cosasdeunicorapp.core.ext.toast
import com.markusw.cosasdeunicorapp.core.presentation.GoogleAuthClient
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.databinding.FragmentLoginBinding
import com.markusw.cosasdeunicorapp.home.presentation.HomeViewModel
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    private val navController by lazy { findNavController() }
    private val ctx by lazy { requireContext() }
    private val googleAuthClient by lazy { GoogleAuthClient(ctx) }
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).result
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            viewModel.onGoogleSignInResult(credential)
            return@registerForActivityResult
        }

        viewModel.onGoogleSignInFinished()
    }

    @Inject
    lateinit var localDataStore: LocalDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.auth.currentUser?.let {
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setupObservers()
    }

    private fun setupEvents() {
        binding.registerText.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.emailField.addTextChangedListener { viewModel.onEmailChanged(it.toString()) }
        binding.passwordField.addTextChangedListener { viewModel.onPasswordChanged(it.toString()) }
        binding.loginButton.setOnClickListener { viewModel.onLogin() }
        binding.forgotPasswordText.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
        binding.googleButton.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                var isDarkModeEnabled by remember(localDataStore) { mutableStateOf(false) }
                LaunchedEffect(key1 = localDataStore) {
                    localDataStore
                        .get(HomeViewModel.DARK_MODE_KEY)
                        .collectLatest { isDarkModeEnabled = it.toBoolean() }
                }

                CosasDeUnicorAppTheme(
                    darkTheme = isDarkModeEnabled
                ) {
                    OutlinedButton(
                        onClick = ::startGoogleSignIn,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(id = R.string.google))
                    }
                }
            }
        }
    }

    private fun startGoogleSignIn() {
        viewModel.onGoogleSignInStarted()
        when (val result = googleAuthClient.signIn()) {
            is Result.Error -> {
                showDialog(
                    message = result.message!!,
                    positiveButtonText = UiText.StringResource(R.string.accept)
                )
                viewModel.onGoogleSignInFinished()
            }

            is Result.Success -> {
                googleSignInLauncher.launch(result.data)
            }
        }
    }

    private fun setupObservers() {

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.emailField.error = state.emailError?.asString(ctx)
                binding.passwordFieldLayout.helperText = state.passwordError?.asString(ctx)
                binding.loadingLayout.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                binding.loginButton.isEnabled = !state.isLoading
                binding.googleButton.isEnabled = !state.isLoading
            }
        }

        lifecycleScope.launch {
            viewModel.authenticationEvents.collect { authEvent ->
                when (authEvent) {
                    is AuthenticationEvent.AuthFailed -> {
                        showDialog(
                            title = UiText.StringResource(R.string.error),
                            message = authEvent.reason,
                            positiveButtonText = UiText.StringResource(R.string.retry),
                            onPositiveButtonClick = { viewModel.onLogin() },
                            neutralButtonText = UiText.StringResource(R.string.cancel)
                        )
                    }

                    is AuthenticationEvent.AuthSuccessful -> {
                        toast(UiText.StringResource(R.string.auth_success))
                        navController.navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}