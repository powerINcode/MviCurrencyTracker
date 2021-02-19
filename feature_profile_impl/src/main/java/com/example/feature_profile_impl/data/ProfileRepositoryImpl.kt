package com.example.feature_profile_impl.data

import com.example.core.coroutine.onIo
import com.example.core_storage.daos.ProfileDao
import com.example.core_storage.models.profile.ProfileEntity
import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_api.data.model.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao
) : ProfileRepository {
    override suspend fun saveProfile(profile: Profile) {
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

    override suspend fun getProfile(): Profile? = onIo {
        profileDao.getProfile()
            ?.let { entity ->
                entity.run {
                    Profile(
                        first = first,
                        second = second,
                        last = last
                    )
                }
            }
    }
}