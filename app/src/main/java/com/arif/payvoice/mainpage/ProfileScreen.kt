package com.arif.payvoice.mainpage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.arif.payvoice.model.User
import com.arif.payvoice.ui.theme.Indigo
import com.arif.payvoice.ui.theme.Purple40
import com.arif.payvoice.ui.theme.White
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var user by remember { mutableStateOf<User?>(null) }
    val database = Firebase.database.reference
    var isInitialLoad by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current


    // Fetch user data when screen loads
    LaunchedEffect(currentUser?.uid) {
        if(isInitialLoad && currentUser?.uid !=null ) {
            getUserData(
                userId = currentUser.uid,
                onSuccess = { fetchedUser ->
                    user = fetchedUser
                    name = fetchedUser.name
                    email = fetchedUser.email
                    address = fetchedUser.address
                    isInitialLoad = false
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()

                    isInitialLoad = false
                }
            )
        }
    }

    Box (
        modifier = Modifier.fillMaxSize()
            .background(color = White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Purple40),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome, ${if (name.isNotEmpty()) name else "User"} 👋",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        modifier = Modifier.clickable { isEditing = true }
                    )
                }

                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "logout",
                    tint = Red,
                    modifier = Modifier
                        .clickable {
                            FirebaseAuth.getInstance().signOut()
                            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }

                    }
                )

            }

            key("name_field_${currentUser?.uid}") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            key("email_field_${currentUser?.uid}") {
                OutlinedTextField(
                    value = email.ifEmpty { currentUser?.email ?: "" },
                    onValueChange = {},
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    isError = showError && !email.contains("@"),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Gray,
                        disabledContainerColor = White,
                        disabledLeadingIconColor = Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            key("address_field_${currentUser?.uid}") {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing
                )
            }

            if (showError) {
                Text(
                    text = "Please enter valid Email",
                    color = Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = {
                        // Update user data in Firebase
                        currentUser?.uid?.let { userId ->
                            val updatedUser = User(
                                uid = userId,
                                name = name,
                                email = email,
                                address = address
                            )

                            database.child("users").child(userId).setValue(updatedUser)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                    isEditing = false
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                    isEditing = true // Keep in edit mode if update fails
                                }
                        }

                },
                colors = ButtonDefaults.buttonColors(containerColor = Indigo),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text="Save", color = Color.White)
            }

        }

        Text(
            text = "PayVoice v1.0\nDeveloped by Alig",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

    }

}

fun getUserData(userId: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
    val database = Firebase.database.reference
    database.child("users").child(userId).get()
        .addOnSuccessListener { dataSnapshot ->
            val user = dataSnapshot.getValue(User::class.java)
            user?.let(onSuccess) ?: onFailure(Exception("User data not found"))
        }
        .addOnFailureListener(onFailure)
}

