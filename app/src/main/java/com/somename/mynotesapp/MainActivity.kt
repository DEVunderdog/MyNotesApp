package com.somename.mynotesapp
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

import com.somename.mynotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var TAG = "FuckingRecyclerViewError"
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<NotesDataModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAddFab:FloatingActionButton = binding.myFab
        mAddFab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, NewNote::class.java)
            startActivity(intent)
        })

        noteRecyclerView = binding.recyclerView
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.setHasFixedSize(true)

        val spacingPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        val itemDecoration = ItemSpacingDecoration(spacingPixels)
        noteRecyclerView.addItemDecoration(itemDecoration)

        noteArrayList = arrayListOf<NotesDataModel>()
        getNoteData()




    }

    private fun getNoteData(){
        dbRef = FirebaseDatabase.getInstance().getReference("Notes")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    noteArrayList.clear()
                    for(noteSnapshot in snapshot.children){
                        val userNote = noteSnapshot.getValue(NotesDataModel::class.java)
                        noteArrayList.add(userNote!!)
                    }

                    noteRecyclerView.adapter = MainAdapter(noteArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Log.e(TAG, "SomeFucking Error")

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sign_in, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.SignIn ->{
                val intent = Intent(this, SignInActivity::class.java )
                startActivity(intent)
                return true
            } else -> return super.onOptionsItemSelected(item)
        }
    }


}