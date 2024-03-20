package com.vicky7230.mvi.di

import com.vicky7230.mvi.data.repository.TodoRepositoryImpl
import com.vicky7230.mvi.domain.repository.TodoRepository
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
    abstract fun bindRepository(todoRepositoryImp: TodoRepositoryImpl): TodoRepository

}