package com.example.rsdev

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        setContentView(R.layout.activity_register)

        val db = Firebase.firestore

        //Recupération des variables sur la vue
        val btn_login = findViewById(R.id.btn_login) as Button

        // Action au click de connexion
        btn_login.setOnClickListener {
            val firstname = findViewById<EditText>(R.id.firstname_user).text.toString();
            val lastname = findViewById<EditText>(R.id.lastname_user).text.toString();
            val username = findViewById<EditText>(R.id.username_user).text.toString();
            val birthDate =  findViewById<DatePicker>(R.id.brith_date);
            val email = findViewById<EditText>(R.id.email_user).text.toString();
            val password = findViewById<EditText>(R.id.password_login).text.toString();
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString();
            val day_brith: Int = birthDate.getDayOfMonth();
            val month_brith: Int = birthDate.getMonth() + 1;
            val year_brith: Int = birthDate.getYear();

            //On vérifie que les champs ne sont pas vides
            if(email.trim().isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                //login du user avec son MDP
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val users = hashMapOf(
                                "user_id" to "user_id",
                                firstname to "firstname",
                                lastname to "lastname",
                                username to "username",
                                email to "email"
                            )

                            db.collection("users").add(users)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot written with ID: ${documentReference.id}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                                Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()
                        }
                    }

            }else{
                Toast.makeText(applicationContext, "Merci d'entrer des identifiants valides", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
