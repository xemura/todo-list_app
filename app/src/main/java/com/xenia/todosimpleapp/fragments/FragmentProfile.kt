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
import com.xenia.todosimpleapp.firebase.ObjectFirebase
import com.xenia.todosimpleapp.databinding.FragmentProfileBinding

class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userUid = ObjectFirebase.userUid
        Log.d("Tag", "Profile ${ObjectFirebase.userUid} | $userUid")
        if (userUid != null) {
            ObjectFirebase.database.child("users").child(userUid).get().addOnSuccessListener {
                val userName = it.child("username").value as String
                binding.editTextProfile.setText(userName)
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }
        binding.profileSignOut.setOnClickListener {
            ObjectFirebase.auth.signOut()
            val action = FragmentProfileDirections.actionFragmentProfileToFragmentSignUp()
            view.findNavController().navigate(action)
        }
        binding.returnToMain.setOnClickListener {
            val changedText = binding.editTextProfile.text.toString()
            if (userUid != null) {
                ObjectFirebase.database.child("users").child(userUid).child("username").setValue(changedText)
            }
            val action = FragmentProfileDirections.actionFragmentProfileToFragmentMain()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}