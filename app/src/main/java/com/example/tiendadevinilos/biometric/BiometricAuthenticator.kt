package com.example.tiendadevinilos.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticator(private val context: Context) {
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(context)
    private lateinit var biometricPrompt: BiometricPrompt

    fun isBiometricAvailable(): BiometricAuthenticationStatus {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthenticationStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthenticationStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthenticationStatus.TEMPORARY_NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthenticationStatus.NOT_AVAILABLE
        }
    }

    fun promptBiometricAuth(
        title: String,
        subTitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit
    ) {
        when (isBiometricAvailable()) {
            BiometricAuthenticationStatus.NOT_AVAILABLE -> {
                onError(
                    BiometricAuthenticationStatus.NOT_AVAILABLE.code,
                    "Biometrias no disponibles"
                )
                return
            }

            BiometricAuthenticationStatus.TEMPORARY_NOT_AVAILABLE -> {
                onError(
                    BiometricAuthenticationStatus.TEMPORARY_NOT_AVAILABLE.code,
                    "Biometrias temporariamente no disponibles"
                )
                return
            }

            BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED -> {
                onError(
                    BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED.code,
                    "Aun no hay biometrias registradas"
                )
                return
            }

            else -> Unit
        }

        biometricPrompt =
            BiometricPrompt(
                fragmentActivity,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess(result)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        onError(errorCode, errString)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailed()
                    }
                }
            )
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButtonText)
            .build()
        biometricPrompt.authenticate(promptInfo)


    }

}