package com.mikkelthygesen.billsplit.features.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)

        lifecycleScope.launch {
            listenToAuth()
        }

        lifecycleScope.launch {
            viewModel.uiEventsState.collect { event ->
                if (event is BaseViewModel.UiEvent.OnBackPressed)
                    onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun listenToAuth() {
        viewModel.authProvider.userLiveData.observe(this) {
            if (it == null) {
                if (!isLandingDestination())
                    navController.navigate(R.id.action_global_landingFragment)
            } else {
                if (isLandingDestination())
                    navController.popBackStack()
            }
        }
    }

    private fun isLandingDestination() =
        navController.currentDestination?.id == R.id.landingFragment

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.groupsFragment -> finish()
            else -> onBackPressedDispatcher.onBackPressed()
        }
    }
}
