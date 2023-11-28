package com.example.fp_test.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.fp_test.R
import com.example.fp_test.databinding.ItemMusicBinding
typealias clickUpdate = (String) -> Unit
typealias clickDelete = (String) -> Unit
class MusicAdapter(private val musicList: List<MusicData>,
    private val clickUpdate: clickUpdate,
    private val clickDelete: clickDelete)
    : RecyclerView.Adapter<MusicAdapter.ItemMusicViewHolder>() {

    inner class ItemMusicViewHolder(private val binding: ItemMusicBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(musicData: MusicData) {
            binding.apply {
                songTitle.text = musicData.songTitle
                songArtistAndAlbum.text = musicData.songArtist + " - " + musicData.songAlbum
                menu.setOnClickListener {
                    val popupMenu = PopupMenu(root.context, it)
                    popupMenu.inflate(R.menu.popup_menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.update -> {
                                clickUpdate(musicData.id)
                                true
                            }
                            R.id.delete -> {
                                clickDelete(musicData.id)
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMusicViewHolder {
        val binding = ItemMusicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemMusicViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: ItemMusicViewHolder, position: Int) {
        holder.bind(musicList[position])
    }


}