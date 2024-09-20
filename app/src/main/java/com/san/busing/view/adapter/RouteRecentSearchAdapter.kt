package com.san.busing.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemRouteRecentSearchBinding
import com.san.busing.domain.modelimpl.RouteRecentSearchModels
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class RouteRecentSearchAdapter(
    private val items: RouteRecentSearchModels,
    private val itemClickEventListener: ItemClickEventListener,
    private val activity: Activity
) : RecyclerView.Adapter<RouteRecentSearchAdapter.RouteRecentSearchViewHolder>() {

    inner class RouteRecentSearchViewHolder(
        private val binding: ItemRouteRecentSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            loadContent(position)
            setBackground(position)
            setContentColor(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtRouteRecentSearchName.text = items.get(position).name
            binding.btnDeleteRecentSearch.visibility = if (items.get(position).bookMark) View.GONE else View.VISIBLE
        }

        private fun setBackground(position: Int) {
            val background = ContextCompat.getDrawable(activity, Utils.getBackgroundByBookMarkStatus(items.get(position).bookMark))
            binding.clRouteRecentSearchItem.background = background
        }

        private fun setContentColor(position: Int) {
            val color = ContextCompat.getColor(activity, Utils.getColorByRouteType(items.get(position).type))
            binding.txtRouteRecentSearchName.setTextColor(color)
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteRecentSearchItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position) }
            binding.btnDeleteRecentSearch.setOnClickListener {
                itemClickEventListener.onDeleteButtonClickListener(position) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RouteRecentSearchViewHolder {
        val binding = ItemRouteRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return RouteRecentSearchViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: RouteRecentSearchViewHolder, position: Int) {
        holder.bind(position)
    }
}