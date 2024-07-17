package com.reznicsoftware.buildtime

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

class TimingRecorder(private val testPlugin: TestPlugin) : TaskExecutionListener, BuildListener {
    private var startTime = System.currentTimeMillis()

    override fun settingsEvaluated(settings: Settings) {
        startTime = System.currentTimeMillis()
    }

    override fun projectsLoaded(gradle: Gradle) {
    }

    override fun projectsEvaluated(gradle: Gradle) {
    }

    override fun buildFinished(result: BuildResult) {
        testPlugin.sendReport(System.currentTimeMillis() - startTime)
    }

    override fun beforeExecute(task: Task) {
    }

    override fun afterExecute(task: Task, state: TaskState) {
    }

}
