package com.reznicsoftware.buildtime

import com.reznicsoftware.buildtime.api.BackendApi
import com.reznicsoftware.buildtime.dto.*
import com.reznicsoftware.buildtime.utils.SysInfo
import com.reznicsoftware.buildtime.utils.getMaxChars
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskActionListener
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

const val BASE_URL = "https://buildtime.reznicsoftware.com/add"
val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

class TestPlugin : Plugin<Project> {
    private var project: Project? = null
    private var startTime: Long = -1

    override fun apply(project: Project) {
        this.project = project

        println(":: BuildTimePlugin: Init ver.${PLUGIN_VERSION} ::")

        project.extensions.create("buildTimeOptions", BuildTimeOptionsExtension::class.java)

        project.gradle.addListener(object : TaskExecutionGraphListener {
            override fun graphPopulated(graph: TaskExecutionGraph) {
//                validateTasks()
            }
        })

        project.gradle.addListener(object : TaskActionListener {
            override fun beforeActions(task: Task) {
                if (task.name == "clean" && startTime < 0) {
                    startTime = System.currentTimeMillis()
                }
            }

            override fun afterActions(task: Task) {
            }
        })

        project.gradle.projectsEvaluated {
            startTime = System.currentTimeMillis()
        }

        project.gradle.buildFinished {
            if (it.failure == null && startTime > 0) {
                sendReport(System.currentTimeMillis() - startTime)
            }
        }
    }

    fun isValidTasks(): Boolean {
        val allTasks = project?.gradle?.taskGraph?.allTasks.orEmpty()
        val clearTask: Task? = allTasks.find { it.name == "clean" }
        val assembleRelease: Task? = allTasks.find { it.name == "assemble" }

        return !(clearTask == null || assembleRelease == null)
    }

    private fun sendReport(time: Long) {
        if (isValidTasks().not()) {
            println("To send you results please run `gradlew clean assemble`")
            return
        }

        var sendData = false
        var androidStudioVersion: String? = null
        var deviceName: String? = null
        var baseUrl = BASE_URL

        project?.let {
            val ext = it.extensions.getByName("buildTimeOptions") as? BuildTimeOptionsExtension
            ext?.let { extension ->
                if (extension.sendData.isPresent) {
                    sendData = extension.sendData.get() == true
                }

                val infoAndroidStudioVersion = extension.getInfo()?.androidStudioVersion
                if (infoAndroidStudioVersion?.isPresent == true) {
                    androidStudioVersion = infoAndroidStudioVersion.get()
                }


                val infoDeviceName = extension.getInfo()?.deviceName
                if (infoDeviceName?.isPresent == true) {
                    deviceName = infoDeviceName.get()
                }

                val burl = extension.getInfo()?.serverURL
                if (burl?.isPresent == true) {
                    baseUrl = burl.get()
                }
            }
        }

        println("System info:")
        val data = SysInfo.getAll().copy(
            androidStudioVersion = androidStudioVersion?.getMaxChars(30_000),
            deviceName = deviceName?.getMaxChars(1_000),
            projectName = project?.rootProject?.name.toString(),
            durationSeconds = time / 1000
        )

        println(json.encodeToString<InputResultDTO>(data))

        if (sendData) {
            BackendApi(baseUrl).sendData(data)
        }
    }
}

