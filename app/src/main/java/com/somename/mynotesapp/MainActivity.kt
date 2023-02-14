package com.somename.mynotesapp

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.somename.mynotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAddFab:FloatingActionButton = binding.myFab
        supportActionBar?.hide()
        mAddFab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, NewNote::class.java)
            startActivity(intent)
        })
    }

    private val signInClient by lazy{
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(this, googleSignInOptions)

    }

    private val signInHandler = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch(e:ApiException){
                Toast.makeText(this, "An error occur while signing.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showSignInDialog(){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Google Sign In")
        builder.setMessage("Please, Sign in to enjoy its perks ;)")
        builder.setPositiveButton("Sign In"){ _,_ ->
            signInHandler.launch(signInClient.signInIntent)
        }
        builder.setNegativeButton("Cancel"){ dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun firebaseAuthWithGoogle(account:GoogleSignInAccount){
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                SavedPreference.setEmail(this,account.email.toString())
                SavedPreference.setUsername(this, account.displayName.toString())
                Toast.makeText(this, "SignIn Success", Toast.LENGTH_LONG).show()

            } else{
                Toast.makeText(this,"SignIn Failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    object SavedPreference{
        const val EMAIL = "email"
        const val USERNAME = "USERNAME"

        private fun getSharedPreference(ctx: Context?): SharedPreferences?{
            return ctx?.let { androidx.preference.PreferenceManager.getDefaultSharedPreferences(it) }
        }

        private fun editor(context: Context, const:String, string:String){
            getSharedPreference(context)?.edit()?.putString(const, string)?.apply()
        }

        fun setEmail(context:Context, email:String){
            editor(
                context,
                EMAIL,
                email
            )
        }

        fun setUsername(context:Context, username:String){
            editor(
                context,
                USERNAME,
                username
            )
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account == null){
            showSignInDialog()

        }
    }
}