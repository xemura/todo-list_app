package com.xenia.todosimpleapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.xenia.todosimpleapp.FragmentCommunication
import com.xenia.todosimpleapp.firebase.ObjectFirebase
import com.xenia.todosimpleapp.R
import com.xenia.todosimpleapp.adapter.RecyclerViewAdapter
import com.xenia.todosimpleapp.data.UserUseCases
import com.xenia.todosimpleapp.databinding.FragmentMainBinding


class FragmentMain : Fragment(), FragmentCommunication {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvAdapter: RecyclerViewAdapter
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialog : Dialog
    private var tasksList : ArrayList<String> = arrayListOf()
    private val userUid = ObjectFirebase.userUid!!
    private val userUseCases = UserUseCases()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        database = ObjectFirebase.database
        recyclerView = binding.rcViewMain

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or
                    ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                userUseCases.getUserTasksList().child(tasksList[viewHolder.adapterPosition]).setValue(null)

                tasksList.removeAt(viewHolder.adapterPosition)

                userUseCases.getUserTasksList().get().addOnSuccessListener {
                        var progressCount = 0
                        it.children.forEach { item ->
                            val isChecked = item.value.toString().toBoolean()
                            if (isChecked) progressCount++
                        }
                        setProgressBarProgress(progressCount)
                    }.addOnFailureListener{
                        Toast.makeText(activity, "Error getting data", Toast.LENGTH_SHORT).show()
                    }

                rvAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                rvAdapter = RecyclerViewAdapter(tasksList, this@FragmentMain)
                recyclerView.adapter = rvAdapter

                setCountTasksToday(tasksList.size)
            }
        }).attachToRecyclerView(recyclerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Tag", "Main ${ObjectFirebase.userUid} | $userUid")

        database.child("users").child(userUid).get().addOnSuccessListener {
            val userName = it.child("username").value as String
            val welcomeText = "Hello, $userName!"
            binding.welcomeText1.text = welcomeText
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        userUseCases.getUserTasksList().get().addOnSuccessListener {
                tasksList.clear()
                var progressCount : Int = 0
                it.children.forEach { item ->
                    val task = item.key.toString()
                    val isChecked = item.value.toString().toBoolean()
                    if (isChecked) progressCount++
                    tasksList.add(task)
                }
                rvAdapter = RecyclerViewAdapter(tasksList, this)
                recyclerView.adapter = rvAdapter

                setCountTasksToday(tasksList.size)
                setProgressBarProgress(progressCount)
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        binding.cardViewMain.setOnClickListener {
            val action = FragmentMainDirections.actionFragmentMainToFragmentProfile()
            view.findNavController().navigate(action)
        }

        binding.fabAdd.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnAddDialog : Button = dialog.findViewById(R.id.btn_dialog)
        val textTask : TextInputEditText = dialog.findViewById(R.id.tiet_dialog)
        val btnCancelDialog : ImageButton = dialog.findViewById(R.id.btn_cancel)

        btnAddDialog.setOnClickListener {
            userUseCases.getUserTasksList().child(textTask.text.toString()).setValue(false)

            userUseCases.getUserTasksList().get().addOnSuccessListener {
                    tasksList.clear()
                    var progressCount = 0
                    it.children.forEach { item ->
                        val task = item.key.toString()
                        val isChecked = item.value.toString().toBoolean()
                        if (isChecked) progressCount++
                        tasksList.add(task)
                    }

                    setCountTasksToday(tasksList.size)
                    setProgressBarProgress(progressCount)

                    recyclerView.adapter = RecyclerViewAdapter(tasksList, this)

                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            dialog.cancel()
        }

        btnCancelDialog.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    override fun respond(countCheckedItems: Int) {
        setProgressBarProgress(countCheckedItems)
    }

    private fun setProgressBarProgress(countCheckedItems : Int) {
        when (countCheckedItems) {
            0 -> {
                binding.progressBar.progress = 0
                binding.progressInProcent.text = "0%"
            }
            tasksList.size -> {
                binding.progressBar.progress = 100
                binding.progressInProcent.text = "100%"
            }
            else -> {
                binding.progressBar.progress = (100/tasksList.size)*countCheckedItems
                val textInPercent = "${((100/tasksList.size)*countCheckedItems)}%"
                binding.progressInProcent.text = textInPercent
            }
        }
    }
    private fun setCountTasksToday(countTasks : Int) {
        val textCountTasks : String = if (countTasks == 1 || countTasks == 0) "You have $countTasks task today"
        else "You have $countTasks tasks today"
        binding.welcomeText2.text = textCountTasks
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
