package com.xenia.todosimpleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.xenia.todosimpleapp.R
import com.xenia.todosimpleapp.databinding.FragmentRecoveryPasswordBinding

class FragmentRecoveryPassword : Fragment() {
    private var _binding: FragmentRecoveryPasswordBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoveryPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendCode.setOnClickListener {
            if (binding.tietRecoveryPasswordEmail.text.toString().isNotEmpty()) {
                binding.tvBtnReturnToSignIn.visibility = View.VISIBLE
                binding.btnSendCode.setText(R.string.link_sent)
                val emailToResetPassword = binding.tietRecoveryPasswordEmail.text.toString()
                firebaseAuth.sendPasswordResetEmail(emailToResetPassword).addOnSuccessListener {
                    //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(activity, "Empty field is not allowed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvBtnReturnToSignIn.setOnClickListener {
            val action = FragmentRecoveryPasswordDirections.actionFragmentRecoveryPasswordToFragmentSignIn2()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}