package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.viewModel

import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository.MultiplayerGameStateRepository
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository.MultiplayerGameStateRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MultiplayerGameStateViewModelModule {

    @Provides
    fun provideMultiplayerGameStateRepository(): MultiplayerGameStateRepository = MultiplayerGameStateRepositoryImpl()
}