package com.example.dirverapp.other

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

typealias OnPositiveClick = () -> Unit

internal inline fun <reified T> Activity.navigateTo(clearTask: Boolean = false, noinline intentExtras: ((Intent) -> Unit)? = null) {
    val intent = Intent(this, T::class.java)

    intentExtras?.run {
        intentExtras(intent)
    }

    if (clearTask) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    startActivity(intent)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}

internal fun Activity.showErrorDialog(
    title: String,
    message: String,
    shouldGoBack: Boolean = false,
    positiveText: String = "Retry",
    onPositiveClick: OnPositiveClick,
) {
    MaterialAlertDialogBuilder(this).apply {
        setCancelable(false)
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveText) { dialog, _ ->
            dialog.dismiss()
            onPositiveClick.invoke()
        }
        setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            if (shouldGoBack) {
                onBackPressed()
            }
        }
        show()
    }
}

fun View.showRetrySnackBar(message: String, action: ((View) -> Unit)?) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)

    snackbar.apply {
        this.setBackgroundTint(ContextCompat.getColor(this.context, android.R.color.holo_red_light))

        val colorWhite = ContextCompat.getColor(this.context, android.R.color.white)
        this.setTextColor(colorWhite)
        this.setActionTextColor(colorWhite)
        setAction("RETRY") {
            dismiss()
            action?.invoke(this@showRetrySnackBar)
        }
        show()
    }
}
