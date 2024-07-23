package com.reznicsoftware.buildtime

import com.reznicsoftware.buildtime.api.BackendApi
import com.reznicsoftware.buildtime.api.ResultDTO
import com.reznicsoftware.buildtime.utils.SysInfo
import com.reznicsoftware.buildtime.utils.getMaxChars
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskActionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class TestPlugin : Plugin<Project> {
    private var project: Project? = null
    private var startTime: Long = -1

    override fun apply(project: Project) {
        this.project = project
        println("BuildTimePlugin: Init ver.${project.version}")
        project.extensions.create("buildTimeOptions", BuildTimeOptionsExtension::class.java)
        project.gradle.addBuildListener(object : BuildListener {
            override fun settingsEvaluated(settings: Settings) {
                println("BuildTimePlugin: projectsLoaded")
            }

            override fun projectsLoaded(gradle: Gradle) {
                println("BuildTimePlugin: projectsLoaded")
                startTime = -1
            }

            override fun projectsEvaluated(gradle: Gradle) {
                println("BuildTimePlugin: projectsLoaded")
            }

            override fun buildFinished(result: BuildResult) {
                println("buildFinished")
                if (startTime > 0) {
                    sendReport(System.currentTimeMillis() - startTime)
                }
            }
        })
        project.gradle.addListener(object : TaskActionListener {
            override fun beforeActions(task: Task) {
                println("before: ${task.name}")
                if (task.name == "clean") {
                    startTime = System.currentTimeMillis()
                }
            }

            override fun afterActions(task: Task) {
                println("after: ${task.name}")
            }
        })
        project.gradle.projectsEvaluated {
            startTime = System.currentTimeMillis()
        }
        project.gradle.buildFinished {
            println("project.gradle.buildFinished")
        }
    }

    fun sendReport(time: Long) {
        var sendData = false
        var androidStudioVersion: String? = null
        var baseUrl = "https://buildtime.reznicsoftware.com/add"

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
        val isOnlyClean = (project?.gradle?.taskGraph?.allTasks?.size ?: 0) <= 1
        if (isOnlyClean) {
            sendData = false
        }
        println("BuildTimePlugin sendData: $sendData")

        val data = ResultDTO(
            deviceId = SysInfo.getDeviceId(),
            cpu = SysInfo.getCPUIdentifier(),
            cores = SysInfo.getCpuCores(),
            androidStudioVersion = androidStudioVersion?.getMaxChars(30_000),
            projectName = project?.rootProject?.name.toString(),
            memorySize = SysInfo.getMemorySize(),
            memoryInfo = SysInfo.getMemoryInfo(),
            javaVersion = SysInfo.getJavaVersion(),
            os = SysInfo.getOSIdentifier(),
            durationSeconds = time / 1000
        )
        println("$data")
        println("BuildTimePlugin end")

        if (sendData) {
            BackendApi(baseUrl).sendData(data)
        }
    }
}

