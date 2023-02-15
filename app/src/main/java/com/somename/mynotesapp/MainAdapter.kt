package com.somename.mynotesapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainAdapter(private val NotesList: ArrayList<NotesDataModel>): RecyclerView.Adapter<MainAdapter.MainViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_rv, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val currentItem = NotesList[position]
        holder.myTitle.text = currentItem.noteUTitle
        val position = holder.bindingAdapterPosition
        holder.btnDelete.setOnClickListener {
            val dbRef = currentItem.noteUID?.let { it1 -> FirebaseDatabase.getInstance().getReference("Notes").child(it1) }
            dbRef?.removeValue()
                ?.addOnSuccessListener {
                    if(position != RecyclerView.NO_POSITION && position < NotesList.size){
                        NotesList.removeAt(position)
                        //notifyItemRemoved(position)
                        //notifyItemRangeChanged(position, NotesList.size)
                        notifyDataSetChanged()
                    }

                }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NewNote::class.java)
            intent.putExtra("noteId", currentItem.noteUID)
            intent.putExtra("noteTitle", currentItem.noteUTitle)
            intent.putExtra("noteContent", currentItem.noteUContent)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val myTitle:TextView = itemView.findViewById<TextView>(R.id.openTitle)
        val btnDelete: Button = itemView.findViewById<Button>(R.id.btnDelete)
    }


}