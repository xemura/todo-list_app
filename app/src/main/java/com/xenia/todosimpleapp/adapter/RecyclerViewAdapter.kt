package com.xenia.todosimpleapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xenia.todosimpleapp.FragmentCommunication
import com.xenia.todosimpleapp.R
import com.xenia.todosimpleapp.data.UserUseCases
import com.xenia.todosimpleapp.databinding.FragmentMainBinding
import com.xenia.todosimpleapp.firebase.ObjectFirebase


class RecyclerViewAdapter(private val tasks: List<String>, private val mListener: FragmentCommunication) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    private lateinit var binding: FragmentMainBinding
    private val userUseCases = UserUseCases()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox : CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = FragmentMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = tasks.size

    private fun getCountChecked() : Int {
        var progressCount = 0
        userUseCases.getUserTasksList().get().addOnSuccessListener {
                it.children.forEach { item ->
                    val isChecked = item.value.toString().toBoolean()
                    if (isChecked) progressCount++
                }
                mListener.respond(progressCount)

            }.addOnFailureListener{
                Log.e("Error", "Error getting data", it)
            }
        return progressCount
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.checkBox.text = tasks[position]
        userUseCases.getUserTasksList().child(holder.checkBox.text.toString()).get().addOnSuccessListener {
                holder.checkBox.isChecked = it.value.toString().toBoolean()
        }
        holder.checkBox.setOnClickListener {
            userUseCases.getUserTasksList().child(holder.checkBox.text.toString()).setValue(holder.checkBox.isChecked)
            getCountChecked()
        }
    }


}