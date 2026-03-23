package com.example.flatline.features.listings

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

sealed class Snack: SnackbarData, SnackbarVisuals {
    override val message: String = ""
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
    open val action: (() -> Unit)? = null

    class Info(
        override val message: String,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
    ): Snack()

    class InfoWithAction(
        override val message: String,
        override val action: () -> Unit,
        override val actionLabel: String,
        val onDismiss: () -> Unit = {},
        override val duration: SnackbarDuration = SnackbarDuration.Short,
    ): Snack()

    class Error(
        override val message: String,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
    ): Snack()

    override val visuals: SnackbarVisuals = this
    override fun performAction() = action?.invoke() ?: Unit

    override fun dismiss() = if (this is InfoWithAction) onDismiss() else Unit
}
