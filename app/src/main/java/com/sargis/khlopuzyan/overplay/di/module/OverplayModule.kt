package com.sargis.khlopuzyan.overplay.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sargis.khlopuzyan.overplay.di.annotation.ViewModelKey
import com.sargis.khlopuzyan.overplay.repository.OverplayRepository
import com.sargis.khlopuzyan.overplay.ui.OverplayFragment
import com.sargis.khlopuzyan.overplay.ui.OverplayViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by Sargis Khlopuzyan on 3/29/2022.
 */
@Module(includes = [OverplayModule.ProvideViewModel::class])
interface OverplayModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    fun bind(): OverplayFragment

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(OverplayViewModel::class)
        fun provideOverplayViewModel(
            repository: OverplayRepository
        ): ViewModel = OverplayViewModel(repository)
    }

    @Module
    class InjectViewModel {
        @Provides
        fun provideOverplayViewModel(
            factory: ViewModelProvider.Factory,
            target: OverplayFragment
        ) = ViewModelProvider(target, factory)[OverplayViewModel::class.java]
    }

}