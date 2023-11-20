package com.newOs.colorCraze.cloudFirestore

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.newOs.colorCraze.R
import timber.log.Timber

class FirestoreManager(private val db: FirebaseFirestore) {

    private val scoreListeners: MutableMap<String, ListenerRegistration> = mutableMapOf()
    private val countDownListeners: MutableMap<String, ListenerRegistration> = mutableMapOf()
    private val startPlayingListeners: MutableMap<String, ListenerRegistration> = mutableMapOf()

    fun addUserIfDoesNotExist(context: Context, playerName: String, initScore: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users").document(playerName).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) { 
                    Toast.makeText(context,context.getString(R.string.playerAlreadyExists),Toast.LENGTH_LONG).show()
                    Timber.i("Document with playerName $playerName already exists")
                }
                else {
                    // If document doesn't exist, create it with the initial score field
                    db.collection("users").document(playerName)
                        .set(initScore) // Set the initial score map here
                        .addOnSuccessListener {
                            Timber.i("Document with playerName $playerName created with initial score.")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error adding document")
                            onFailure(e)
                        }
                }
            } else {
                Timber.i("Error checking document existence")
                onFailure(task.exception!!)
            }
        }
    }

    fun initPlayerCollection(playerName: String, updates: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.update(updates)
            .addOnSuccessListener {
                Timber.i("initScore field added to the document.")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.i("Error adding initScore field to the document.")
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
                            Timber.i("Score for $playerName incremented to: $newScore")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error updating score")
                            onFailure(e)
                        }
                } else {
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i("Error checking document existence")
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
                            Timber.i("CountDown for $playerName updated to: $newCountDownValue")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error updating countDown")
                            onFailure(e)
                        }
                } else {
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i("Error checking document existence")
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
                            Timber.i("Score for $playerName incremented to: $newScore")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error updating score")
                            onFailure(e)
                        }
                } else {
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i("Error checking document existence")
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
                            Timber.i("Score for $playerName set to zero.")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error updating score")
                            onFailure(e)
                        }
                } else {
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i( "Error checking document existence")
                onFailure(task.exception!!)
            }
        }
    }

    fun setStartPlaying(playerName: String, startPlaying: Boolean, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val updateMap = hashMapOf("startPlaying" to startPlaying)

                    documentReference.update(updateMap as Map<String, Any>)
                        .addOnSuccessListener {
                            val status = if (startPlaying) "started" else "stopped"
                            Timber.i("Player $playerName $status playing.")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error updating startPlaying status")
                            onFailure(e)
                        }
                } else {
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i("Error checking document existence")
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
                            Timber.i("CountDown for $playerName is: $newCountDown")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error updating countDown")
                            onFailure(e)
                        }
                } else {
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i("Error checking document existence")
                onFailure(task.exception!!)
            }
        }
    }

    fun listenToScoreChanges(playerName: String, listener: (Int) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        val scoreListener = documentReference.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Timber.i("Error listening to changes")
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
                Timber.i("Error listening to changes")
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val countDown = documentSnapshot.getLong("countDown")?.toInt() ?: 0
                listener(countDown)
            }
        }

        countDownListeners[playerName] = countDownListener
    }

    fun listenToStartPlayingChanges(playerName: String, listener: (Boolean) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        val startPlayingListener = documentReference.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Timber.i("Error listening to changes")
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val startPlaying = documentSnapshot.getBoolean("startPlaying") ?: false
                listener(startPlaying)
            }
        }

        startPlayingListeners[playerName] = startPlayingListener
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
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i("Error checking document existence")
                onFailure(task.exception!!)
            }
        }
    }

    fun readCountDown(playerName: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit) {
        val documentReference = db.collection("users").document(playerName)

        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val countDown = document.getLong("countDown")?.toInt() ?: 0
                    onSuccess(countDown)
                } else {
                    Timber.i("Document with playerName $playerName does not exist")
                    onFailure(Exception("Document with playerName $playerName does not exist"))
                }
            } else {
                Timber.i("Error checking document existence")
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

    fun removeAllStartPlayingListeners() {
        for (listener in startPlayingListeners.values) { listener.remove() }
        startPlayingListeners.clear()
    }

}
