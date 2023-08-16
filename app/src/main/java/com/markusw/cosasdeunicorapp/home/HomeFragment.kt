package com.markusw.cosasdeunicorapp.home

import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.databinding.FragmentHomeBinding
import com.markusw.cosasdeunicorapp.home.composables.BottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private val navController by lazy { findNavController() }
    private val bottomBarNavController by lazy { binding.homeFragmentHost.findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setupObservers()
        setupComponents()
    }

    private fun setupComponents() {
        binding.bottomNavigationComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                BottomNavigationBar(
                    onScreenClicked = { screen ->
                        //TODO: Setup nested navigation graph
                        when (screen) {
                            is HomeBottomBarScreen.Chat -> {
                                navigateTo(R.id.homeChatFragment)
                            }
                            is HomeBottomBarScreen.Documents -> {

                            }
                            is HomeBottomBarScreen.Home -> {
                                navigateTo(R.id.homeMainFragment)
                            }
                            is HomeBottomBarScreen.More -> {

                            }
                            is HomeBottomBarScreen.News -> {

                            }
                        }
                    }
                )
            }
        }
    }

    private fun setupEvents() {
        //binding.closeSessionButton.setOnClickListener { viewModel.onCloseSession() }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.homeEvents.collect { homeEvent ->
                when (homeEvent) {
                    HomeEvents.LogoutSuccessful -> {
                        navController.navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                }
            }
        }
    }

    private fun navigateTo(@IdRes destination: Int) {
        if (bottomBarNavController.currentDestination?.id != destination) {
            bottomBarNavController.navigate(destination)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}