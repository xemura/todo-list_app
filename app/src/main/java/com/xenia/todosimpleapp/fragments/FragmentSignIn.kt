package com.xenia.todosimpleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.xenia.todosimpleapp.databinding.FragmentSignInBinding

class FragmentSignIn : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignUp.setOnClickListener {
            val action = FragmentSignInDirections.actionFragmentSignIn2ToFragmentSignUp()
            view.findNavController().navigate(action)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.tietSignInEmail.text.toString()
            val password = binding.tietSignInPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val action = FragmentSignInDirections.actionFragmentSignIn2ToFragmentMain()
                        view.findNavController().navigate(action)
                    }
                    else {
                        Toast.makeText(activity, "Incorrect username or password!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(activity, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvForgotPassword.setOnClickListener {
            val action = FragmentSignInDirections.actionFragmentSignIn2ToFragmentRecoveryPassword()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}