package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.settings.GitSettings
import com.intellij.openapi.diagnostic.Logger
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils

/**
 * <type>(<scope>): <subject>
 * <BLANK LINE>
 * <body>
 * <BLANK LINE>
 * <footer>
 *
 * @author fobgochod
 * @date 2023/7/13 22:00
 */
data class CommitMessage(
    var changeType: TypeRow = TypeRow(CommitType.FEAT),
    var changeScope: String = "",
    var changeSubject: String = "",
    var changeBody: String = "",
    var breakingChanges: String = "",
    var closedIssues: String = "",
    var skipCI: Boolean = false
) {

    companion object {
        private val logger = Logger.getInstance(CommitMessage::class.java)
        private val state: GitSettings = GitSettings.instance

        fun parse(message: String): CommitMessage {
            val commit = CommitMessage()
            try {
                val messages = message.split(GitConstant.NEWLINE).toList().dropWhile { it.isEmpty() }
                val messageGroup = messages
                    .flatMapIndexed { index, it ->
                        when {
                            index == 0 && messages.size == 1 -> listOf(0, 0)
                            index == 0 || index == messages.lastIndex -> listOf(index)
                            it.isEmpty() -> listOf(index - 1, index + 1)
                            else -> emptyList()
                        }
                    }
                    .windowed(size = 2, step = 2) { (from, to) -> messages.slice(from..to) }
                    .filter { it.isNotEmpty() }

                // analysis one line data
                messageGroup.filter { it.size == 1 }.map { it[0] }
                    .forEachIndexed { index, row ->
                        if (index == 0) {
                            val matcher = GitConstant.HEADER_PATTERN.matcher(row)
                            if (matcher.find()) {
                                commit.changeType = state.getTypeFromName(matcher.group(1))
                                commit.changeScope = matcher.group(3) ?: GitConstant.EMPTY
                                commit.changeSubject = matcher.group(5) ?: GitConstant.EMPTY
                            }
                        } else if (row.startsWith(GitConstant.BREAKING_CHANGE)) {
                            commit.breakingChanges = row.replace(GitConstant.BREAKING_CHANGE, GitConstant.EMPTY).trim()
                        } else if (row.startsWith(GitConstant.CLOSES)) {
                            commit.closedIssues = formatClosedIssue(row.replace(GitConstant.CLOSES, GitConstant.EMPTY))
                        } else if (row.equals(state.skipCI.label, ignoreCase = true)) {
                            commit.skipCI = true
                        } else {
                            commit.changeBody = row.trim()
                        }
                    }

                // analysis multi line data
                messageGroup.filter { it.size > 1 }.forEach { row ->
                    val firstRow = row[0]
                    if (firstRow.startsWith(GitConstant.BREAKING_CHANGE)) {
                        commit.breakingChanges = row.joinToString(System.lineSeparator())
                            .replace(GitConstant.BREAKING_CHANGE, GitConstant.EMPTY)
                            .trim()
                    } else if (firstRow.startsWith(GitConstant.CLOSES)) {
                        commit.closedIssues = row.map {
                            formatClosedIssue(it.replace(GitConstant.CLOSES, GitConstant.EMPTY))
                        }.filter { it.isNotBlank() }.joinToString(GitConstant.COMMA)
                    } else {
                        commit.changeBody = row.joinToString(System.lineSeparator()).trim()
                    }
                }
            } catch (e: RuntimeException) {
                logger.error(e.message)
            }
            return commit
        }

        fun formatClosedIssue(closedIssue: String): String {
            val issue = closedIssue.trim()
            val issueNum = issue.trim('#').trim()
            return when {
                StringUtils.isNumeric(issue) -> "#$issue"
                StringUtils.isNumeric(issueNum) -> "#$issueNum"
                else -> GitConstant.EMPTY
            }
        }

        fun wrapText(message: String): String {
            return when {
                state.wrapTextEnabled -> message.split(System.lineSeparator()).toList()
                    .flatMap { WordUtils.wrap(it, GitConstant.MAX_LINE_LENGTH).split(System.lineSeparator()).toList() }
                    .joinToString(System.lineSeparator())

                else -> message
            }
        }
    }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        // header
        builder.append(changeType.name)
        if (changeScope.isNotBlank()) {
            builder.append('(').append(changeScope).append(')')
        }
        builder.append(GitConstant.COLON_EMPTY)
        builder.append(changeSubject)

        // body
        if (changeBody.isNotBlank()) {
            builder.append(System.lineSeparator())
            builder.append(System.lineSeparator())
            builder.append(wrapText(changeBody))
        }

        // footer
        if (breakingChanges.isNotBlank()) {
            builder.append(System.lineSeparator())
            builder.append(System.lineSeparator())
            builder.append(wrapText(GitConstant.BREAKING_CHANGE + breakingChanges))
        }

        if (closedIssues.isNotBlank()) {
            builder.append(System.lineSeparator())
            closedIssues.split(GitConstant.COMMA)
                .map { formatClosedIssue(it) }
                .filter { it.isNotBlank() }
                .distinct()
                .forEach {
                    builder.append(System.lineSeparator())
                        .append(GitConstant.CLOSES)
                        .append(it)
                }
        }

        if (skipCI) {
            builder.append(System.lineSeparator())
            builder.append(System.lineSeparator())
            if (state.skipCI.isTwoEmpty()) {
                builder.append(System.lineSeparator())
            }
            builder.append(state.skipCI.label)
        }

        return builder.toString()
    }
}
