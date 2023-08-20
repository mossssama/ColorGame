package com.example.colorgame.ui.multiplayerMode.multiplayerResult.viewModel

import com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepository
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepositoryImpl
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
