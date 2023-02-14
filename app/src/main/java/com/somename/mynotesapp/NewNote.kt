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
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.somename.mynotesapp.databinding.ActivityNewNoteBinding


class NewNote : AppCompatActivity() {
    private lateinit var binding:ActivityNewNoteBinding
    private lateinit var notesDao:NotesDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "New Note "
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.saveNote ->{
                val noteTitle = binding.EditTitle.text.toString()
                val noteContent = binding.EditNoteContent.text.toString()
                if(noteTitle.isNotEmpty() && noteContent.isNotEmpty()){
                    notesDao.addNote(noteContent, noteTitle)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}