package com.diva.pimpad.repository

import androidx.lifecycle.MutableLiveData
import com.diva.pimpad.model.Acne
import com.google.firebase.database.*

class AcneRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Acne")

    @Volatile private var INSTANCE : AcneRepository ?= null

    fun getInstance(): AcneRepository{
        return INSTANCE ?: synchronized(this){

            val instance = AcneRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadAcnes(acneList : MutableLiveData<ArrayList<Acne>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _acneList : ArrayList<Acne> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Acne::class.java)!!
                    } as ArrayList<Acne>
                    acneList.postValue(_acneList)

                }catch (e: Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}