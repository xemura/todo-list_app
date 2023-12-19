package com.xenia.todosimpleapp.data

import com.google.firebase.database.DatabaseReference
import com.xenia.todosimpleapp.firebase.ObjectFirebase

class UserUseCases {
    fun getUser() : DatabaseReference {
        return ObjectFirebase.database.child("users").child(ObjectFirebase.userUid!!)
    }

    fun getUserTasksList() : DatabaseReference {
        return ObjectFirebase.database.child("users").child(ObjectFirebase.userUid!!).child("tasksList")
    }
}