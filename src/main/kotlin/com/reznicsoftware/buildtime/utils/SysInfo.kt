package com.reznicsoftware.buildtime.utils

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

fun main() {
    println(SysInfo.getDeviceId())
    println(SysInfo.getOSIdentifier())
    println(SysInfo.getJavaVersion())
    println(SysInfo.getCPUIdentifier())
    println(SysInfo.getCpuCores())
    println(SysInfo.getMemorySize())
    println(SysInfo.getDiskInfo())
    println(SysInfo.getMemoryInfo())
}

object SysInfo {
    private val systemInfo = SystemInfo()

    fun getDiskInfo(): String {
        val hardware: HardwareAbstractionLayer = systemInfo.hardware

        val diskStores = hardware.diskStores.firstOrNull()
        return "${diskStores?.model}#${diskStores?.size}"
    }

    fun getOSIdentifier(): String {
        return listOf("os.name", "os.version", "os.arch").joinToString("_") { System.getProperty(it) }
    }

    fun getDeviceId(): String {
        val systemInfo = SystemInfo()
        val operatingSystem: OperatingSystem = systemInfo.operatingSystem
        val hardwareAbstractionLayer: HardwareAbstractionLayer = systemInfo.hardware
        val centralProcessor: CentralProcessor = hardwareAbstractionLayer.processor
        val computerSystem: ComputerSystem = hardwareAbstractionLayer.computerSystem

        val vendor: String = operatingSystem.manufacturer.replace(" ","")
        val hardwareUUID: String = computerSystem.hardwareUUID.replace("-","").replace(" ","")
         val processorIdentifier: String = centralProcessor.processorIdentifier.processorID.replace(" ","")
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

    fun getMemorySize(): Long {
        val hardware: HardwareAbstractionLayer = systemInfo.hardware
        return hardware.memory.total
    }

    fun getMemoryInfo(): String {
        val hardware: HardwareAbstractionLayer = systemInfo.hardware
        return hardware.memory.physicalMemory
            .mapIndexed { index: Int, m: PhysicalMemory? ->
                "$index-${m?.capacity}-${m?.memoryType}-${m?.clockSpeed}"
            }.joinToString { it }
    }

    fun getCpuCores(): Int {
        val hardwareAbstractionLayer: HardwareAbstractionLayer = systemInfo.hardware
        val centralProcessor: CentralProcessor = hardwareAbstractionLayer.processor

        return centralProcessor.physicalProcessorCount
    }

    fun getCPUIdentifier(): String {
        val hardwareAbstractionLayer: HardwareAbstractionLayer = systemInfo.hardware
        val centralProcessor: CentralProcessor = hardwareAbstractionLayer.processor
        return centralProcessor.processorIdentifier.name
    }
}