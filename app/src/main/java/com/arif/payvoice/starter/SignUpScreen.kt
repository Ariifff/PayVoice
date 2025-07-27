package com.arif.payvoice.starter

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arif.payvoice.model.User
import com.arif.payvoice.ui.theme.Blue
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    onSignupSuccess: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("") }
    var isEmailVerified by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val database= Firebase.database.reference

    // Check verification status periodically
    LaunchedEffect(email) {
        while (email.isNotBlank() && !isEmailVerified) {
            delay(3000)
            auth.signInWithEmailAndPassword(email, "temporaryPassword123")
                .addOnSuccessListener {
                    it.user?.reload()?.addOnSuccessListener { _ ->
                        isEmailVerified = it.user?.isEmailVerified ?: false
                        if (!isEmailVerified) {
                            auth.signOut()
                        }
                    }
                }
                .addOnFailureListener {
                    // Couldn't sign in - probably not created yet
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Owner Name") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field with Verify Button
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailVerified = false // reset if email is changed
            },
            label = { Text("Email") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            trailingIcon = {
                if (email.isNotBlank()) {
                    if (isEmailVerified) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Verified",
                            tint = Color.Green,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    } else {
                        Text(
                            "Verify",
                            color = Blue,
                            modifier = Modifier
                                .clickable {
                                    if (email.isNotEmpty()) {
                                        loading = true
                                        auth.fetchSignInMethodsForEmail(email)
                                            .addOnCompleteListener { methodTask ->
                                                if (methodTask.isSuccessful) {
                                                    val signInMethods = methodTask.result?.signInMethods
                                                    if (signInMethods.isNullOrEmpty()) {
                                                        // New user — create temp account to send verification
                                                        auth.createUserWithEmailAndPassword(
                                                            email,
                                                            "temporaryPassword123"
                                                        )
                                                            .addOnCompleteListener { createTask ->
                                                                if (createTask.isSuccessful) {
                                                                    auth.currentUser?.sendEmailVerification()
                                                                        ?.addOnCompleteListener { verifyTask ->
                                                                            if (verifyTask.isSuccessful) {
                                                                                Toast.makeText(
                                                                                    context,
                                                                                    "Verification email sent to your email, check spam folder",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            } else {
                                                                                Toast.makeText(
                                                                                    context,
                                                                                    "Failed to send email: ${verifyTask.exception?.message}",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                            auth.signOut()
                                                                            loading = false
                                                                        }
                                                                } else {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Error: ${createTask.exception?.message}",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    loading = false
                                                                }
                                                            }
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Email already registered",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        loading = false
                                                    }
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Error checking email",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    loading = false
                                                }
                                            }
                                    }
                                }
                                .wrapContentSize()
                                .clip(RoundedCornerShape(4.dp))
                                .padding(end = 16.dp),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!isEmailVerified) {
                        Toast.makeText(context, "Please verify your email first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    auth.signInWithEmailAndPassword(email, "temporaryPassword123")
                        .addOnSuccessListener {
                            val user = auth.currentUser
                            user?.updatePassword(password)
                                ?.addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        // Create User object
                                        val newUser = User(
                                            uid = user.uid,
                                            name = name,
                                            email = email,
                                            address = address
                                        )

                                        // Save to Realtime Database
                                        database.child("users").child(user.uid).setValue(newUser)
                                            .addOnCompleteListener { dbTask ->
                                                if (dbTask.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Sign-up complete!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onSignupSuccess()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to save user data: ${dbTask.exception?.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(context, "Failed to set password", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Please verify first or try again", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue)
        ) {
            Text("Sign Up", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have account? ")
            Text(
                text = "Login",
                color = Blue,
                modifier = Modifier.clickable { },
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}