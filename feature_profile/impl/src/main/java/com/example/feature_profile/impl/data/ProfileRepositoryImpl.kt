package com.example.feature_profile.impl.data

import com.example.core.storage.daos.ProfileDao
import com.example.core.storage.models.profile.ProfileEntity
import com.example.core.streams.coroutine.onIo
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao
) : com.example.feature_profile.api.data.ProfileRepository {
    override suspend fun saveProfile(profile: com.example.feature_profile.api.data.model.Profile) {
        return onIo {
            profileDao.saveProfile(
                profile = profile.run {
                    ProfileEntity(
                        first = first,
                        second = second,
                        last = last
                    )
                }
            )
        }
    }

    override suspend fun getProfile(): com.example.feature_profile.api.data.model.Profile? = onIo {
        profileDao.getProfile()
            ?.let { entity ->
                entity.run {
                    com.example.feature_profile.api.data.model.Profile(
                        first = first,
                        second = second,
                        last = last
                    )
                }
            }
    }
}