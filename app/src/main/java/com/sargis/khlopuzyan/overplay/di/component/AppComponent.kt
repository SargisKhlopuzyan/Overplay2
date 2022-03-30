package com.sargis.khlopuzyan.overplay.di.component

import android.content.Context
import com.sargis.khlopuzyan.overplay.App
import com.sargis.khlopuzyan.overplay.di.module.AppModule
import com.sargis.khlopuzyan.overplay.di.module.OverplayModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Sargis Khlopuzyan on 3/29/2022.
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        OverplayModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Context
        ): AppComponent
    }

}