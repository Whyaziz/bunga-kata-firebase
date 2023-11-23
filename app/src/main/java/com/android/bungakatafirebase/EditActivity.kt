package com.android.bungakatafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.bungakatafirebase.databinding.ActivityEditBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditBinding
    private val firestore = FirebaseFirestore.getInstance()
    var updateId = ""
    private val puisiLiveData : MutableLiveData<List<Puisi>>
            by lazy {
                MutableLiveData<List<Puisi>>()
            }

    private val puisiCollectionRef  = firestore.collection("puisi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)



        updateId = intent.getStringExtra("UPDATE_ID").toString()
        val judul = intent.getStringExtra("TITLE").toString()
        val writer = intent.getStringExtra("WRITER").toString()
        val content = intent.getStringExtra("CONTENT").toString()

        with(binding){
            txtTitle.setText(judul)
            txtWriter.setText(writer)
            txtPuisi.setText(content)

            btnSimpan.setOnClickListener {
                update(Puisi(
                    judul = txtTitle.text.toString(),
                    penulis = txtWriter.text.toString(),
                    puisi = txtPuisi.text.toString()
                ))
                finish()
            }
        }
    }

    private fun update(puisi: Puisi){
        puisiCollectionRef.document(updateId).set(puisi).addOnFailureListener{
            Log.d("MainActivity", "Error updating budget : ")
        }
    }
}