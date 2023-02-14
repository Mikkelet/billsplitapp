package com.mikkelthygesen.billsplit.features.main.add_service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffoldWithAuth
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddServiceFragment : Fragment() {

    private val addServiceViewModel: AddServiceViewModel by viewModels()
    private val groupId by lazy { arguments?.getString(ARG_GROUP_ID) ?: "" }
    private val serviceId by lazy { arguments?.getString(ARG_SERVICE_ID) ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiStateFlow = addServiceViewModel.uiStateFlow.collectAsState()
                addServiceViewModel.loadService(groupId, serviceId)
                BaseScaffoldWithAuth(addServiceViewModel,
                    topBar = {
                        BigTopBar(
                            title = if (uiStateFlow.value is AddServiceViewModel.ServiceLoaded) {
                                if (serviceId.isBlank()) "New service" else addServiceViewModel.service.nameState
                            } else "",
                            leadingContent = {
                                BackButton {
                                    addServiceViewModel.onBackButtonPressed()
                                }
                            },
                            trailingContent = {
                                SimpleIconButton(iconResId = R.drawable.ic_check) {
                                    addServiceViewModel.submitSubscriptionService()
                                }
                            }
                        )
                    }) {
                    Crossfade(targetState = uiStateFlow.value) { uiState ->
                        when (uiState) {
                            is AddServiceViewModel.ServiceLoaded -> {
                                AddServiceView()
                            }
                            is BaseViewModel.UiState.Loading -> {
                                LoadingView()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(addServiceViewModel.uiEventsState) {
            when (it) {
                is AddServiceViewModel.ServiceAdded,
                is BaseViewModel.UiEvent.OnBackPressed -> popBackStack()
            }
        }
    }


    companion object {
        const val ARG_GROUP_ID = "group_id"
        const val ARG_SERVICE_ID = "service_id"
    }
}