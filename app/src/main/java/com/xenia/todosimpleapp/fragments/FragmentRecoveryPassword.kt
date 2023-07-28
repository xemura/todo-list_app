package com.xenia.todosimpleapp.fragments

import android.os.Bundle
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
import com.xenia.todosimpleapp.databinding.FragmentRecoveryPasswordBinding

class FragmentRecoveryPassword : Fragment() {
    private lateinit var binding: FragmentRecoveryPasswordBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecoveryPasswordBinding.inflate(layoutInflater, container, false)
        database = Firebase.database("https://todosimpleapp-a5de8-default-rtdb.europe-west1.firebasedatabase.app/").reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendCode.setOnClickListener {
            if (binding.tietRecoveryPasswordEmail.text.toString().isNotEmpty()) {
                // change password in auth
                binding.tvRp.visibility = View.VISIBLE
                binding.btnSendCode.setText(R.string.link_sent)
                val emailToResetPassword = binding.tietRecoveryPasswordEmail.text.toString()
                firebaseAuth.sendPasswordResetEmail(emailToResetPassword).addOnSuccessListener {
                    //Toast.makeText(activity, "ddd", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(activity, "Empty field is not allowed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvRp.setOnClickListener {
            val action = FragmentRecoveryPasswordDirections.actionFragmentRecoveryPasswordToFragmentSignIn2()
            view.findNavController().navigate(action)
        }
    }
}