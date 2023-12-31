package com.xenia.todosimpleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.xenia.todosimpleapp.data.UserData
import com.xenia.todosimpleapp.databinding.FragmentSignUpBinding
import com.xenia.todosimpleapp.firebase.ObjectFirebase

class FragmentSignUp : Fragment() {
    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSignIn.setOnClickListener {
            val action = FragmentSignUpDirections.actionFragmentSignUpToFragmentSignIn2()
            view.findNavController().navigate(action)
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.tietName.text.toString()
            val email = binding.tietSignUpEmail.text.toString()
            val createdPassword = binding.tietSignUpPasswordCreate.text.toString()
            val confirmedPassword = binding.tietSignUpConfirmPassword.text.toString()
            val tasksList = arrayListOf<String>()

            if (name.isNotEmpty() && email.isNotEmpty() && createdPassword.isNotEmpty() && confirmedPassword.isNotEmpty()) {
                if (createdPassword == confirmedPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, createdPassword).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = UserData(name, email, tasksList)
                            val uid = ObjectFirebase.userUid!!

                            ObjectFirebase.database.child("users").child(uid).setValue(user)

                            val action = FragmentSignUpDirections.actionFragmentSignUpToFragmentMain()
                            view.findNavController().navigate(action)
                        }
                        else {
                            Toast.makeText(activity, "Failed to create an account", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(activity, "Passwords does not confirm", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(activity, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}