package com.fobgochod.git.commit

import com.fobgochod.git.commit.domain.CommitMessage
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ClosedIssueTest : BasePlatformTestCase() {

    fun testCase1() {
        val message = "#111"
        val result = CommitMessage.formatClosedIssue(message)

        assertTrue(result == "#111")
    }

    fun testCase2() {
        val message = "222"
        val result = CommitMessage.formatClosedIssue(message)

        assertTrue(result == "#222")
    }

    fun testCase3() {
        val message = " # 333 "
        val result = CommitMessage.formatClosedIssue(message)

        assertTrue(result == "#333")
    }
}
