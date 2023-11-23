package com.android.bungakatafirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.bungakatafirebase.databinding.FragmentPuisiBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PuisiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PuisiFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentPuisiBinding
    private val firestore = FirebaseFirestore.getInstance()
    var updateId = ""
    private val puisiLiveData : MutableLiveData<List<Puisi>>
            by lazy {
                MutableLiveData<List<Puisi>>()
            }

    private val puisiCollectionRef  = firestore.collection("puisi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPuisiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observePuisi()
        getAllPuisi()

        with(binding){

            val adapter = PuisiAdapter(
                emptyList(), // Initial empty list or you can provide some default data
                { selectedPuisi ->
                    updateId = selectedPuisi.id
                    val title = selectedPuisi.judul
                    val writer = selectedPuisi.penulis
                    val content = selectedPuisi.puisi

                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("UPDATE_ID", updateId)
                    intent.putExtra("TITLE", title)
                    intent.putExtra("WRITER", writer)
                    intent.putExtra("CONTENT", content)
                    startActivity(intent)
                    updateId = ""
                },
                { selectedPuisi ->
                    updateId = selectedPuisi.id
                    val title = selectedPuisi.judul
                    val writer = selectedPuisi.penulis
                    val content = selectedPuisi.puisi

                    val intent = Intent(requireContext(), EditActivity::class.java)
                    intent.putExtra("UPDATE_ID", updateId)
                    intent.putExtra("TITLE", title)
                    intent.putExtra("WRITER", writer)
                    intent.putExtra("CONTENT", content)
                    startActivity(intent)

                    updateId = ""
                },
                { selectedPuisi ->
                    delete(selectedPuisi)
                    getAllPuisi()
                }
            )
            reycPuisi.apply {
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter = adapter
            }


        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PuisiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PuisiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getAllPuisi() {
        puisiCollectionRef.get().addOnSuccessListener { snapshot ->
            val puisis = snapshot.documents.map { document ->
                Puisi(
                    document.id,
                    document.getString("judul") ?: "",
                    document.getString("penulis") ?: "",
                    document.getString("puisi") ?: ""
                )
            }
            puisiLiveData.postValue(puisis)
            Log.d("PuisiFragment", "Retrieved data: $puisis")
        }.addOnFailureListener { exception ->
            Log.e("PuisiFragment", "Error getting data: $exception")
        }


    }


    private fun observePuisi() {
        puisiLiveData.observe(viewLifecycleOwner) { puisi ->
            // Get the adapter from the RecyclerView and update its data
            (binding.reycPuisi.adapter as? PuisiAdapter)?.updateData(puisi)
        }
    }

    private fun delete(puisi: Puisi){

        val id = puisi.id
        puisiCollectionRef.document(id).delete().addOnFailureListener {
            Log.d("MainActivity", "Error deleting budget")
        }
    }

    override fun onResume() {
        super.onResume()
        observePuisi()
        getAllPuisi()
    }

}