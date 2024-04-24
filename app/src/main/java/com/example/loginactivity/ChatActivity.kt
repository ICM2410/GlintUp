package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.loginactivity.databinding.ActivityChatBinding
import java.util.concurrent.Executor

class ChatActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var binding : ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navegacion.menu.getItem(3).isChecked = true

        binding.navegacion.setOnItemSelectedListener {
            navigateToItem(it.itemId)
        }

        val intent = Intent(this, PersonalChatActivity::class.java)

        configurarBotonSiguiente()

        //Verificar permisos
        // Lets the user authenticate using either a Class 3 biometric or
        // their lock screen credential (PIN, pattern, or password).
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biométrica para acceder a los chats")
            .setSubtitle("Necesitamos saber que eres tú para darte acceso a los chats.")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d("MY_APP_TAG", "La aplicación puede hacer autenticación biométrica.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "El dispositivo no cuenta con sensores biométricos.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Las características biométricas no están disponibles en este momento.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                startActivity(enrollIntent)
            }
        }

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Error de autenticación: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Autenticación concedida!", Toast.LENGTH_SHORT

                    )
                        .show()
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Autenticación fallida.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

    }

    override fun onResume() {
        super.onResume()
        binding.navegacion.menu.getItem(3).isChecked = true

    }

    private fun navigateToItem(itemId: Int): Boolean {
        when (itemId) {
            R.id.likes -> {
                startActivity(Intent(this, LikesActivity::class.java))
                return true
            }
            R.id.profile -> {
                startActivity(Intent(this, UserActivity::class.java))
                return true
            }
            R.id.lamp -> {
                startActivity(Intent(this, MatchActivity::class.java))
                return true
            }
            R.id.explore -> {
                startActivity(Intent(this, ExploreActivity::class.java))
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun configurarBotonSiguiente() {
        binding.bradpi.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)

        }
    }
}
