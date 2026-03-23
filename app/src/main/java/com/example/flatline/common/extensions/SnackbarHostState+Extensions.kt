package com.example.flatline.common.extensions

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.example.flatline.features.listings.Snack
import kotlinx.coroutines.flow.SharedFlow

suspend fun SnackbarHostState.observeSnacks(sourceFlow: SharedFlow<Snack>) {
    sourceFlow.collect {
        if (it is Snack.Error) showSnackbar(it)
    }
//        when (it) {
//            is Snack.Info,
//            is Snack.Error -> showSnackbar(it)
//            is Snack.InfoWithAction -> {
//                when (showSnackbar(it)) {
//                    SnackbarResult.ActionPerformed -> it.performAction()
//                    SnackbarResult.Dismissed -> it.onDismiss()
//                }
//            }
//        }
//    }
}