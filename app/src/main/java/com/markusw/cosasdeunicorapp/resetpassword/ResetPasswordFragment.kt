package com.markusw.cosasdeunicorapp.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.markusw.cosasdeunicorapp.core.ext.showDialog
import com.markusw.cosasdeunicorapp.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ResetPasswordViewModel>()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.emailField.error = state.emailError
                binding.loadingLayout.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.resetPasswordEvents.collect { event ->
                when (event) {
                    is ResetPasswordEvents.EmailSentError -> {
                        showDialog(
                            title = "Error al enviar el correo",
                            message = event.reason,
                            positiveButtonText = "Reintentar",
                            onPositiveButtonClick = { viewModel.onContinue() },
                            neutralButtonText = "Cancelar",
                        )
                    }
                    is ResetPasswordEvents.EmailSentSuccessfully -> {
                        showDialog(
                            title = "Correo enviado exitosamente",
                            message = "Se ha enviado un correo a ${viewModel.uiState.value.email} con las instrucciones para restablecer la contraseña",
                            positiveButtonText = "Aceptar",
                            onPositiveButtonClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    private fun setupEvents() {
        binding.topAppBar.setNavigationOnClickListener { navController.popBackStack() }
        binding.emailField.addTextChangedListener { viewModel.onEmailChanged(it.toString()) }
        binding.continueButton.setOnClickListener { viewModel.onContinue() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}