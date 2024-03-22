package com.fobgochod.git.commit

import com.fobgochod.git.commit.util.StringUtils
import junit.framework.TestCase

class StringUtilsTest : TestCase() {

    fun testFormatClosedIssue() {
        val result1 = StringUtils.formatClosedIssue("#111")
        val result2 = StringUtils.formatClosedIssue("222")
        val result3 = StringUtils.formatClosedIssue(" # 333 ")

        assertTrue(result1 == "#111")
        assertTrue(result2 == "#222")
        assertTrue(result3 == "#333")
    }

    fun testIsNumeric() {
        assertTrue(StringUtils.isNumeric("123"))
        assertTrue(!StringUtils.isNumeric("#123"))
    }
}
