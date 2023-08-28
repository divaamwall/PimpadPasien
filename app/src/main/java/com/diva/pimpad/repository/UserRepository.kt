package com.diva.pimpad.repository

import androidx.lifecycle.MutableLiveData
import com.diva.pimpad.model.Dokters
import com.google.firebase.database.*

class UserRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Dokters")
    private var dokterLists = ArrayList<Dokters>()

    @Volatile private var INSTANCE : UserRepository ?= null
    fun getInstance(): UserRepository{
        return INSTANCE ?: synchronized(this){
            val instance = UserRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadDokters(dokterList : MutableLiveData<ArrayList<Dokters>>,){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dokterLists.clear()
                try {
                    val _dokterList : ArrayList<Dokters> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Dokters::class.java)!!
                    } as ArrayList<Dokters>
                    dokterList.postValue(_dokterList)
                }catch (e: Exception){

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}