package com.vicky7230.mvi.di

import com.vicky7230.mvi.data.Repository
import com.vicky7230.mvi.data.RepositoryImp
import com.vicky7230.mvi.data.network.ApiHelper
import com.vicky7230.mvi.data.network.ApiHelperImp
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
    abstract fun bindRepository(repositoryImp: RepositoryImp): Repository

    @Binds
    @Singleton
    abstract fun bindApiHelper(apiHelperImp: ApiHelperImp): ApiHelper

}