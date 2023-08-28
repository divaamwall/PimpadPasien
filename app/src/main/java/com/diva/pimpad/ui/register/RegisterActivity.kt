package com.diva.pimpad.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.diva.pimpad.databinding.ActivityRegisterBinding
import com.diva.pimpad.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        setupView()
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val checkPassword = binding.edtConfirmpassword.text.toString()


            when {
                name.isEmpty() -> {
                    binding.edtName.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.edtEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edtPassword.error = "Masukkan password"
                }
                checkPassword.isEmpty() -> {
                    binding.edtConfirmpassword.error = "Masukkan konfirmasi password"
                }
                checkPassword != password -> {
                    Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                }
                else -> {
                    register(name, email, password)
                }

            }


        }

    }

    private fun register(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    var hashMap:HashMap<String, String> = HashMap()
                    hashMap.put("profileImage", "https://firebasestorage.googleapis.com/v0/b/pimpad-e4011.appspot.com/o/profile.png?alt=media&token=ced8947a-5a7f-4184-8802-a474c617f2c5")
                    hashMap.put("userId", userId)
                    hashMap.put("username", username)
                    hashMap.put("status", "offline")
                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
                }
            }
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
}