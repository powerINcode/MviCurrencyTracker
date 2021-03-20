package com.example.feature_rate_tracker_api.domain

import com.example.core.usecase.ObservableSimpleDataUseCase
import com.example.feature_rate_tracker_api.data.models.Advertisement

interface ObserveAdvertisementUseCase : ObservableSimpleDataUseCase<List<Advertisement>>