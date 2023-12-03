package com.markusw.cosasdeunicorapp.auth.presentation.register

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
import com.markusw.cosasdeunicorapp.core.ext.showDialog
import com.markusw.cosasdeunicorapp.core.ext.toast
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    private val navController by lazy { findNavController() }
    private val ctx by lazy { requireContext() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setupObservers()
    }

    private fun setupEvents() {
        binding.topAppBar.setNavigationOnClickListener { navController.popBackStack() }
        binding.registerButton.setOnClickListener { viewModel.onRegister() }
        binding.nameField.addTextChangedListener { viewModel.onNameChanged(it.toString()) }
        binding.emailField.addTextChangedListener { viewModel.onEmailChanged(it.toString()) }
        binding.passwordField.addTextChangedListener { viewModel.onPasswordChanged(it.toString()) }
        binding.repeatPasswordField.addTextChangedListener { viewModel.onRepeatedPasswordChanged(it.toString()) }
        binding.termsCheckbox.setOnCheckedChangeListener { _, isChecked -> viewModel.onTermsChecked(isChecked) }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.nameField.error = state.nameError?.asString(ctx)
                binding.emailField.error = state.emailError?.asString(ctx)
                binding.passwordFieldLayout.helperText = state.passwordError?.asString(ctx)
                binding.repeatPasswordFieldLayout.helperText = state.repeatedPasswordError?.asString(ctx)
                binding.loadingLayout.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                binding.registerButton.isEnabled = !state.isLoading
            }
        }

        lifecycleScope.launch {
            viewModel.registrationEvents.collect { registrationEvent ->
                when (registrationEvent) {
                    is RegistrationEvent.RegistrationFailed -> {
                        showDialog(
                            message = registrationEvent.reason,
                            title = UiText.StringResource(R.string.error),
                            positiveButtonText = UiText.StringResource(R.string.retry),
                            onPositiveButtonClick = { viewModel.onRegister() },
                            neutralButtonText = UiText.StringResource(R.string.cancel)
                        )
                    }
                    is RegistrationEvent.SuccessfullyRegistration -> {
                        showDialog(
                            title = UiText.StringResource(R.string.auth_success),
                            message = UiText.StringResource(R.string.succesfully_registered),
                            positiveButtonText = UiText.StringResource(R.string.understood)
                        )
                        navController.popBackStack()
                    }
                    is RegistrationEvent.TermsNotAccepted -> {
                        showDialog(
                            title = UiText.StringResource(R.string.important),
                            message = registrationEvent.message,
                            positiveButtonText = UiText.StringResource(R.string.understood)
                        )
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