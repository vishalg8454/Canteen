package com.vishal.canteen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vishal.canteen.adapter.FirestoreItemAdapter
import com.vishal.canteen.databinding.FragmentListBinding
import com.vishal.canteen.model.ItemModel
import com.vishal.canteen.model.OrderModel
import com.vishal.canteen.viewmodel.MainAcivityViewModel

class ListFragment : Fragment() {

    private val viewModel: MainAcivityViewModel by activityViewModels()
    private lateinit var binding: FragmentListBinding
    private lateinit var db: FirebaseFirestore
    private var category = ""
    private var localUid = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
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
        category = arguments?.getString("category").toString()

        recycler()
    }

    private fun recycler() {
        val query = db.collection("items").whereEqualTo("category", category)

        val options = FirestoreRecyclerOptions.Builder<ItemModel>()
            .setQuery(query, ItemModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = FirestoreItemAdapter(options)
        adapter.setUid(localUid)

        binding.itemrecyclerview.layoutManager = GridLayoutManager(this.context, 2)
        binding.itemrecyclerview.adapter = adapter
    }
}