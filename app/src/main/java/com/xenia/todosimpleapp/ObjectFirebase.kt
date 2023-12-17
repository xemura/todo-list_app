package com.xenia.todosimpleapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object ObjectFirebase {
    var auth: FirebaseAuth = Firebase.auth
    var firebaseDatabase : DatabaseReference = Firebase.database("https://todosimpleapp-a5de8-default-rtdb.europe-west1.firebasedatabase.app/").reference
    var userUid = auth.currentUser?.uid
}