package com.example.hiretop.ui.screens.auth

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hiretop.R
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.screens.AccountTypeScreen

object SignupScreen : NavDestination {
    override val route: String = "signup_screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    var emailState by rememberSaveable { mutableStateOf("") }
    var passwordState by rememberSaveable { mutableStateOf("") }
    var confirmPasswordState by rememberSaveable { mutableStateOf("") }
    var passwordVisibilityState by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisibilityState by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.create_account_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(height = 30.dp))

        Text(
            text = stringResource(R.string.signup_screen_subheader_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(horizontal = 15.dp)
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
                .padding(horizontal = 25.dp),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.email_field_placeholder_text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
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
                .padding(horizontal = 25.dp),
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
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
                .padding(horizontal = 25.dp),
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(height = 35.dp))

        Button(
            modifier = Modifier
                .width(width = mWidth * 0.7F)
                .height(45.dp)
                .padding(horizontal = 15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small,
            onClick = { onSignupClicked(mContext, navController) }
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

        Spacer(modifier = Modifier.height(height = 25.dp))

    }
}

fun onSignupClicked(mContext: Context, navController: NavHostController) {
    navController.navigate(route = AccountTypeScreen.route) {
        popUpTo(route = SignupScreen.route) {
            inclusive = true
        }
    }
}
