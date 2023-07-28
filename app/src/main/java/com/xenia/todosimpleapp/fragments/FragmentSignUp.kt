package com.xenia.todosimpleapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xenia.todosimpleapp.R
import com.xenia.todosimpleapp.UserData
import com.xenia.todosimpleapp.databinding.FragmentSignUpBinding

class FragmentSignUp : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        database = Firebase.database("https://todosimpleapp-a5de8-default-rtdb.europe-west1.firebasedatabase.app/").reference
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
                            // save in firebase realtime database
                            val user = UserData(name, email, tasksList)
                            val uid = firebaseAuth.currentUser?.uid

                            if (uid != null) {
                                database.child("users").child(uid).setValue(user)
                            }

                            val action = FragmentSignUpDirections.actionFragmentSignUpToFragmentMain()
                            view.findNavController().navigate(action)
                        }
                        else {
                            Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(activity, "Passwords does`t confirm", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(activity, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}