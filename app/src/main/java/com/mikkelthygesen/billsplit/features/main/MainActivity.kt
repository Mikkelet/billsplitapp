package com.mikkelthygesen.billsplit.features.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.mikkelthygesen.billsplit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)

        viewModel.authProvider.userLiveData.observe(this) {
            println("user state change! ")
            if (it == null)
                navController.navigate(R.id.landingFragment)
            else navController.navigate(R.id.groupsFragment)
        }
    }
}
