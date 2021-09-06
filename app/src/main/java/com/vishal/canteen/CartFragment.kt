package com.vishal.canteen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vishal.canteen.adapter.CartItemAdapter
import com.vishal.canteen.databinding.FragmentCartBinding
import com.vishal.canteen.model.ItemModel
import com.vishal.canteen.viewmodel.MainAcivityViewModel
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: MainAcivityViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private var totalAmount = 0L
    private var localUid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        db = Firebase.firestore

        if (viewModel.authh.value!!.currentUser != null) {
            localUid = viewModel.authh.value!!.currentUser!!.uid
        }

        viewModel.authh.observe(viewLifecycleOwner, Observer {
            if (it.currentUser != null) {
                localUid = it.currentUser!!.uid
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (localUid != "") {
            recycler()
            fetchPrice()
        }
        btn_order.setOnClickListener {
            if (totalAmount > 0) {
                progressBar.visibility = View.VISIBLE
                var list = mutableListOf<Pair<String, Long>>()
                var itemsList: String = ""
                db.collection("users").document(localUid).collection("cart")
                    .get()
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            for (document in result.result!!) {
                                totalAmount += document["price"] as Long
//                                list.add(Pair(document["itemName"] as String, totalAmount))
                                itemsList += document["itemName"] as String
                                itemsList += ", "
                                Log.i("logcat", "amoiunt")
                            }
                        }
                        Log.i("logcat", list.toString())
                        db.collection("orders").document()
                            .set(
                                hashMapOf(
//                                    "items" to list,
                                    "items" to itemsList,
                                    "status" to "ordered",
                                    "totalAmount" to totalAmount,
                                    "user" to localUid
                                )
                            ).addOnCompleteListener {
                                Toast.makeText(
                                    this.context,
                                    "Order Placed Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                db.collection("users").document(localUid).collection("cart").get()
                                    .addOnSuccessListener { itr ->
                                        for (i in itr) {
                                            var ref = i.reference
                                            ref.delete()
                                        }
                                    }
                            }
                        progressBar.visibility = View.INVISIBLE
                    }
            }
        }
    }

    private fun fetchPrice() {
        db.collection("users").document(localUid).collection("cart")
            .get()
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    for (document in result.result!!) {
                        totalAmount += document["price"] as Long
                        Log.i("logcat", "amoiunt")
                    }
                }

                btn_order.text = "₹" + totalAmount.toString() + " Order"
                Log.i("logcat", totalAmount.toString())
            }
    }

    private fun recycler() {
        val query = db.collection("users").document(localUid).collection("cart")

        val options = FirestoreRecyclerOptions.Builder<ItemModel>()
            .setQuery(query, ItemModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = CartItemAdapter(options)
        adapter.setUid(localUid)

        binding.cartrecyclerview.layoutManager = LinearLayoutManager(this.context)
        binding.cartrecyclerview.adapter = adapter
    }
}