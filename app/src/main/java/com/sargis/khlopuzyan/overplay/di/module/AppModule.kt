package com.sargis.khlopuzyan.overplay.di.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sargis.khlopuzyan.overplay.data.local.DataStoreUtil
import com.sargis.khlopuzyan.overplay.di.factory.AppViewModelFactory
import com.sargis.khlopuzyan.overplay.repository.OverplayRepository
import com.sargis.khlopuzyan.overplay.repository.OverplayRepositoryImpl
import com.sargis.khlopuzyan.overplay.util.SensorEventUtil
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by Sargis Khlopuzyan on 3/29/2022.
 */
@Module(includes = [AppModule.ProvideViewModel::class])
abstract class AppModule {

    @Module
    class ProvideViewModel {

        @Provides
        @Singleton
        fun provideExecutor(): Executor = Executors.newFixedThreadPool(2)

        @Provides
        @Singleton
        fun provideDataStoreUtil(context: Context): DataStoreUtil = DataStoreUtil(context)

        @Provides
        @Singleton
        fun provideSensorEventUtil(): SensorEventUtil = SensorEventUtil()

        @Provides
        fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory = AppViewModelFactory(providers)

        @Provides
        @Singleton
        fun provideOverplayRepository(
            dataStoreUtil: DataStoreUtil,
            sensorEventUtil: SensorEventUtil
        ): OverplayRepository =
            OverplayRepositoryImpl(
                dataStoreUtil,
                sensorEventUtil,
                Dispatchers.IO
            )
    }

}