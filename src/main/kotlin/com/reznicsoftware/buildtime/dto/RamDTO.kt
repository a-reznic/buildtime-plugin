package com.reznicsoftware.buildtime.dto

import kotlinx.serialization.Serializable

@Serializable
data class RamDTO(
    val list: List<MemoryInfoDTO>?,
    val total: Long?,
    val availability: Long?
)

@Serializable
data class MemoryInfoDTO(
    val clockSpeed: Long?,
    val capacity: Long?,
    val memoryType: String?,
    val manufacturer: String?
)
