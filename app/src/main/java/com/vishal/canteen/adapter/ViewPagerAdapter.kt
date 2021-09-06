package com.vishal.canteen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vishal.canteen.R


class ViewPagerAdapter(
    private val texts: List<String>
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.banner_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return texts.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curText = texts[position]
         Glide.with(holder.itemView.context)
             .load(curText).centerCrop()
             .placeholder(R.drawable.ic_placeholder_24dp)
             .error(R.drawable.ic_placeholder_24dp)
             .into(holder.itemView.findViewById(R.id.imageView4))
    }
}