package com.somename.mynotesapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotesDao {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    private val db = FirebaseFirestore.getInstance()
    val noteCollection = db.collection("Notes")

    fun addNote(content:String, contentTitle:String){
        val onlineUserId = mAuth.currentUser?.uid
        val note = NotesData(contentTitle, content, onlineUserId)
        noteCollection.document().set(note)

    }

}