package com.diva.pimpad

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.diva.pimpad.databinding.ActivityMainBinding
import com.diva.pimpad.model.Users
import com.diva.pimpad.ui.login.LoginActivity
import com.diva.pimpad.ui.profile.ProfileActivity
import com.diva.pimpad.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var util: Util
    private lateinit var myId: String
    private var databaseReference: DatabaseReference? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    var user = ArrayList<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        util = Util()
        myId = util.getUID()!!

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        loadUser()

        setupView()

        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
            return
        }

        binding.logout.setOnClickListener {
            showPopup()
        }
        binding.profileImage.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_detect, R.id.navigation_chat, R.id.navigation_resultDetect
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    fun loadUser() {
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user.clear()
                val user = snapshot.getValue(Users::class.java)
                binding.name.text = user!!.username
                Picasso.get()
                    .load(user.profileImage)
                    .into(binding.profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun updateStatus(status: String) {
        val hashMap = HashMap<String, Any>()
        hashMap.put("status", status)
        databaseReference!!.updateChildren(hashMap)


    }

    override fun onResume() {
        super.onResume()
        updateStatus("online")
//        util.updateOnlineStatus("online")
    }

    override fun onPause() {
        super.onPause()
        updateStatus("offline")
//        util.updateOnlineStatus("offline")
    }

    private fun showPopup() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        alert.setMessage("Apakah anda mau keluar?")
            .setPositiveButton("Logout",  { dialog, which ->
                signOut()
            }).setNegativeButton("Cancel", null)
        val alert1: AlertDialog = alert.create()
        alert1.show()
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}