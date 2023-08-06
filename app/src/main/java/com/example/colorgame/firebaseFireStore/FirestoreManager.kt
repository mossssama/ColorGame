package com.example.colorgame.firebaseFireStore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirestoreManager(private val db: FirebaseFirestore) {

    private val scoreListeners: MutableMap<String, ListenerRegistration> = mutableMapOf()
    private val countDownListeners: MutableMap<String, ListenerRegistration> = mutableMapOf()

    fun addUserIfDoesNotExist(playerName: String, initScore: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users").document(playerName).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) { Log.i("TAG", "Document with playerName $playerName already exists") }
                else {
                    // If document doesn't exist, create it with the initial score field
                    db.collection("users").document(playerName)
                        .set(initScore) // Set the initial score map here
                        .addOnSuccessListener {
                            Log.i("TAG", "Document with playerName $playerName created with initial score.")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.i("TAG", "Error adding document", e)
                            onFailure(e)
                        }
                }
            } else {
                Log.i("TAG", "Error checking document existence", task.exception)
                onFailure(task.exception!!)
            }
        }
    }

    fun initPlayerKeyValuePair(playerName: String, updatedpair: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.update(updatedpair)
            .addOnSuccessListener {
                Log.i("TAG", "initScore field added to the document.")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.i("TAG", "Error adding initScore field to the document.", e)
                onFailure(e)
            }
    }

    fun checkIfUserExists(playerName: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                val exists = document != null && document.exists()
                onSuccess(exists)
            } else {
                onFailure(task.exception!!)
            }
        }
    }

    fun incrementScore(playerName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val currentScore = document.getLong("score")?.toInt() ?: 0
                    val newScore = currentScore + 1

                    val updateMap = hashMapOf("score" to newScore)

                    documentReference.update(updateMap as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.i("TAG", "Score for $playerName incremented to: $newScore")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.i("TAG", "Error updating score", e)
                            onFailure(e)
                        }
                } else {
                    Log.i("TAG", "Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Log.i("TAG", "Error checking document existence", task.exception)
                onFailure(task.exception!!)
            }
        }
    }


    fun updateCountDown(playerName: String, newCountDownValue: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val updateMap = hashMapOf("countDown" to newCountDownValue)

                    documentReference.update(updateMap as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.i("TAG", "CountDown for $playerName updated to: $newCountDownValue")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.i("TAG", "Error updating countDown", e)
                            onFailure(e)
                        }
                } else {
                    Log.i("TAG", "Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Log.i("TAG", "Error checking document existence", task.exception)
                onFailure(task.exception!!)
            }
        }
    }


    fun decrementScore(playerName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val currentScore = document.getLong("score")?.toInt() ?: 0
                    val newScore = currentScore - 1

                    val updateMap = hashMapOf("score" to newScore)

                    documentReference.update(updateMap as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.i("TAG", "Score for $playerName incremented to: $newScore")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.i("TAG", "Error updating score", e)
                            onFailure(e)
                        }
                } else {
                    Log.i("TAG", "Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Log.i("TAG", "Error checking document existence", task.exception)
                onFailure(task.exception!!)
            }
        }
    }

    fun setScoreToZero(playerName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val updateMap = hashMapOf("score" to 0)

                    documentReference.update(updateMap as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.i("TAG", "Score for $playerName set to zero.")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.i("TAG", "Error updating score", e)
                            onFailure(e)
                        }
                } else {
                    Log.i("TAG", "Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Log.i("TAG", "Error checking document existence", task.exception)
                onFailure(task.exception!!)
            }
        }
    }

    fun setCountDownToHundred(playerName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val newCountDown = 100

                    val updateMap = hashMapOf("countDown" to newCountDown)

                    documentReference.update(updateMap as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.i("TAG", "CountDown for $playerName is: $newCountDown")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.i("TAG", "Error updating countDown", e)
                            onFailure(e)
                        }
                } else {
                    Log.i("TAG", "Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Log.i("TAG", "Error checking document existence", task.exception)
                onFailure(task.exception!!)
            }
        }
    }


    fun listenToScoreChanges(playerName: String, listener: (Int) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        val scoreListener = documentReference.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Log.e("TAG", "Error listening to changes", e)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val score = documentSnapshot.getLong("score")?.toInt() ?: 0
                listener(score)
            }
        }

        scoreListeners[playerName] = scoreListener
    }

    fun listenToCountDownChanges(playerName: String, listener: (Int) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        val countDownListener = documentReference.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Log.e("TAG", "Error listening to changes", e)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val countDown = documentSnapshot.getLong("countDown")?.toInt() ?: 0
                listener(countDown)
            }
        }

        countDownListeners[playerName] = countDownListener
    }

    fun readScore(playerName: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val score = document.getLong("score")?.toInt() ?: 0
                    onSuccess(score)
                } else {
                    Log.i("TAG", "Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Log.i("TAG", "Error checking document existence", task.exception)
                onFailure(task.exception!!)
            }
        }
    }

    fun removeAllScoreListeners() {
        for (listener in scoreListeners.values) { listener.remove() }
        scoreListeners.clear()
    }

    fun removeAllCountDownListeners() {
        for (listener in countDownListeners.values) { listener.remove() }
        countDownListeners.clear()
    }

}
