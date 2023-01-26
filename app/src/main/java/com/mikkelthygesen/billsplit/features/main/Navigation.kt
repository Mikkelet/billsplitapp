/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mikkelthygesen.billsplit.features.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mikkelthygesen.billsplit.R
import java.security.InvalidParameterException

enum class Screen { Landing, Profile, Groups, AddGroup, Group }

fun Fragment.navigate(to: Screen, from: Screen, args: Bundle? = null) {
    if (to == from) {
        throw InvalidParameterException("Can't navigate to $to")
    }
    when (to) {
        Screen.Landing -> {
            findNavController().navigate(R.id.action_global_landingFragment)
        }
        Screen.Profile -> {
            findNavController().navigate(R.id.profileFragment)
        }
        Screen.Groups -> {
            findNavController().navigate(R.id.groupsFragment)
        }
        Screen.AddGroup -> {
            findNavController().navigate(R.id.action_global_addGroupFragment)
        }
        Screen.Group -> {
            findNavController().navigate(R.id.action_global_groupFragment, args)
        }
    }
}

fun Fragment.popBackStack() = findNavController().popBackStack()
