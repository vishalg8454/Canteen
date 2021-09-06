package com.vishal.canteen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vishal.canteen.adapter.OrderItemAdapter
import com.vishal.canteen.databinding.FragmentOrderBinding
import com.vishal.canteen.model.OrderModel
import com.vishal.canteen.viewmodel.MainAcivityViewModel

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel: MainAcivityViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private var localUid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
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
        }
    }

    private fun recycler() {
        val query = db.collection("orders").whereEqualTo("user",localUid)

        val options = FirestoreRecyclerOptions.Builder<OrderModel>()
            .setQuery(query, OrderModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = OrderItemAdapter(options)
        adapter.setUid(localUid)

        binding.orderrecyclerview.layoutManager = LinearLayoutManager(this.context)
        binding.orderrecyclerview.adapter = adapter
    }
}