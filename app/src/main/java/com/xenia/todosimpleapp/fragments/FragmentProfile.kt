package com.xenia.todosimpleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
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
        val userUid = ObjectFirebase.userUid!!

        ObjectFirebase.database.child("users").child(userUid).get().addOnSuccessListener {
            val userName = it.child("username").value as String
            binding.editTextProfile.setText(userName)
        }.addOnFailureListener {
            Toast.makeText(activity, "Error getting data", Toast.LENGTH_SHORT).show()
        }

        binding.profileSignOut.setOnClickListener {
            ObjectFirebase.auth.signOut()
            val action = FragmentProfileDirections.actionFragmentProfileToFragmentSignUp()
            view.findNavController().navigate(action)
        }

        binding.returnToMain.setOnClickListener {
            val setName = binding.editTextProfile.text.toString()
            ObjectFirebase.database.child("users").child(userUid).child("username").setValue(setName)
            val action = FragmentProfileDirections.actionFragmentProfileToFragmentMain()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}