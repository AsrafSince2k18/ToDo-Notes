package com.since.todo.data.di

import android.content.Context
import androidx.room.Room
import com.since.todo.data.database.NoteDatabase
import com.since.todo.data.repo_impl.NoteRepoImpl
import com.since.todo.domain.repo.NoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : NoteDatabase{
        return Room.databaseBuilder(context, NoteDatabase::class.java,"ToDo").build()
    }

    @Provides
    @Singleton
    fun provideNoteRepo(noteDatabase: NoteDatabase): NoteRepo{
        return NoteRepoImpl(noteDao = noteDatabase.noteDao())
    }

}