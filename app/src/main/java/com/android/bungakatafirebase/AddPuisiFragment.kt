package com.android.bungakatafirebase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.android.bungakatafirebase.databinding.FragmentAddPuisiBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddPuisiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPuisiFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAddPuisiBinding

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
        binding = FragmentAddPuisiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnSimpan.setOnClickListener {
                val judul = txtTitle.text.toString()
                val penulis = txtWriter.text.toString()
                val puisi = txtPuisi.text.toString()

                add(
                    Puisi(
                        judul = judul,
                        penulis = penulis,
                        puisi = puisi
                    )
                )

                txtTitle.setText("")
                txtWriter.setText("")
                txtPuisi.setText("")

                Toast.makeText(requireContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                findNavController().navigate(AddPuisiFragmentDirections.actionAddPuisiFragmentToPuisiFragmet())
            }
        }

    }

    private fun add(puisi: Puisi){
        puisiCollectionRef.add(puisi).addOnFailureListener{
            Log.d("MainActivity", "Error adding budget : ")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddPuisiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddPuisiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}