package com.vicky7230.mvi.di

import com.vicky7230.mvi.data.remote.TodoRemoteSource
import com.vicky7230.mvi.data.remote.TodoRemoteSourceImpl
import com.vicky7230.mvi.data.repository.TodoRemoteRepositoryImpl
import com.vicky7230.mvi.domain.repository.TodoRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindTodoRemoteSource(todoRemoteSource: TodoRemoteSourceImpl): TodoRemoteSource

    @Binds
    @Singleton
    abstract fun bindTodoRemoteRepository(todoRemoteRepository: TodoRemoteRepositoryImpl): TodoRemoteRepository

}