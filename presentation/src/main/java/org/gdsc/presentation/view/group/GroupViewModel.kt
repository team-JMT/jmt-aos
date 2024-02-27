package org.gdsc.presentation.view.group

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gdsc.domain.usecase.token.GetAccessTokenUseCase
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val tokenUseCase: GetAccessTokenUseCase
): ViewModel() {


}