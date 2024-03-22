package com.fobgochod.git.commit

import com.fobgochod.git.commit.domain.CommitMessage
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class CommitMessageTest : BasePlatformTestCase() {

    fun testParseOnlyHeader() {
        val message = """
            feat(scope): 添加登陆日志
        """.trimIndent()

        val cm = CommitMessage.parse(message)
        println(cm)

        assertTrue(cm.changeType.name == "feat")
        assertTrue(cm.changeScope == "scope")
    }

    fun testParseSingleLine() {
        val message = """
            
            feat(scope): 添加登陆日志
            1.日志管理服务
            
            BREAKING CHANGE: 1.重大变化1

            Closes#111

            [skip ci]
        """.trimIndent()


        val cm = CommitMessage.parse(message)
        println(cm)

        assertTrue(cm.changeType.name == "feat")
        assertTrue(cm.changeScope == "scope")
        assertTrue(cm.closedIssues == "#111")
        assertTrue(cm.skipCI)
    }

    fun testParseMultiLine() {
        val message = """
            
            feat(scope): 添加登陆日志
            1.日志管理服务
            2.登陆日志接入
            3.单元测试

            BREAKING CHANGE: 
            1.重大变化1
            2.重大变化2

            Closes#111
            Closes222

            [skip ci]
        """.trimIndent()


        val cm = CommitMessage.parse(message)
        println(cm)

        assertTrue(cm.changeType.name == "feat")
        assertTrue(cm.changeScope == "scope")
        assertTrue(cm.closedIssues == "#111,#222")
        assertTrue(cm.skipCI)
    }
}