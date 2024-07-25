package com.reznicsoftware.buildtime

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

abstract class BuildTimeInfoExtension {
    abstract val androidStudioVersion: Property<String>
    abstract val serverURL: Property<String>
    abstract val deviceName: Property<String>
}

abstract class BuildTimeOptionsExtension {

    abstract val sendData: Property<Boolean?>

    @Nested
    abstract fun getInfo(): BuildTimeInfoExtension?

    fun info(action: Action<in BuildTimeInfoExtension>) {
        action.execute(getInfo())
    }
}