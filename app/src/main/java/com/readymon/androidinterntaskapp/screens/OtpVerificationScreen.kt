package com.readymon.androidinterntaskapp.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    flow: String,
    onVerificationSuccess: () -> Unit
) {
    var otp1 by remember { mutableStateOf("") }
    var otp2 by remember { mutableStateOf("") }
    var otp3 by remember { mutableStateOf("") }
    var otp4 by remember { mutableStateOf("") }
    var otp5 by remember { mutableStateOf("") }
    var otp6 by remember { mutableStateOf("") }

    var timeLeft by remember { mutableStateOf(120) } // 2 minutes in seconds
    var isResendEnabled by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val focusRequesters = remember { List(6) { FocusRequester() } }

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else {
            isResendEnabled = true
        }
    }

    fun verifyOtp() {
        val enteredOtp = "$otp1$otp2$otp3$otp4$otp5$otp6"
        if (enteredOtp.length == 6) {
            // In a real app, verify with backend
            // For demo, accept any 6-digit code
            onVerificationSuccess()
        } else {
            showError = true
        }
    }

    fun resendOtp() {
        // Reset timer
        timeLeft = 120
        isResendEnabled = false
        // Clear OTP fields
        otp1 = ""
        otp2 = ""
        otp3 = ""
        otp4 = ""
        otp5 = ""
        otp6 = ""
        showError = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verify OTP",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Enter the 6-digit code sent to your email",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OtpTextField(
                value = otp1,
                onValueChange = {
                    if (it.length <= 1) {
                        otp1 = it
                        if (it.length == 1) focusRequesters[1].requestFocus()
                    }
                },
                focusRequester = focusRequesters[0]
            )
            OtpTextField(
                value = otp2,
                onValueChange = {
                    if (it.length <= 1) {
                        otp2 = it
                        if (it.length == 1) focusRequesters[2].requestFocus()
                    }
                },
                focusRequester = focusRequesters[1]
            )
            OtpTextField(
                value = otp3,
                onValueChange = {
                    if (it.length <= 1) {
                        otp3 = it
                        if (it.length == 1) focusRequesters[3].requestFocus()
                    }
                },
                focusRequester = focusRequesters[2]
            )
            OtpTextField(
                value = otp4,
                onValueChange = {
                    if (it.length <= 1) {
                        otp4 = it
                        if (it.length == 1) focusRequesters[4].requestFocus()
                    }
                },
                focusRequester = focusRequesters[3]
            )
            OtpTextField(
                value = otp5,
                onValueChange = {
                    if (it.length <= 1) {
                        otp5 = it
                        if (it.length == 1) focusRequesters[5].requestFocus()
                    }
                },
                focusRequester = focusRequesters[4]
            )
            OtpTextField(
                value = otp6,
                onValueChange = {
                    if (it.length <= 1) {
                        otp6 = it
                    }
                },
                focusRequester = focusRequesters[5]
            )
        }

        if (showError) {
            Text(
                text = "Please enter a valid 6-digit OTP",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Time remaining: ${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (timeLeft > 30) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { verifyOtp() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Verify", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { if (isResendEnabled) resendOtp() },
            enabled = isResendEnabled
        ) {
            Text(
                text = if (isResendEnabled) "Resend OTP" else "Resend OTP (${timeLeft}s)",
                fontWeight = FontWeight.Bold
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
        modifier = Modifier
            .width(50.dp)
            .focusRequester(focusRequester),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}