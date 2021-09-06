package com.vishal.canteen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.vishal.canteen.model.OrderModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_view.view.*

private lateinit var db: FirebaseFirestore

    class FirestoreItemAdapter(options: FirestoreRecyclerOptions<ItemModel>) :
        FirestoreRecyclerAdapter<ItemModel, FirestoreItemAdapter.ViewHolder>(options) {
    private var localUid = ""
    private var signedIn = true
    private lateinit var context: Context

    fun setUid(uid: String) {
        this.localUid = uid
        if (uid == "")
            signedIn = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: ItemModel) {
        val id = snapshots.getSnapshot(position).id
        var name = item.itemName.toString()
        var unit = item.unit
        var price = item.price.toString()
        val url = item.imgURL

        holder.itemView.tv_itemname.text = name
        holder.itemView.tv_unit.text = unit
        holder.itemView.tv_price.text = price

        val docRefCart = db.collection("users").document(localUid)
            .collection("cart").document(id)

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_placeholder_24dp)
            .error(R.drawable.ic_launcher_foreground)
            .transform(CenterCrop(), RoundedCorners(8))

        Glide.with(holder.itemView.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(url)
            .into(holder.itemView.findViewById(R.id.imageView))


        if (signedIn) {
            docRefCart.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result?.exists()!!) {
                            holder.itemView.addToCart.visibility = View.INVISIBLE
                            holder.itemView.inCart.visibility = View.VISIBLE
                        }
                    }
                }
        }

        holder.itemView.addToCart.setOnClickListener {
            if (signedIn) {
                docRefCart.set(hashMapOf("imgURL" to url, "itemName" to name,"price" to item.price, "unit" to unit))
                holder.itemView.addToCart.visibility = View.INVISIBLE
                holder.itemView.inCart.visibility = View.VISIBLE
            } else {
                Toast.makeText(context, "Sign in to add to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun startListening() {
        super.startListening()
        db = Firebase.firestore
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}