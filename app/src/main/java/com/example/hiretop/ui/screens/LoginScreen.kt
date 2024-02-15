package com.example.hiretop.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hiretop.R
import com.example.hiretop.navigation.NavDestination

object LoginScreen : NavDestination {
    override val route: String = "login_screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    var emailState by rememberSaveable { mutableStateOf("") }
    var passwordState by rememberSaveable { mutableStateOf("") }
    var passwordVisibilityState by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_screen_header_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(height = 30.dp))

        Text(
            text = stringResource(R.string.login_screen_subheader_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
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
                    text = stringResource(R.string.email_field_label_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.email_field_placeholder_text),
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

        Spacer(modifier = Modifier.height(height = 15.dp))

        Text(
            text = stringResource(R.string.forgot_password_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(alignment = Alignment.End)
                .clickable { onForgotPasswordClicked(mContext) }
                .padding(horizontal = 25.dp)
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

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
            onClick = { onLoginClicked(mContext, navController) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.login_button_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 35.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(modifier = Modifier.weight(weight = 1F))

            Spacer(modifier = Modifier.width(width = 10.dp))

            Text(
                text = stringResource(R.string.continue_with_text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(width = 10.dp))

            Divider(modifier = Modifier.weight(weight = 1F))
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_colorful_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onLoginWithGoogleClicked(mContext) }
            )

            Spacer(modifier = Modifier.width(width = mWidth * 0.1F))

            Image(
                painter = painterResource(id = R.drawable.ic_github_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        onLoginWithGithubClicked(mContext)
                    }
            )
        }

        Spacer(modifier = Modifier.height(height = 30.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.new_user_text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.padding(horizontal = 1.dp))
            Text(
                text = stringResource(id = R.string.create_account_text),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    onCreateAccountClicked(mContext, navController)
                }
            )
        }

    }
}

fun onForgotPasswordClicked(context: Context) {
    Toast.makeText(context, "Mot de passe oublié", Toast.LENGTH_SHORT).show()
}

fun onLoginClicked(context: Context, navController: NavHostController) {
    navController.navigate(route = AccountTypeScreen.route) {
        popUpTo(route = LoginScreen.route) {
            inclusive = true
        }
    }
}

fun onLoginWithGoogleClicked(context: Context) {
    Toast.makeText(context, "Se connecter avec son compte google", Toast.LENGTH_SHORT).show()
}

fun onLoginWithGithubClicked(context: Context) {
    Toast.makeText(context, "Se connecter avec son compte github", Toast.LENGTH_SHORT).show()
}

fun onCreateAccountClicked(context: Context, navController: NavHostController) {
    navController.navigate(route = SignupScreen.route) {
        popUpTo(route = LoginScreen.route) {
            inclusive = true
        }
    }
}
