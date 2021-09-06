package com.vishal.canteen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vishal.canteen.R
import com.vishal.canteen.model.OrderModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.order_item_view.view.*

private lateinit var db: FirebaseFirestore


class OrderItemAdapter(options: FirestoreRecyclerOptions<OrderModel>) :
    FirestoreRecyclerAdapter<OrderModel, OrderItemAdapter.ViewHolder>(options) {
    private var localUid = ""
    private var signedIn = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item_view, parent, false)
        return ViewHolder(view)
    }

    fun setUid(uid: String) {
        this.localUid = uid
        if (uid == "")
            signedIn = false
    }

    override fun onBindViewHolder(
        holder: OrderItemAdapter.ViewHolder,
        position: Int,
        item: OrderModel
    ) {
        val id = snapshots.getSnapshot(position).id
        val totalAmount = "₹" + item.totalAmount.toString()
        val totalItems = item.items

        holder.itemView.tv_uid.text = id
        holder.itemView.tv_amount.text = totalAmount
        holder.itemView.tv_items.text = totalItems
    }

    override fun startListening() {
        super.startListening()
        db = Firebase.firestore
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}