package com.example.fp_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fp_test.databinding.ActivityMainBinding
import com.example.fp_test.recyclerView.MusicData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity(), FirebaseOperations {
    private lateinit var binding: ActivityMainBinding


    private val firestore = FirebaseFirestore.getInstance()
    private val musicCollectionReference = firestore.collection("musics")

    override val musicListLiveData: MutableLiveData<List<MusicData>> by lazy {
        MutableLiveData<List<MusicData>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigationView.setupWithNavController(navController)

        }
    }


    override fun addMusicData(musicData: MusicData) {
        musicCollectionReference.add(musicData)
            .addOnSuccessListener {
                    documentReference ->
                val createdMusicId = documentReference.id
                musicData.id = createdMusicId
                documentReference.set(musicData)
                    .addOnSuccessListener {
                        Log.d("MainApp", "DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener {
                        Log.w("MainApp", "Error writing document", it)
                    }
            }
            .addOnFailureListener {
                Log.w("MainApp", "Error adding document", it)
            }
    }


    override fun updateMusicData(musicData: MusicData) {
        musicCollectionReference.document(musicData.id).set(musicData)
            .addOnSuccessListener {
                Log.d("MainApp", "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener {
                Log.w("MainApp", "Error updating document", it)
            }
    }

    override fun deleteMusicData(musicData: MusicData) {
        if(musicData.id.isBlank()) {
            Log.d("MainApp", "DocumentSnapshot id is blank!")
            return
        }

        musicCollectionReference.document(musicData.id).delete()
            .addOnSuccessListener {
                Log.d("MainApp", "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener {
                Log.w("MainApp", "Error deleting document", it)
            }
    }

    override fun observeMusicChanges() {
        musicCollectionReference.addSnapshotListener { snapshot, e ->
            if(e != null) {
                Log.w("MainApp", "Listen failed", e)
            }

            val musics = snapshot?.toObjects(MusicData::class.java)
            if(musics != null) {
                musicListLiveData.postValue(musics)
            }
        }
    }
}