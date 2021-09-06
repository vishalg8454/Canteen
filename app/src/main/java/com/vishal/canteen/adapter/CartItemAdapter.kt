package com.vishal.canteen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vishal.canteen.R
import com.vishal.canteen.model.ItemModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cart_item_view.view.*

private lateinit var db: FirebaseFirestore


class CartItemAdapter(options: FirestoreRecyclerOptions<ItemModel>) :
    FirestoreRecyclerAdapter<ItemModel, CartItemAdapter.ViewHolder>(options) {
    private var localUid = ""
    private var signedIn = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_view, parent, false)
        return ViewHolder(view)
    }

    fun setUid(uid: String) {
        this.localUid = uid
        if (uid == "")
            signedIn = false
    }

    override fun onBindViewHolder(
        holder: CartItemAdapter.ViewHolder,
        position: Int,
        item: ItemModel
    ) {
        val id = snapshots.getSnapshot(position).id
        var name = item.itemName.toString()
        var unit = item.unit
        var price = item.price.toString()
        val url = item.imgURL

        holder.itemView.tvName.text = name
        holder.itemView.tvPrice.text = price
        holder.itemView.tvUnitCart.text = unit

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_placeholder_24dp)
            .error(R.drawable.ic_launcher_foreground)
            .transform(CenterCrop(), RoundedCorners(8))

        Glide.with(holder.itemView.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(url)
            .into(holder.itemView.findViewById(R.id.ivItemImage))

        holder.itemView.delete.setOnClickListener {
            db.collection("users")
                .document(localUid)
                .collection("cart")
                .document(id)
                .delete()
        }
    }

    override fun startListening() {
        super.startListening()
        db = Firebase.firestore
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}