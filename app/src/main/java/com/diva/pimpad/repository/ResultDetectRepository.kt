package com.diva.pimpad.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.diva.pimpad.model.ResultDetect
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultDetectRepository {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser!!.uid
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("ResultAcne/").child(uid)
    var resultDetect = ArrayList<ResultDetect>()

    @Volatile private var INSTANCE : ResultDetectRepository ?= null
    fun getInstance(): ResultDetectRepository{
        return INSTANCE ?: synchronized(this){
            val instance = ResultDetectRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadResultDetect(resultDetectList: MutableLiveData<ArrayList<ResultDetect>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resultDetect.clear()
                try {
                    val _resultDetectUserList : ArrayList<ResultDetect> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(ResultDetect::class.java)!!
                    } as ArrayList<ResultDetect>
                    resultDetectList.postValue(_resultDetectUserList)
                    Log.d("result", _resultDetectUserList.toString())
                } catch (e : Exception){

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}