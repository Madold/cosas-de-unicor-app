package com.markusw.cosasdeunicorapp.login

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
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    private val navController by lazy { findNavController() }
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
        }
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
                when (val signInResult  = googleAuthClient.signIn()) {
                    is Resource.Error -> {
                        showDialog(
                            message = signInResult.message!!,
                            positiveButtonText = "Aceptar"
                        )
                    }

                    is Resource.Success -> {
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
                binding.emailField.error = state.emailError
                binding.passwordFieldLayout.helperText = state.passwordError
                binding.loadingLayout.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.authenticationEvents.collect { authEvent ->
                when (authEvent) {
                    is AuthenticationEvent.AuthFailed -> {
                        showDialog(
                            title = "Error",
                            message = authEvent.reason,
                            positiveButtonText = "Reintentar",
                            onPositiveButtonClick = { viewModel.onLogin() },
                            neutralButtonText = "Cancelar"
                        )
                    }

                    is AuthenticationEvent.AuthSuccessful -> {
                        toast("Auth successful")
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