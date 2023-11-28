package com.example.fp_test

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fp_test.databinding.FragmentLyricsBinding
import com.example.fp_test.recyclerView.MusicData


class LyricsFragment : Fragment() {

    private lateinit var binding: FragmentLyricsBinding
    private var mainActivityOperations: FirebaseOperations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLyricsBinding.bind(inflater.inflate(R.layout.fragment_lyrics, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding) {

            submit.setOnClickListener {
                val musicData = MusicData(
                    songTitle = songName.text.toString(),
                    songArtist = songArtist.text.toString(),
                    songAlbum = songAlbum.text.toString(),
                    songLyrics = songLyrics.text.toString(),
                )
                if(arguments?.get("updatingID") == null) {
                    mainActivityOperations?.addMusicData(musicData)
                    findNavController().navigateUp()
                }
            }
        }

        if(arguments?.get("updatingID") != null) {
            val musicId = LyricsFragmentArgs.fromBundle(requireArguments()).updatingID

            mainActivityOperations?.musicListLiveData?.observe(viewLifecycleOwner) { musicData ->
                val music = musicData.find { music -> music.id == musicId }
                if(music != null) {
                    with(binding) {
                        songName.setText(music.songTitle)
                        songArtist.setText(music.songArtist)
                        songAlbum.setText(music.songAlbum)
                        songLyrics.setText(music.songLyrics)

                        submit.setOnClickListener {
                            val updatedMusicData = MusicData(
                                id = musicId,
                                songTitle = songName.text.toString(),
                                songArtist = songArtist.text.toString(),
                                songAlbum = songAlbum.text.toString(),
                                songLyrics = songLyrics.text.toString(),
                            )
                            mainActivityOperations?.updateMusicData(updatedMusicData)
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity) {
            mainActivityOperations = context
        } else {
            throw RuntimeException("$context must implement FirebaseOperations")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivityOperations = null
    }
}