package com.example.feature_profile_impl.di

import com.example.core.di.scopes.FeatureScope
import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_impl.data.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ProfileFeatureModule {
    @FeatureScope
    @Binds
    fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository
}