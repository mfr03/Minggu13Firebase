package com.example.fp_test

import androidx.lifecycle.MutableLiveData
import com.example.fp_test.recyclerView.MusicData

interface FirebaseOperations {

    val musicListLiveData: MutableLiveData<List<MusicData>>

    fun addMusicData(musicData: MusicData)
    fun updateMusicData(musicData: MusicData)
    fun deleteMusicData(musicData: MusicData)
    fun observeMusicChanges()
}