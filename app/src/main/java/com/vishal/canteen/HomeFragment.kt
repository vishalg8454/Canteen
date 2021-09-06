package com.vishal.canteen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vishal.canteen.adapter.CategoryItemAdapter
import com.vishal.canteen.adapter.ViewPagerAdapter
import com.vishal.canteen.databinding.FragmentCartBinding
import com.vishal.canteen.databinding.FragmentHomeBinding
import com.vishal.canteen.model.CategoryModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = Firebase.firestore
        auth = Firebase.auth
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        categoryRecycler()

        var link1: String;
        var link2: String;
        var link3: String

        db.collection("banner").document("banner").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result?.exists()!!) {
                        link1 = it.result?.get("link1") as String
                        link2 = it.result?.get("link2") as String
                        link3 = it.result?.get("link3") as String
                        var list = listOf(link1, link2, link3)
                        list = list.shuffled()
                        val adapter = ViewPagerAdapter(list)

                        binding.viewPager.adapter = adapter
                    }
                }
            }

        var name = "Welcome back "
        name += auth.currentUser?.displayName.toString()
        binding.tvWelcome.text = name

    }

    private fun categoryRecycler() {
        val query = db.collection("category")

        val options = FirestoreRecyclerOptions.Builder<CategoryModel>()
            .setQuery(query, CategoryModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = CategoryItemAdapter(options)

        category_recyler_view.layoutManager = GridLayoutManager(this.context, 3)
        category_recyler_view.adapter = adapter
    }
}