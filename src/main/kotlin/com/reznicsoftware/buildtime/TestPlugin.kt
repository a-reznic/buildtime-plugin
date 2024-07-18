package com.reznicsoftware.buildtime

import com.reznicsoftware.buildtime.api.BackendApi
import com.reznicsoftware.buildtime.api.ResultDTO
import com.reznicsoftware.buildtime.utils.SysInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.LoggerFactory

class TestPlugin : Plugin<Project> {
    private var logger: org.slf4j.Logger = LoggerFactory.getLogger("TestPlugin")
    private var project: Project? = null
    private var startTime = System.currentTimeMillis()

    override fun apply(project: Project) {
        this.logger = project.logger
        this.project = project
        println(">>> BuildTimePlugin")
        project.extensions.create("buildTimeOptions", BuildTimeOptionsExtension::class.java)

        project.gradle.projectsEvaluated {
            startTime = System.currentTimeMillis()
        }
        project.gradle.buildFinished {
            sendReport(System.currentTimeMillis() - startTime)
        }
    }

    fun sendReport(time: Long) {
        var sendData = false
        var androidStudioVersion: String? = null
        var baseUrl = "https://buildtime.reznicsoftware.com"

        project?.let {
            val ext = it.extensions.getByName("buildTimeOptions") as? BuildTimeOptionsExtension
            ext?.let { extension ->
                if (extension.sendData.isPresent) {
                    sendData = extension.sendData.get() == true
                }
                val version = extension.getInfo()?.androidStudioVersion
                val burl = extension.getInfo()?.serverURL
                if (version?.isPresent == true) {
                    androidStudioVersion = version.get()
                }
                if (burl?.isPresent == true) {
                    baseUrl = burl.get()
                }
            }
        }
        val data = ResultDTO(
            deviceId = SysInfo.getDeviceId(),
            cpu = SysInfo.getCPUIdentifier(),
            cores = SysInfo.getCpuCores(),
            androidStudioVersion = androidStudioVersion,
            projectName = project?.rootProject?.name.toString(),
            memorySize = SysInfo.getMemorySize(),
            memoryInfo = SysInfo.getMemoryInfo(),
            javaVersion = SysInfo.getJavaVersion(),
            os = SysInfo.getOSIdentifier(),
            durationSeconds = time / 1000
        )
        println("::${project?.rootProject?.name}::")
        println("$data")
        println(">>>BuildTimePlugin<<<")

        if (sendData) {
            BackendApi(baseUrl).sendData(data)
        }
    }
}

