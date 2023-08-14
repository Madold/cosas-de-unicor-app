package com.markusw.cosasdeunicorapp.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
    }

    private fun setupEvents() {
        binding.topAppBar.setNavigationOnClickListener { navController.popBackStack() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}