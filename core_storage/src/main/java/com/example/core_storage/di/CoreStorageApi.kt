package com.example.core_storage.di

import com.example.core_storage.daos.CurrenciesDao
import com.example.core_storage.daos.ProfileDao

interface CoreStorageApi :
    CurrenciesDao.Injector,
    ProfileDao.Injector