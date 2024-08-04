package com.reznicsoftware.buildtime

import com.reznicsoftware.buildtime.utils.SysInfo

class Info

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
