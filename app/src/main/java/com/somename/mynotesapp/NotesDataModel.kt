package com.somename.mynotesapp

data class NotesDataModel(
    var noteUID: String? = null,
    var noteUTitle:String? = null,
    var noteUContent:String? = null,
    var isSelected:Boolean = false
)
