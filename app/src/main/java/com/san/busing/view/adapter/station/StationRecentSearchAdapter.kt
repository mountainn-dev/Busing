package com.san.busing.view.adapter.station

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemStationRecentSearchBinding
import com.san.busing.domain.modelimpl.station.StationRecentSearchModels
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class StationRecentSearchAdapter(
    private val items: StationRecentSearchModels,
    private val itemClickEventListener: ItemClickEventListener,
    private val activity: Activity,
) : RecyclerView.Adapter<StationRecentSearchAdapter.StationRecentSearchViewHolder>() {
    inner class StationRecentSearchViewHolder(
        private val binding: ItemStationRecentSearchBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setBackground(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtStationRecentSearchName.text = items.get(position).name
            binding.btnDeleteRecentSearch.visibility = if (items.get(position).bookMark) View.GONE else View.VISIBLE
        }

        private fun setBackground(position: Int) {
            val background =
                ContextCompat.getDrawable(
                    activity,
                    Utils.getBackgroundByBookMarkStatus(items.get(position).bookMark),
                )
            binding.clStationRecentSearchItem.background = background
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clStationRecentSearchItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
            binding.btnDeleteRecentSearch.setOnClickListener {
                itemClickEventListener.onDeleteButtonClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StationRecentSearchAdapter.StationRecentSearchViewHolder {
        val binding =
            ItemStationRecentSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )

        return StationRecentSearchViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(
        holder: StationRecentSearchAdapter.StationRecentSearchViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }
}
