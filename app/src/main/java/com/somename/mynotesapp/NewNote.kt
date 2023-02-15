package com.somename.mynotesapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.somename.mynotesapp.databinding.ActivityNewNoteBinding
import java.util.UUID


class NewNote : AppCompatActivity() {
    private val TAG = "FuckingNotesAPP"
    private lateinit var binding:ActivityNewNoteBinding





    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "On Create is called ")
        super.onCreate(savedInstanceState)
        val binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "New Note "
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val database = Firebase.database.reference
        val noteRef = database.child("Notes")

        val existingNoteId = intent.getStringExtra("noteId")
        val existingTitle = intent.getStringExtra("noteTitle")
        val existingNote = intent.getStringExtra("noteContent")
        if(existingNoteId != null && existingTitle != null && existingNote != null ){
            binding.EditTitle.setText(existingTitle)
            binding.EditNoteContent.setText(existingNote)
        }


        binding.btnSave.setOnClickListener{
            val title = binding.EditTitle.text.toString()
            val content = binding.EditNoteContent.text.toString()

            if(existingNoteId != null && existingTitle != null && existingNote != null ){
                val mynoteRef = database.child("Notes").child(existingNoteId)
                mynoteRef.child("noteUTitle").setValue(title)
                mynoteRef.child("noteUContent").setValue(content)
                    .addOnSuccessListener {
                        Log.d(TAG, "Note Added Successfully")
                        finish()
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Error updating note: ${it.message}")
                    }
            } else {
                val noteId = UUID.randomUUID().toString()
                val notesList = NotesDataModel(noteId, title, content)
                notesList.noteUID?.let { it1 -> noteRef.child(it1).setValue(notesList, object:DatabaseReference.CompletionListener{
                    override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                        if(error == null){
                            Log.e(TAG, "There isn't any error")
                            finish()
                        } else {
                            Log.e(TAG, "There is error")
                        }
                    }
                }) }
            }

        }
    }

}