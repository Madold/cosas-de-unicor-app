package com.markusw.cosasdeunicorapp.auth.presentation.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.showDialog
import com.markusw.cosasdeunicorapp.core.ext.toast
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    private val navController by lazy { findNavController() }
    private val ctx by lazy { requireContext() }
    private val googleAuthClient by lazy {
        GoogleAuthUIClient(
            context = requireContext(),
            oneTapClient = Identity.getSignInClient(requireContext())
        )
    }
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val googleCredentials = googleAuthClient.getGoogleCredentialsFromIntent(result.data)
            viewModel.onGoogleSignInResult(googleCredentials)
            return@registerForActivityResult
        }
        viewModel.onGoogleSignInCanceled()
    }

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
        binding.googleButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.onGoogleSignInStarted()
                when (val signInResult  = googleAuthClient.signIn()) {
                    is Result.Error -> {
                        showDialog(
                            message = signInResult.message!!,
                            positiveButtonText = UiText.StringResource(R.string.accept)
                        )
                        viewModel.onGoogleSignInFinished()
                    }

                    is Result.Success -> {
                        signInResult.data?.let { intentSender ->
                            googleSignInLauncher.launch(
                                IntentSenderRequest.Builder(
                                    intentSender
                                ).build()
                            )
                        }
                    }
                }
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