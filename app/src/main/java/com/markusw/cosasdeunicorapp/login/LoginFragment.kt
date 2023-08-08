package com.markusw.cosasdeunicorapp.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.markusw.cosasdeunicorapp.R
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
        binding.passwordField.addTextChangedListener { viewModel.onEmailChanged(it.toString()) }
        binding.loginButton.setOnClickListener { viewModel.onLogin() }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.authenticationEvents.collectLatest { authEvent ->
                when (authEvent) {
                    is  AuthenticationEvent.AuthFailed -> {
                        Timber.e(authEvent.reason)
                    }

                    is AuthenticationEvent.AuthSuccessful -> {
                        Timber.d("Auth succesfull")
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