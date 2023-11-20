package com.newOs.colorCraze.ui.mainMode.scoresHistory.viewModel

import com.newOs.colorCraze.ui.mainMode.scoresHistory.repository.GetScoresRepository
import com.newOs.colorCraze.ui.mainMode.scoresHistory.repository.GetScoresRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object GetScoresViewModelModule {
    @Provides
    fun  provideGetScoresRepository(): GetScoresRepository = GetScoresRepositoryImpl()
}

