package com.example.hiretop.ui.screens.entreprise.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiretop.R
import com.example.hiretop.ui.extras.FailurePopup

@Composable
fun EditEnterpriseCultureAndValuesSection(
    currentCultureAndValues: String?,
    onSaveClicked: (String) -> Unit
) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val maxLength = 1500

    var requiredCultureAndValues by remember { mutableStateOf(currentCultureAndValues ?: "") }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null
        })
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(25.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 5.dp))

        Text(
            text = stringResource(R.string.indicates_required_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(height = 20.dp))

        OutlinedTextField(
            value = requiredCultureAndValues,
            onValueChange = { if (it.length <= maxLength) requiredCultureAndValues = it },
            label = {
                Text(
                    text = stringResource(R.string.required_enterprise_culture_values_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingText = {
                Text(
                    text = "${requiredCultureAndValues.length} / $maxLength",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            singleLine = false,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = mWidth * 0.4f)
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        Button(
            modifier = Modifier
                .width(width = mWidth * 0.7F)
                .height(50.dp)
                .padding(horizontal = 15.dp)
                .align(alignment = Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small,
            onClick = {
                if (requiredCultureAndValues.isEmpty()) {
                    onErrorMessage = mContext.getString(R.string.empty_cultures_and_values_text)
                } else {
                    onSaveClicked(requiredCultureAndValues)
                }
            }
        ) {
            Text(
                text = stringResource(R.string.save_button_text),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(height = 45.dp))
    }
}