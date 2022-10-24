package com.example.rsdev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        setContentView(R.layout.activity_register)

        //Recupération des variables sur la vue
        val btn_login = findViewById(R.id.btn_login) as Button
        val email_login_input = findViewById(R.id.email_login) as EditText
        val password_login_input = findViewById(R.id.password_login) as EditText

        // Action au click de connexion
        btn_login.setOnClickListener {
            val email: String = email_login_input.text.toString()
            val password: String = password_login_input.text.toString()

            //On vérifie que les champs ne sont pas vides
            if(email.toString().trim().isNotEmpty() && password.toString().isNotEmpty()) {
                //login du user avec son MDP
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Merci d'entrer des identifiants valides", Toast.LENGTH_SHORT).show()
            }
        }
    }
}