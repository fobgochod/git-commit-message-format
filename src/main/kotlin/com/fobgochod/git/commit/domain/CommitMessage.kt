package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.domain.option.SkipCI
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.util.StringUtils
import com.fobgochod.git.commit.util.WordUtils
import com.intellij.openapi.diagnostic.Logger

/**
 * <type>(<scope>): <subject>
 * <BLANK LINE>
 * <body>
 * <BLANK LINE>
 * <footer>
 *
 * @author fobgochod
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
                val messages = message.split(GitConstant.NEWLINE).dropWhile { it.isEmpty() }.toMutableList()
                if (messages.isEmpty()) return commit

                // header handler
                val header = messages.first()
                val matcher = GitConstant.HEADER_PATTERN.matcher(header)
                if (matcher.find()) {
                    commit.changeType = state.getTypeFromName(matcher.group(1))
                    commit.changeScope = matcher.group(3) ?: GitConstant.EMPTY
                    commit.changeSubject = matcher.group(5) ?: GitConstant.EMPTY
                    // match ok, remove first line
                    messages.removeFirst()
                }

                // body and footer handler
                val messageGroup = messages.flatMapIndexed { index, it ->
                    when {
                        index == 0 && messages.size == 1 -> listOf(0, 0)
                        index == 0 || index == messages.lastIndex -> listOf(index)
                        it.isEmpty() -> listOf(index - 1, index + 1)
                        else -> emptyList()
                    }
                }
                    .windowed(size = 2, step = 2) { (from, to) -> messages.slice(from..to) }
                    .filter { it.isNotEmpty() }

                // analysis group data
                messageGroup.forEach { row ->
                    val firstRow = row[0]
                    if (firstRow.startsWith(GitConstant.BREAKING_CHANGE)) {
                        commit.breakingChanges = row.joinToString(System.lineSeparator())
                            .replace(GitConstant.BREAKING_CHANGE, GitConstant.EMPTY)
                            .trim()
                    } else if (firstRow.startsWith(GitConstant.CLOSES)) {
                        commit.closedIssues = row.map {
                            StringUtils.formatClosedIssue(it.replace(GitConstant.CLOSES, GitConstant.EMPTY))
                        }.filter { it.isNotBlank() }.joinToString(GitConstant.COMMA)
                    } else if (SkipCI.isSelf(firstRow.trim())) {
                        commit.skipCI = true
                    } else {
                        commit.changeBody = row.joinToString(System.lineSeparator()).trim()
                    }
                }
            } catch (e: RuntimeException) {
                logger.error(e.message)
            }
            return commit
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
        if (changeScope.isNotBlank() && !state.hideScope) {
            builder.append('(').append(changeScope).append(')')
        }
        builder.append(GitConstant.COLON_EMPTY)
        builder.append(changeSubject)

        // body
        if (changeBody.isNotBlank() && !state.hideBody) {
            builder.append(System.lineSeparator())
            builder.append(System.lineSeparator())
            builder.append(wrapText(changeBody))
        }

        // footer
        if (breakingChanges.isNotBlank() && !state.hideBreaking) {
            builder.append(System.lineSeparator())
            builder.append(System.lineSeparator())
            builder.append(wrapText(GitConstant.BREAKING_CHANGE + breakingChanges))
        }

        if (closedIssues.isNotBlank() && !state.hideIssues) {
            builder.append(System.lineSeparator())
            closedIssues.split(GitConstant.COMMA)
                .map { StringUtils.formatClosedIssue(it) }
                .filter { it.isNotBlank() }
                .distinct()
                .forEach {
                    builder.append(System.lineSeparator())
                        .append(GitConstant.CLOSES + GitConstant.SPACE)
                        .append(it)
                }
        }

        if (skipCI && !state.hideSkipCI) {
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
