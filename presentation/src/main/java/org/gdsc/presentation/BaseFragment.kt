package org.gdsc.presentation

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import org.gdsc.presentation.utils.showMediaPermissionsDialog

abstract class BaseFragment: Fragment() {

    open fun grantedPermissions() { }

    protected val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if(isGranted.all{ it.value }) {
            grantedPermissions()
        }else{
            this.showMediaPermissionsDialog()
        }
    }
}