package com.example.feature_profile_impl.di

import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_impl.data.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ProfileFeatureModule {
    @Singleton
    @Binds
    fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository
}