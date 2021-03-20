package com.example.core.storage.di

import com.example.core.storage.daos.CurrenciesDao
import com.example.core.storage.daos.ProfileDao

interface CoreStorageApi :
    CurrenciesDao.Injector,
    ProfileDao.Injector