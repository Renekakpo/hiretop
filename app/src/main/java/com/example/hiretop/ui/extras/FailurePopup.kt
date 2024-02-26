package com.example.hiretop.ui.extras

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hiretop.R

@Composable
fun FailurePopup(
    title: String = stringResource(R.string.erreur_text),
    errorMessage: String,
    onDismiss: (() -> Unit)? = null
) {
    var isOpen by remember { mutableStateOf(true) }

    if (isOpen) {
        AlertDialog(
            onDismissRequest = {
                isOpen = false
                onDismiss?.invoke()
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                Button(
                    onClick = {
                        isOpen = false
                        onDismiss?.invoke()
                    }
                ) {
                    Text(text = stringResource(R.string.ok_text))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            shape = RoundedCornerShape(8.dp)
        )
    }
}