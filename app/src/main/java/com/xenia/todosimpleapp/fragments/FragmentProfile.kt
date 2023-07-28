package com.xenia.todosimpleapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xenia.todosimpleapp.databinding.FragmentProfileBinding

class FragmentProfile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        database = Firebase.database("https://todosimpleapp-a5de8-default-rtdb.europe-west1.firebasedatabase.app/").reference
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userUid = auth.currentUser?.uid
        if (userUid != null) {
            database.child("users").child(userUid).get().addOnSuccessListener {
                val userName = it.child("username").value as String
                binding.editTextProfile.setText(userName)
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }
        binding.profileSignOut.setOnClickListener {
            auth.signOut()
            val action = FragmentProfileDirections.actionFragmentProfileToFragmentSignUp()
            view.findNavController().navigate(action)
        }
        binding.returnToMain.setOnClickListener {
            val changedText = binding.editTextProfile.text.toString()
            if (userUid != null) {
                database.child("users").child(userUid).child("username").setValue(changedText)
            }
            val action = FragmentProfileDirections.actionFragmentProfileToFragmentMain()
            view.findNavController().navigate(action)
        }

    }

}