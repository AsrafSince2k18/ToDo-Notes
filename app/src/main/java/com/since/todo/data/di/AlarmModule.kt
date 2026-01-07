package com.since.todo.data.di

import android.content.Context
import com.since.todo.data.repo_impl.AlarmRepoImpl
import com.since.todo.domain.repo.AlarmRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmRepoImpl(@ApplicationContext context: Context) : AlarmRepo{
        return AlarmRepoImpl(context=context)
    }

}