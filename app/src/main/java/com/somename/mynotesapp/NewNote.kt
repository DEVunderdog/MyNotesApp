package com.somename.mynotesapp

import android.app.ActionBar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.somename.mynotesapp.databinding.ActivityNewNoteBinding


class NewNote : AppCompatActivity() {
    private lateinit var binding:ActivityNewNoteBinding
    private lateinit var myRef: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "New Note "
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        myRef = FirebaseDatabase.getInstance().getReference("Notes")


        binding.btnSave.setOnClickListener{
            saveNote()
        }
    }


    private fun saveNote(){
        val title = binding.EditTitle.text.toString()
        val content = binding.EditNoteContent.text.toString()
        val noteId = myRef.push().key!!
        val notesList = NotesDataModel(noteId,title, content)
        myRef.child(noteId).setValue(notesList).addOnSuccessListener {
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
            val intent = Intent(this@NewNote, MainActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
        }

    }
}