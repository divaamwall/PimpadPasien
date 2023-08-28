package com.diva.pimpad.ui.result

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.diva.pimpad.MainActivity
import com.diva.pimpad.R
import com.diva.pimpad.databinding.ActivityResultBinding
import com.diva.pimpad.ml.Model2
import com.diva.pimpad.model.Users
import com.diva.pimpad.ui.resultdetect.ResultDetectFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.Category
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var getFile: File? = null
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var username : String
    private lateinit var resultAcne: String
    val probabilityAsCategory = mutableListOf<Category>()
    private val tfImageProcesor by lazy{
        ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }
    private val imageTensor = TensorImage(DataType.FLOAT32)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("ResultAcne")

        val intent = getIntent()
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.hasil)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val myFile = intent.getSerializableExtra("image") as File
        getFile = myFile
        val result = BitmapFactory.decodeFile(getFile?.path)



        //load file txt
        val fileName = "label.txt"
        val labelFile = application.assets.open(fileName).bufferedReader().use{it.readText()}.split("\n")

        val resized = result.copy(Bitmap.Config.ARGB_8888, true)
        imageTensor.load(resized)

        val resultResized = tfImageProcesor.process(imageTensor)

        val model = Model2.newInstance(this)

        // Runs model inference and gets result.
        val outputs = model.process(resultResized.tensorBuffer)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        for (i in 0..outputFeature0.floatArray.size - 1){
            probabilityAsCategory.add(Category(labelFile[i], outputFeature0.floatArray[i]))

            //mengecek nilai yang paling mendekati
            Log.d("Hasil", outputFeature0.floatArray[i].toString() + " " + labelFile[i])
        }

        //menjadi kan nilai terbesar menjadi hasil output
        Log.d("Output", outputs.toString())

        val outputAcne = probabilityAsCategory.apply { sortByDescending { it.score } }

        binding.resultNameTv.text = outputAcne[0].label

        resultAcne = outputAcne[0].label

        // Releases model resources if no longer used.

        model.close()

        binding.imageResultIv.setImageBitmap(result)


        loadUser()

        binding.saveIv.setOnClickListener{
            uploadImage()
        }


        binding.daftarJerawatBtn.setOnClickListener {
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
    fun loadUser() {
        val reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                binding.usernameTv.setText(user!!.username)
                username = user.username
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun saveResultDetect(resultImage: String) {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val resultIdKey = reference.push().key

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap.put("userId", firebaseUser.uid)
        hashMap.put("resultId", resultIdKey)
        hashMap.put("username", username)
        hashMap.put("resultImage", resultImage)
        hashMap.put("resultAcne", resultAcne)
        hashMap.put("timeStamp", Date().time)

        reference.child("ResultAcne/").child(firebaseUser.uid).push().setValue(hashMap)

    }

    private fun saveResultDetectForDokter(resultImage: String) {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val resultIdKey = reference.push().key
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap.put("userId", firebaseUser.uid)
        hashMap.put("resultId", resultIdKey)
        hashMap.put("username", username)
        hashMap.put("resultImage", resultImage)
        hashMap.put("resultAcne", resultAcne)
        hashMap.put("timeStamp", Date().time)

        reference.child("ResultAcneForDokter").push().setValue(hashMap)
    }

    private fun uploadImage() {
        val loadingBar = ProgressDialog(this)
        loadingBar.setMessage("Please wait, the result is being saving...")
        loadingBar.show()

        val fileImage = getFile

        val uri = Uri.fromFile(fileImage)

        val storageReference = FirebaseStorage.getInstance().reference.child("ImagesFromResult").child(firebaseUser.uid)
        val reference = FirebaseDatabase.getInstance().reference
        val resultIdKey = reference.push().key
        val filePath = storageReference.child("$resultIdKey.jpg")
        filePath.putFile(uri!!).addOnCompleteListener { it ->
//                Log.d(RegisterActivity.TAG, "Successfully uploaded image : ${it.metadata?.path}")
            if (it.isSuccessful){
                filePath.downloadUrl.addOnSuccessListener {
                    val url = it.toString()
                    saveResultDetect(url)
                    saveResultDetectForDokter(url)
                    loadingBar.dismiss()
                    Toast.makeText(this@ResultActivity, "Hasil Deteksi Telah Di Upload", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ResultActivity, ResultDetectFragment::class.java))
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


