package com.axoloth.bmicalculator.security.googlelogin

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialCustomException
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleLoginLogic(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun performLogin(
        onLoginSuccess: (String) -> Unit,
        onLoginError: (String?) -> Unit
    ) {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("207437685884-luj1c29vlk6fv4tm1s1o1bpus4ogudib.apps.googleusercontent.com")
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )
            
            val credential = result.credential

            if (credential is GoogleIdTokenCredential) {
                onLoginSuccess(credential.idToken)
            } else {
                onLoginError("Kredensial tidak valid atau bukan dari Google")
            }
        } catch (e: GetCredentialCancellationException) {
            Log.e("GoogleLogin", "User cancelled the operation")
            onLoginError("Login dibatalkan oleh pengguna (Canceled)")
        } catch (e: GetCredentialCustomException) {
            Log.e("GoogleLogin", "Custom Error: ${e.type}, Message: ${e.message}")
            onLoginError("Login Error Custom: ${e.message}")
        } catch (e: GetCredentialException) {
            Log.e("GoogleLogin", "Credential Error: ${e.message}")
            onLoginError("Login Error: ${e.message}")
        } catch (e: Exception) {
            Log.e("GoogleLogin", "Unexpected Error", e)
            onLoginError("Gagal terhubung ke Google: ${e.localizedMessage}")
        }
    }
}
