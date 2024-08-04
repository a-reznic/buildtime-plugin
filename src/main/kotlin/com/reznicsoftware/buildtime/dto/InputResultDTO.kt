package com.reznicsoftware.buildtime.dto

import kotlinx.serialization.Serializable

@Serializable
data class InputResultDTO(
    val deviceId: String? = null,
    val cpu: CpuDTO? = null,
    val os: OsDTO? = null,
    val ram: RamDTO? = null,
    val hdd: HardDriveDTO? = null,

    val javaVersion: String? = null,
    val pluginVersion: String? = null,
    val deviceName: String? = null,
    val projectName: String? = null,
    val durationSeconds: Long? = null,
    val createdAt: String? = null,
    val androidStudioVersion: String? = null,
    val remoteAddr: String? = null
)