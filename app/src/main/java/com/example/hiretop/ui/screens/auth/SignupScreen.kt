package com.example.hiretop.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hiretop.R
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.utils.Utils.isEmailValid
import com.example.hiretop.utils.Utils.isValidPassword
import com.example.hiretop.viewModels.MainViewModel

object SignupScreen : NavDestination {
    override val route: String = "signup_screen"
}

@Composable
fun SignupScreen(navController: NavHostController, mainViewModel: MainViewModel = hiltViewModel()) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    var emailState by rememberSaveable { mutableStateOf("") }
    var passwordState by rememberSaveable { mutableStateOf("") }
    var confirmPasswordState by rememberSaveable { mutableStateOf("") }
    var passwordVisibilityState by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisibilityState by rememberSaveable { mutableStateOf(false) }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = stringResource(id = R.string.back_arrow_icon_desc_text),
            modifier = Modifier
                .size(34.dp)
                .clickable { onBackClicked(navController = navController) }
                .align(alignment = Alignment.Start)
        )

        Spacer(modifier = Modifier.height(height = 40.dp))

        Text(
            text = stringResource(R.string.create_account_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(height = 30.dp))

        Text(
            text = stringResource(R.string.signup_screen_subheader_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(height = mWidth * 0.2F))

        OutlinedTextField(
            value = emailState,
            singleLine = true,
            onValueChange = { emailState = it },
            label = {
                Text(
                    text = stringResource(id = R.string.email_field_label_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.email_field_placeholder_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        OutlinedTextField(
            value = passwordState,
            singleLine = true,
            onValueChange = { passwordState = it },
            label = {
                Text(
                    text = stringResource(R.string.password_field_lable_text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            visualTransformation = if (passwordVisibilityState) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally),
            placeholder = {
                Text(
                    text = stringResource(R.string.password_field_placeholder_text),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisibilityState = !passwordVisibilityState
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisibilityState) {
                            Icons.Rounded.Visibility
                        } else {
                            Icons.Rounded.VisibilityOff
                        },
                        contentDescription = stringResource(R.string.password_filed_visibility_icon_desc),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        OutlinedTextField(
            value = confirmPasswordState,
            singleLine = true,
            onValueChange = { confirmPasswordState = it },
            label = {
                Text(
                    text = stringResource(R.string.confirm_password_field_label_text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            visualTransformation = if (confirmPasswordVisibilityState) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally),
            placeholder = {
                Text(
                    text = stringResource(R.string.confirm_password_field_placeholder_text),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        confirmPasswordVisibilityState = !confirmPasswordVisibilityState
                    }
                ) {
                    Icon(
                        imageVector = if (confirmPasswordVisibilityState) {
                            Icons.Rounded.Visibility
                        } else {
                            Icons.Rounded.VisibilityOff
                        },
                        contentDescription = stringResource(R.string.confirm_password_field_visibility_icon_desc),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(height = 35.dp))

        Button(
            modifier = Modifier
                .width(width = mWidth * 0.7F)
                .height(45.dp)
                .align(alignment = Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small,
            onClick = {
                if (emailState.isEmpty()) {
                    onErrorMessage = mContext.getString(R.string.empty_email_alert_text)
                } else if (!isEmailValid(emailState)) {
                    onErrorMessage = mContext.getString(R.string.incorrect_email_alert_text)
                } else if (passwordState.isEmpty()) {
                    onErrorMessage = mContext.getString(R.string.empty_password_alert_Text)
                } else if (!isValidPassword(passwordState)) {
                    onErrorMessage = mContext.getString(R.string.incorrect_password_alert_text)
                } else if (confirmPasswordState.isEmpty()) {
                    onErrorMessage = mContext.getString(R.string.empty_password_alert_Text)
                } else if (!isValidPassword(passwordState)) {
                    onErrorMessage = mContext.getString(R.string.incorrect_password_alert_text)
                } else if (passwordState != confirmPasswordState) {
                    onErrorMessage = mContext.getString(R.string.confirm_password_alert_text)
                } else {
                    mainViewModel.signUpToFirebaseWithEmailAndPassword(
                        email = emailState,
                        password = passwordState,
                        onSuccess = {
                            onNavigateToAccountTypeScreen(navController)
                        },
                        onFailure = {
                            onErrorMessage = it
                        }
                    )
                }
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.signup_button_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Text(
            text = stringResource(R.string.skip_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(10.dp)
                .clickable { onSkipAuthProcess(navController = navController) }
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

    }
}

private fun onBackClicked(navController: NavController) {
    navController.popBackStack()
}

fun onNavigateToAccountTypeScreen(navController: NavHostController) {
    navController.navigate(route = AccountTypeScreen.route) {
        popUpTo(route = SignupScreen.route) {
            inclusive = true
        }
    }
}

private fun onSkipAuthProcess(navController: NavHostController) {
    navController.navigate(route = AccountTypeScreen.route) {
        popUpTo(route = SignupScreen.route) {
            inclusive = true
        }
    }
}
