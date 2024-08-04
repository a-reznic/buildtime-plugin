package com.reznicsoftware.buildtime.dto

import kotlinx.serialization.Serializable

@Serializable
data class CpuDTO(
    var physicalCount: Int? = null,
    var logicalCount: Int? = null,
    var archictecture: String? = null,
    var name: String? = null
)
