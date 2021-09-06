package com.vishal.canteen.adapter

//import com.example.android.firebase.ShopFragmentDirections
//import com.example.android.firebase.ui.ShopFragmentDirections
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.vishal.canteen.HomeFragmentDirections
import com.vishal.canteen.R
import com.vishal.canteen.model.CategoryModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.category_view.view.*

var LOGCAT = "logcat"

class CategoryItemAdapter(options: FirestoreRecyclerOptions<CategoryModel>) :
    FirestoreRecyclerAdapter<CategoryModel, CategoryItemAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: CategoryModel) {
        val id = snapshots.getSnapshot(position).id

        holder.itemView.textView.text = item.categoryName
        holder.itemView.findViewById<ImageView>(R.id.imageView2).setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToListFragment2(id)
            it.findNavController()
                .navigate(action)
        }

        val url = item.imgURL
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_placeholder_24dp)
            .error(R.drawable.ic_launcher_foreground)
            .transform(CenterCrop(), RoundedCorners(8))

        Glide.with(holder.itemView.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(url)
            .into(holder.itemView.findViewById(R.id.imageView2))

    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}