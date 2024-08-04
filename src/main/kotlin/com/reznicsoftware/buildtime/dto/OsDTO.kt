package com.reznicsoftware.buildtime.dto

import kotlinx.serialization.Serializable

@Serializable
data class OsDTO(
    val name: String?,
    val version: String?,
    val arch: String?,
)