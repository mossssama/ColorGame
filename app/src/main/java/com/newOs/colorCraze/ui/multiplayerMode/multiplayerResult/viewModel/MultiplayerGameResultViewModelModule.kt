package com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.viewModel

import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepository
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MultiplayerGameResultViewModelModule {
    @Provides
    fun provideMultiplayerGameResultRepository(): MultiplayerGameResultRepository = MultiplayerGameResultRepositoryImpl()
}
