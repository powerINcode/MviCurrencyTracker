package com.example.feature_rate_tracker_api.domain

import com.example.core.domain.usecase.FlowSimpleDataUseCase
import com.example.feature_rate_tracker_api.data.models.Advertisement

interface ObserveAdvertisementUseCase : FlowSimpleDataUseCase<List<Advertisement>>