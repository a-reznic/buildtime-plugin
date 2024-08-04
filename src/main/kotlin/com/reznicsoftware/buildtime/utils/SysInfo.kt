package com.reznicsoftware.buildtime.utils

import com.reznicsoftware.buildtime.PLUGIN_VERSION
import com.reznicsoftware.buildtime.dto.*
import oshi.SystemInfo
import oshi.hardware.CentralProcessor
import oshi.hardware.ComputerSystem
import oshi.hardware.HardwareAbstractionLayer
import oshi.hardware.PhysicalMemory
import oshi.software.os.OperatingSystem

fun String.getMaxChars(length: Int): String {
    if (this.length < length) return this
    return this.substring(0, length)
}

object SysInfo {
    private val systemInfo = SystemInfo()

    fun getAll(): InputResultDTO {
        return InputResultDTO(
            deviceId = getDeviceId(),
            cpu = cpuInfo(),
            hdd = getDiskInfo(),
            os = getOSIdentifier(),
            ram = ramInfo(),
            javaVersion = getJavaVersion(),
            pluginVersion = PLUGIN_VERSION.trim(),
        )
    }

    fun getDiskInfo(): HardDriveDTO {
        val hardware: HardwareAbstractionLayer = systemInfo.hardware

        val diskStores = hardware.diskStores.map {
            HardDriveInfoDTO(
                model = it?.model.orEmpty(),
                size = it?.size
            )
        }

        return HardDriveDTO(
            list = diskStores
        )
    }

    fun getOSIdentifier(): OsDTO {
        return OsDTO(
            name = System.getProperty("os.name"),
            version = System.getProperty("os.version"),
            arch = System.getProperty("os.arch")
        )
    }

    fun getDeviceId(): String {
        val systemInfo = SystemInfo()
        val operatingSystem: OperatingSystem = systemInfo.operatingSystem
        val hardwareAbstractionLayer: HardwareAbstractionLayer = systemInfo.hardware
        val centralProcessor: CentralProcessor = hardwareAbstractionLayer.processor
        val computerSystem: ComputerSystem = hardwareAbstractionLayer.computerSystem
        //
        val vendor: String = operatingSystem.manufacturer.replaceSpace()
        val hardwareUUID: String = computerSystem.hardwareUUID.replaceSpace().replace("-", "")
        val processorIdentifier: String = centralProcessor.processorIdentifier.processorID.replaceSpace()
        val processors: Int = centralProcessor.logicalProcessorCount

        return "$vendor$hardwareUUID$processorIdentifier$processors"
    }

    fun getJavaVersion(): String {
        return listOf(
            "java.vm.name",
            "java.vm.vendor",
            "java.vm.version"
        ).joinToString("_") { System.getProperty(it) }
    }

    fun ramInfo(): RamDTO {
        return RamDTO(
            list = systemInfo.hardware.memory.physicalMemory.map { m: PhysicalMemory? ->
                MemoryInfoDTO(
                    memoryType = m?.memoryType,
                    capacity = m?.capacity,
                    clockSpeed = m?.clockSpeed,
                    manufacturer = m?.manufacturer
                )
            },
            total = systemInfo.hardware.memory.total,
            availability = systemInfo.hardware.memory.available
        )
    }

    fun cpuInfo(): CpuDTO {
        val processor: CentralProcessor = systemInfo.hardware.processor

        return CpuDTO(
            name = processor.processorIdentifier.name,
            physicalCount = processor.physicalProcessorCount,
            logicalCount = processor.logicalProcessorCount,
            archictecture = processor.processorIdentifier.microarchitecture,
        )
    }
}

fun String.replaceSpace(): String {
    return this.replace(" ", "")
}