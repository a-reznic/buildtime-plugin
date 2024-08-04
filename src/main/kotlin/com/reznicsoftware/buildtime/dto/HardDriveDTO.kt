package com.reznicsoftware.buildtime.dto

import kotlinx.serialization.Serializable

@Serializable
data class HardDriveDTO(
    val list: List<HardDriveInfoDTO>
)

@Serializable
data class HardDriveInfoDTO(
    val model: String?,
    val size: Long?
)