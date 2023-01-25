package com.mikkelthygesen.billsplit.features.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mikkelthygesen.billsplit.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
            delay(1000)
            listenToAuth()
        }
    }

    private fun listenToAuth() {
        viewModel.authProvider.userLiveData.observe(this) {
            println("qqq user=$it")
            if (it == null && !isLandingDestination()) {
                println("qqq showing landing")
                navController.navigate(R.id.action_global_landingFragment)
            }
        }
    }

    private fun isLandingDestination() =
        navController.currentDestination?.id == R.id.landingFragment

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.landingFragment -> Unit
            else -> onBackPressedDispatcher.onBackPressed()
        }
    }
}
