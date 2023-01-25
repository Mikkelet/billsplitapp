package com.mikkelthygesen.billsplit.features.main.add_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupFragment : Fragment() {

    private val addGroupViewModel: AddGroupViewModel by viewModels()
    private val group by lazy {  addGroupViewModel.group }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val uiStateFlow = addGroupViewModel.uiStateFlow.collectAsState()
                val uiState = uiStateFlow.value

                BillSplitTheme {
                    Scaffold {
                        val padding = it
                        AddGroupView(
                            uiState = uiState,
                            group = group,
                            onNext = addGroupViewModel::next,
                            onBack = {
                                if (uiState is AddGroupViewModel.AddName)
                                    close()
                                else addGroupViewModel.back()
                            },
                            onClose = ::close,
                            onSubmitGroup = addGroupViewModel::saveGroup,
                            showSubmitLoader = addGroupViewModel.submittingGroup
                        )
                    }
                }
            }
        }
    }

    private fun close() {
        findNavController().popBackStack()
    }
}