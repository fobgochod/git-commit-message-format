package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.settings.GitSettings
import com.intellij.openapi.diagnostic.Logger
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils

data class CommitMessage(
    var changeType: String = "",
    var changeScope: String = "",
    var changeSubject: String = "",
    var changeBody: String = "",
    var wrapText: Boolean = true,
    var breakingChanges: String = "",
    var closedIssues: String = "",
    var skipCI: Boolean = false
) {

    companion object {
        private val logger = Logger.getInstance(CommitMessage::class.java)
        private val state: GitSettings = GitSettings.getInstance()

        fun parse(message: String): CommitMessage {
            val commitMessage = CommitMessage()
            try {
                var matcher = GitConstant.HEADER_PATTERN.matcher(message)
                if (!matcher.find()) return commitMessage

                commitMessage.changeType = state.getTypeFromName(matcher.group(1)).name
                commitMessage.changeScope = if (matcher.group(3) != null) matcher.group(3) else ""
                commitMessage.changeSubject = if (matcher.group(4) != null) matcher.group(4) else ""

                val messages = message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (messages.size < 2) {
                    return commitMessage
                }

                var pos = 1
                var builder: StringBuilder = StringBuilder()
                while (pos < messages.size) {
                    val lineString = messages[pos]
                    if (lineString.startsWith(GitConstant.BREAKING_CHANGE_PREFIX)
                        || lineString.startsWith(GitConstant.CLOSES_PREFIX)
                        || lineString.equals(state.skipCI.label, ignoreCase = true)
                    ) break
                    builder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.changeBody = builder.toString().trim { it <= ' ' }

                builder = StringBuilder()
                while (pos < messages.size) {
                    val lineString = messages[pos]
                    if (lineString.startsWith(GitConstant.CLOSES_PREFIX)
                        || lineString.equals(state.skipCI.label, ignoreCase = true)
                    ) break
                    builder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.breakingChanges =
                    builder.toString().trim { it <= ' ' }.replace(GitConstant.BREAKING_CHANGE_PREFIX, "")

                matcher = GitConstant.CLOSED_ISSUES_PATTERN.matcher(message)
                builder = StringBuilder()
                while (matcher.find()) {
                    builder.append(matcher.group(1)).append(',')
                }
                if (builder.isNotEmpty()) builder.delete(builder.length - 1, builder.length)
                commitMessage.closedIssues = builder.toString()

                commitMessage.skipCI = message.contains(state.skipCI.label)
            } catch (e: RuntimeException) {
                logger.error(e.message)
            }
            return commitMessage
        }
    }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append(changeType)
        if (StringUtils.isNotBlank(changeScope)) {
            builder.append('(').append(changeScope).append(')')
        }
        builder.append(": ").append(changeSubject)

        if (StringUtils.isNotBlank(changeBody)) {
            builder.append(System.lineSeparator()).append(System.lineSeparator())
            if (wrapText) {
                builder.append(WordUtils.wrap(changeBody, GitConstant.MAX_LINE_LENGTH))
            } else {
                builder.append(changeBody)
            }
        }

        if (StringUtils.isNotBlank(breakingChanges)) {
            val breakingContent = GitConstant.BREAKING_CHANGE_PREFIX + breakingChanges
            builder.append(System.lineSeparator()).append(System.lineSeparator())
            if (wrapText) {
                builder.append(WordUtils.wrap(breakingContent, GitConstant.MAX_LINE_LENGTH))
            } else {
                builder.append(breakingContent)
            }
        }

        if (StringUtils.isNotBlank(closedIssues)) {
            builder.append(System.lineSeparator())
            for (closedIssue: String in closedIssues.split(",")) {
                builder.append(System.lineSeparator()).append(GitConstant.CLOSES_PREFIX)
                    .append(formatClosedIssue(closedIssue))
            }
        }

        if (skipCI) {
            builder.append(System.lineSeparator()).append(System.lineSeparator())
            if (state.skipCI.isTwoEmpty()) {
                builder.append(System.lineSeparator())
            }
            builder.append(state.skipCI.label)
        }

        return builder.toString()
    }


    private fun formatClosedIssue(closedIssue: String): String {
        val issue = closedIssue.trim { it <= ' ' }
        if (StringUtils.isNumeric(issue)) {
            return "#$issue"
        }
        return issue
    }
}
