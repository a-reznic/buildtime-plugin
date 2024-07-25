package com.reznicsoftware.buildtime.api

import kotlinx.serialization.Serializable

@Serializable
data class ResultDTO(
    val deviceId: String?,
    val cpu: String?,
    val cores: Int?,
    val javaVersion: String?,
    val pluginVersion: String?,
    val projectName: String?,
    val os: String?,
    val durationSeconds: Long?,
    val memorySize: Long? = null,
    val memoryInfo: String? = null,
    val createdAt: String? = null,
    val androidStudioVersion: String?,
    val deviceName: String?,
)