package com.reznicsoftware.buildtime

import com.reznicsoftware.buildtime.dto.InputResultDTO
import com.reznicsoftware.buildtime.utils.SysInfo
import kotlinx.serialization.encodeToString
import kotlin.test.Test

class TestInfo {
    @Test
    fun testInfo() {
        println(json.encodeToString<InputResultDTO>(SysInfo.getAll()))
    }
}