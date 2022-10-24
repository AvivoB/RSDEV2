package com.example.rsdev

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        setContentView(R.layout.activity_login)

        //Recupération des variables sur la vue
        val btn_login = findViewById(R.id.btn_login) as Button
        val btn_register = findViewById(R.id.btn_register) as Button
        val btn_google = findViewById(R.id.google_login) as Button
        val email_login_input = findViewById(R.id.email_login) as EditText
        val password_login_input = findViewById(R.id.password_login) as EditText

        // Action au click de connexion
        btn_login.setOnClickListener {
            val email: String = email_login_input.text.toString()
            val password: String = password_login_input.text.toString()

            //On vérifie que les champs ne sont pas vides
            if(email.toString().trim().isNotEmpty() && password.toString().isNotEmpty()) {
                //login du user avec son MDP
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()
                                val FeedActivity = Intent(this, FeedActivity::class.java)
                                FeedActivity.putExtra("keyIdentifier", "value")
                                startActivity(FeedActivity)
                            } else {
                                Toast.makeText(applicationContext, "Identifants incorrects", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
            }else{
                Toast.makeText(applicationContext, "Merci d'entrer des identifiants valides", Toast.LENGTH_SHORT).show()
            }
        }

        btn_google.setOnClickListener{
            oneTapClient = Identity.getSignInClient(this)
            signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build())
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("922656101530-r7nf5gsa727fpmj721bnc7jslmvi9hm7.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build()

            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, REQ_ONE_TAP,
                            null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                }

        }

        btn_register.setOnClickListener {
            val RegisterActivity = Intent(this, RegisterActivity::class.java)
            startActivity(RegisterActivity)
        }
    }
}