package com.example.feature_profile.impl.di

import com.example.core.di.scopes.FeatureScope
import com.example.feature_profile.impl.data.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ProfileFeatureModule {
    @FeatureScope
    @Binds
    fun bindProfileRepository(repository: ProfileRepositoryImpl): com.example.feature_profile.api.data.ProfileRepository
}