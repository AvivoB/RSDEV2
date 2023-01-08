import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.rsdev.R
import com.google.firebase.database.*

class MyMessage : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_my)
        val database = FirebaseDatabase.getInstance()
        val messagesRef = database.getReference("messages")

        // Obtenez une référence aux éléments de formulaire
        val messageInput = findViewById<EditText>(R.id.message_input)
        val sendButton = findViewById<Button>(R.id.send_button)

        // Ajoutez un écouteur de clic sur le bouton "Envoyer"
        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            messagesRef.push().setValue(message)
        }
        val messagesListView = findViewById<ListView>(R.id.messages_list)
        val messages = ArrayList<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        messagesListView.adapter = adapter

        // Récupérez les messages de la base de données
        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey:
            String?) {
                val message = dataSnapshot.getValue(String::class.java)
                adapter.add(message)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}



