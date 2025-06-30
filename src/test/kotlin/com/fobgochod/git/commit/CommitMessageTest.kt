package com.fobgochod.git.commit

import com.fobgochod.git.commit.domain.CommitMessage
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class CommitMessageTest : BasePlatformTestCase() {

    fun testStandard() {
        val message = """
            feat(scope): A new feature
            
            Just as in the subject, use the imperative, present tense: "change" not "changed" nor "changes". 
            The body should include the motivation for the change and contrast this with previous behavior.

            BREAKING CHANGE: should start with the word BREAKING CHANGE: with a space or two newlines. 
            The rest of the commit message is then used for this.

            Closes #111

            [skip ci]
        """.trimIndent()


        val cm = CommitMessage.parse(message)
        println(cm)

        assertTrue(cm.changeType.name == "feat")
        assertTrue(cm.changeScope == "scope")
        assertTrue(cm.closedIssues == "#111")
        assertTrue(cm.skipCI)
    }

    fun testOnlyHeader() {
        val message = """
            feat(scope): 添加登陆日志
        """.trimIndent()

        val cm = CommitMessage.parse(message)
        println(cm)

        assertTrue(cm.changeType.name == "feat")
        assertTrue(cm.changeScope == "scope")
    }

    fun testSingleLine() {
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
        assertTrue(cm.changeBody == "1.日志管理服务")
        assertTrue(cm.closedIssues == "#111")
        assertTrue(cm.skipCI)
    }

    fun testMultiLine() {
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

    fun testOnlySkipCI() {
        val message = """
            feat(scope): 测试只有 skip ci

            [skip ci]
        """.trimIndent()


        val cm = CommitMessage.parse(message)
        println(cm)

        assertTrue(cm.changeType.name == "feat")
        assertTrue(cm.changeScope == "scope")
        assertTrue(cm.skipCI)
    }

    fun testNoHeader() {
        val message = """
            1.日志管理服务
            
            [skip ci]
        """.trimIndent()


        val cm = CommitMessage.parse(message)
        println(cm)

        assertTrue(cm.changeBody == "1.日志管理服务")
        assertTrue(cm.skipCI)
    }

    fun testEmpty() {
        val message = ""

        val cm = CommitMessage.parse(message)
        println(cm)

        assertEmpty(cm.changeSubject)
    }
}