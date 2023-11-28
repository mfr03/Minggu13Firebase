package com.example.fp_test

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fp_test.databinding.FragmentHomeBinding
import com.example.fp_test.recyclerView.MusicAdapter
import com.example.fp_test.recyclerView.MusicData


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var mainActivityOperations: FirebaseOperations? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeMusic()
        getAllMusic()
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


    private fun getAllMusic() {
        mainActivityOperations?.observeMusicChanges()
    }

    private fun observeMusic() {
        mainActivityOperations?.musicListLiveData?.observe(viewLifecycleOwner) { musicData ->
            binding.recyclerView.apply {
                adapter = MusicAdapter(musicData.toMutableList(), {
                    val action = HomeFragmentDirections.actionHomeFragmentToLyricsFragment(musicData.find { music -> music.id == it }!!.id)
                    findNavController().navigate(action)

                }, {
                    mainActivityOperations?.deleteMusicData(musicData.find { music -> music.id == it }!!)
                })
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

}