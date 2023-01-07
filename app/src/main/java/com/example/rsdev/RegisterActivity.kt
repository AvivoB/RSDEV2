package com.example.rsdev

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    protected lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        setContentView(R.layout.activity_register)

        val db = Firebase.firestore
        auth = FirebaseAuth.getInstance()

        //Recupération des variables sur la vue
        val btn_login = findViewById(R.id.btn_login) as Button

        // Action au click de connexion
        btn_login.setOnClickListener {
            val firstname = findViewById<EditText>(R.id.firstname_user).text.toString();
            val lastname = findViewById<EditText>(R.id.lastname_user).text.toString();
            val username = findViewById<EditText>(R.id.username_user).text.toString();
            val birthDate = findViewById<DatePicker>(R.id.brith_date);
            val email = findViewById<EditText>(R.id.email_user).text.toString();
            val password = findViewById<EditText>(R.id.password_login).text.toString();
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString();
            val day_brith: Int = birthDate.getDayOfMonth();
            val month_brith: Int = birthDate.getMonth() + 1;
            val year_brith: Int = birthDate.getYear();

            //On vérifie que les champs ne sont pas vides
            if (email.trim()
                    .isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
            ) {
                //login du user avec son MDP
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val users = hashMapOf(
                                "user_id" to auth.currentUser?.uid.toString(),
                                "firstname" to firstname,
                                "lastname" to lastname,
                                "username" to username,
                                "email" to email,
                                "dob" to day_brith.toString() + "-" + month_brith + "-" + year_brith
                            )

                            db.collection("users").document(auth.currentUser?.uid.toString()).set(users)
                                .addOnSuccessListener { documentReference ->

                                    Toast.makeText(
                                        applicationContext,
                                        "Connexion réussie",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val FeedActivity = Intent(this, FeedActivity::class.java)
                                    FeedActivity.putExtra("keyIdentifier", "value")
                                    startActivity(FeedActivity)
                                    this.finish()

                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }

                        }
                    }

            } else {
                Toast.makeText(
                    applicationContext,
                    "Merci d'entrer des identifiants valides",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}
