package com.xenia.todosimpleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xenia.todosimpleapp.databinding.FragmentOnBoardBinding


class FragmentOnBoard : Fragment() {
    private lateinit var binding: FragmentOnBoardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardBinding.inflate(layoutInflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = FragmentOnBoardDirections.actionFragmentOnBoardToFragmentMain()
            view.findNavController().navigate(action)
        }

        binding.btnOnBoard.setOnClickListener {
            val action = FragmentOnBoardDirections.actionFragmentOnBoardToFragmentSignUp()
            view.findNavController().navigate(action)
        }
    }
}