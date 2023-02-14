package com.somename.mynotesapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.somename.mynotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAddFab:FloatingActionButton = binding.myFab
        mAddFab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, NewNote::class.java)
            startActivity(intent)
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