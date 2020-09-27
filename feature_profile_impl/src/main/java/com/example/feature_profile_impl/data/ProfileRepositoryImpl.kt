package com.example.feature_profile_impl.data

import com.example.core.rx.onIo
import com.example.core_storage.daos.ProfileDao
import com.example.core_storage.models.profile.ProfileEntity
import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_api.data.model.Profile
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao
) : ProfileRepository {
    override fun saveProfile(profile: Profile): Completable {
        return profileDao.saveProfile(
            profile = profile.run {
                ProfileEntity(
                    first = first,
                    second = second,
                    last = last
                )
            }
        ).onIo()
    }

    override fun getProfile(): Maybe<Profile> = profileDao.getProfile()
        .map { entity ->
            entity.run {
                Profile(
                    first = first,
                    second = second,
                    last = last
                )
            }
        }.onIo()
}