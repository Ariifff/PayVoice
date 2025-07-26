package com.arif.payvoice.starter

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arif.payvoice.ui.theme.Blue

//import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen() {
    val context = LocalContext.current
    //val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf(TextFieldValue("")) }
    var isSending by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reset Password", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                /* isSending = true
                auth.sendPasswordResetEmail(email.text.trim())
                    .addOnCompleteListener { task ->
                        isSending = false
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Password reset link sent to ${email.text}",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                task.exception?.message ?: "Failed to send email",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }*/

            },
            colors=ButtonDefaults.buttonColors(containerColor = Blue),
            enabled = !isSending && email.text.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSending) "Sending..." else "Send Reset Link")
        }
    }
}
