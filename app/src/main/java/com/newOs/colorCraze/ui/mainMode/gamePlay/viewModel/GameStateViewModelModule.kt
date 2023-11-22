package com.newOs.colorCraze.ui.mainMode.gamePlay.viewModel

import com.newOs.colorCraze.ui.mainMode.gamePlay.repository.GameStateRepository
import com.newOs.colorCraze.ui.mainMode.gamePlay.repository.GameStateRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object GameStateViewModelModule {

    @Provides
    fun provideGameStateRepository(): GameStateRepository = GameStateRepositoryImpl()

}
