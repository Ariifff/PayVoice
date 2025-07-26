package com.arif.payvoice.mainpage

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.arif.payvoice.ui.theme.Indigo
import com.arif.payvoice.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    var name by remember { mutableStateOf("") }
    var shopName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var upi by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current



    Box (
        modifier = Modifier.fillMaxSize()
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
                    Text(
                        text = if (shopName.isNotEmpty()) shopName else "Your Shop Name",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
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

            // Text Fields with icons
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = shopName,
                onValueChange = { shopName = it },
                label = { Text("Shop Name") },
                leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                isError = showError && !email.contains("@"),
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = upi,
                onValueChange = { upi = it },
                label = { Text("UPI ID") },
                leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
                singleLine = true,
                isError = showError && !upi.contains("@"),
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing
            )

            if (showError) {
                Text(
                    text = "Please enter valid Email and UPI ID",
                    color = androidx.compose.ui.graphics.Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = {
                    if (email.contains("@") && upi.contains("@")) {
                        showError = false
                        isEditing = false
                        // TODO: Save logic (maybe use DataStore)
                    } else {
                        showError = true
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
            text = "PayVoice v1.0\nDeveloped by Arif",
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

