package com.markusw.cosasdeunicorapp.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.markusw.cosasdeunicorapp.core.ext.showDialog
import com.markusw.cosasdeunicorapp.core.ext.toast
import com.markusw.cosasdeunicorapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    private val navController by lazy { findNavController() }

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
                binding.nameField.error = state.nameError
                binding.emailField.error = state.emailError
                binding.passwordFieldLayout.helperText = state.passwordError
                binding.repeatPasswordFieldLayout.helperText = state.repeatedPasswordError
                state.termsError?.let {
                    showDialog(
                        message = it,
                        title = "Importante",
                        positiveButtonText = "Aceptar"
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.registrationEvents.collect { registrationEvent ->
                when (registrationEvent) {
                    is RegistrationEvent.RegistrationFailed -> {
                        showDialog(
                            message = registrationEvent.reason,
                            title = "Error",
                            positiveButtonText = "Reintentar",
                            onPositiveButtonClick = { viewModel.onRegister() }
                        )
                    }
                    is RegistrationEvent.SuccessfullyRegistration -> {
                        toast("Registro exitoso")
                        navController.popBackStack()
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