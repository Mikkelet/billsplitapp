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
import com.mikkelthygesen.billsplit.features.main.group.GroupFragment
import com.mikkelthygesen.billsplit.features.main.profile.ProfileFragment

fun Fragment.navigateToProfile() =
    findNavController().navigate(R.id.action_global_profileFragment)
fun Fragment.navigateToAddGroup() =
    findNavController().navigate(R.id.action_global_addGroupFragment)

fun Fragment.navigateToGroup(groupId: String) {
    val args = Bundle()
    args.putString(GroupFragment.ARG_GROUP_ID, groupId)
    findNavController().navigate(R.id.action_global_groupFragment, args)
}

fun Fragment.navigateToFriends() {
    when (this) {
        is ProfileFragment -> findNavController().navigate(R.id.action_profileFragment_to_friendsFragment)
        else -> findNavController().navigate(R.id.action_global_friendsFragment)
    }
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}
