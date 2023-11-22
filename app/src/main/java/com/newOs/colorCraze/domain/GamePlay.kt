package com.newOs.colorCraze.domain

import android.content.Context
import android.view.View
import com.newOs.colorCraze.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineScope

interface GamePlay {
    fun getNewUI(binding: View): String
    fun threeWrongGamePlay(boxId: String, binding: View, context: Context,dataStoreManager: DataStoreManager,lifecycleScope: CoroutineScope)
    fun continuousRightModeGamePlay(boxId: String, binding: View, context: Context,dataStoreManager: DataStoreManager,lifecycleScope: CoroutineScope)
}