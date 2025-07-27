package com.arif.payvoice.mainpage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.arif.payvoice.model.User
import com.arif.payvoice.ui.theme.Indigo
import com.arif.payvoice.ui.theme.Purple40
import com.arif.payvoice.ui.theme.White
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    // State variables
    var user by remember { mutableStateOf<User?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val context = LocalContext.current
    val database = Firebase.database.reference

    // Fetch user data on first load
    LaunchedEffect(currentUser?.uid) {
        if (currentUser?.uid != null) {
            getUserData(
                userId = currentUser.uid,
                onSuccess = { fetchedUser ->
                    user = fetchedUser
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                auth.signOut()
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                navController.navigate("login") { popUpTo(0) }
                showLogoutDialog = false
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Scaffold(
        bottomBar = {
            // Footer positioned near navigation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VersionFooter()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Header Card
                ProfileHeaderCard(user?.name ?: "User")

                Spacer(modifier = Modifier.height(24.dp))

                // Edit Profile Section
                EditProfileSection(
                    isEditing = isEditing,
                    onEditClick = { isEditing = true },
                    onLogoutClick = { showLogoutDialog = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Form
                ProfileForm(
                    user = user,
                    isEditing = isEditing,
                    onNameChange = { user = user?.copy(name = it) },
                    onAddressChange = { user = user?.copy(address = it) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                if (isEditing) {
                    SaveButton(
                        onClick = {
                            user?.let {
                                updateUserData(
                                    userId = currentUser?.uid ?: return@SaveButton,
                                    user = it.copy(uid = currentUser?.uid ?: ""), // Ensure UID is included
                                    onSuccess = {
                                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                        isEditing = false
                                    },
                                    onFailure = {
                                        Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    )
                }
            }


        }
    }
}

@Composable
private fun ProfileHeaderCard(userName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                text = "Welcome, $userName 👋",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}

@Composable
private fun EditProfileSection(
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
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
            if (!isEditing) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit",
                    modifier = Modifier.clickable(onClick = onEditClick)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = "logout",
            tint = Color.Red,
            modifier = Modifier.clickable(onClick = onLogoutClick)
        )
    }
}

@Composable
private fun ProfileForm(
    user: User?,
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = user?.name ?: "",
            onValueChange = onNameChange,
            label = { Text("Your Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = isEditing
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = user?.email ?: "",
            onValueChange = {},
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Gray,
                disabledContainerColor = White,
                disabledLeadingIconColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = user?.address ?: "",
            onValueChange = onAddressChange,
            label = { Text("Address") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEditing
        )
    }
}

@Composable
private fun SaveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Indigo),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Save", color = Color.White)
    }
}

@Composable
private fun VersionFooter() {
    Text(
        text = "PayVoice v1.0\nDeveloped by Alig",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        textAlign = TextAlign.Center,
        color = Color.Gray
    )
}

@Composable
private fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Logout", fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) {
                Text("Log Out")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun getUserData(
    userId: String,
    onSuccess: (User) -> Unit,
    onFailure: (Exception) -> Unit
) {
    Firebase.database.reference.child("users").child(userId).get()
        .addOnSuccessListener { dataSnapshot ->
            dataSnapshot.getValue(User::class.java)?.let(onSuccess)
                ?: onFailure(Exception("User data not found"))
        }
        .addOnFailureListener(onFailure)
}

private fun updateUserData(
    userId: String,
    user: User,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    Firebase.database.reference.child("users").child(userId).setValue(user)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure() }
}