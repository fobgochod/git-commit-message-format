package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.option.BlockType
import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.domain.option.SkipCI
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.util.StringUtils
import com.fobgochod.git.commit.util.WordUtils
import com.intellij.openapi.diagnostic.Logger

/**
 *
 * Represents a structured Git commit message following the Conventional Commits format:
 *
 *     <type>(<scope>): <subject>
 *
 *     <body>
 *
 *     <footer>
 *
 * Components:
 * - changeType:    The type of change (e.g., feat, fix).
 * - changeScope:   Optional scope of the change (e.g., module, component).
 * - changeSubject: Short, descriptive summary of the change.
 * - changeBody:    Detailed explanation of the change (optional).
 * - breakingChanges: Description of any breaking changes (optional).
 * - closedIssues:  A list of issues closed by this commit (e.g., Closes #123).
 * - skipCI:        Whether to skip CI for this commit (e.g., contains [skip ci]).
 *
 * This data class is typically populated by a parser that analyzes raw commit messages.
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

        /**
         * Parse raw commit message into a structured CommitMessage object.
         */
        fun parse(message: String): CommitMessage {
            val commit = CommitMessage()
            try {
                val messages = message.trim().lines().toMutableList()
                if (messages.isEmpty()) return commit

                // === 1. Parse header line ===
                val header = messages.first().trim()
                val matcher = GitConstant.HEADER_PATTERN.matcher(header)
                if (matcher.find()) {
                    // If matches Conventional Commit format: type(scope): subject
                    commit.changeType = state.getTypeFromName(matcher.group(1))
                    commit.changeScope = matcher.group(3) ?: GitConstant.EMPTY
                    commit.changeSubject = matcher.group(5) ?: GitConstant.EMPTY
                    messages.removeFirst()
                } else {
                    if (messages.size == 1
                        && !header.startsWith(GitConstant.BREAKING_CHANGE)
                        && !header.startsWith(GitConstant.CLOSES)
                        && !SkipCI.isSelf(header)
                    ) {
                        // If message has only one non-footer line, treat it as a plain subject
                        commit.changeSubject = header
                        messages.removeFirst()
                    }
                }

                // === 2. Parse body and footer ===
                // Split remaining message by empty lines (paragraphs)
                val messageGroup = messages.joinToString("\n")
                    .split(GitConstant.BLOCK_REGEX)
                    .map { it.trim().lines().filter { line -> line.isNotBlank() } }
                    .filter { it.isNotEmpty() }

                // === 3. Classify each paragraph block ===
                val changeBody = mutableListOf<String>()
                val breakingChanges = mutableListOf<String>()
                val closedIssues = mutableListOf<String>()
                var breaking = false
                messageGroup.forEach { block ->
                    when (classify(block)) {
                        BlockType.BREAKING -> {
                            breakingChanges.addAll(block)
                            breaking = true
                        }

                        BlockType.CLOSES -> closedIssues.addAll(block)
                        BlockType.SKIP_CI -> commit.skipCI = true
                        BlockType.BODY -> {
                            if (breaking) {
                                // After BREAKING_CHANGE line, remaining blocks are treated as breaking
                                breakingChanges.addAll(block)
                            } else {
                                changeBody.addAll(block)
                            }
                        }
                    }
                }

                // === 4. Assign parsed fields to commit object ===
                commit.changeBody = changeBody.joinToString(System.lineSeparator()).trim()
                commit.breakingChanges = breakingChanges
                    .joinToString(System.lineSeparator())
                    .replace(GitConstant.BREAKING_CHANGE, GitConstant.EMPTY)
                    .trim()
                commit.closedIssues = closedIssues
                    .map {
                        StringUtils.formatClosedIssue(
                            it.replace(GitConstant.CLOSES, GitConstant.EMPTY)
                        )
                    }
                    .filter { it.isNotBlank() }
                    .distinct().sorted()
                    .joinToString(GitConstant.COMMA)
            } catch (e: RuntimeException) {
                logger.error("Failed to parse commit message", e)
            }
            return commit
        }

        /**
         * Classify a paragraph block into its semantic type.
         * The classification is based on the first line of the block.
         */
        fun classify(block: List<String>): BlockType {
            val first = block.firstOrNull()?.trim() ?: return BlockType.BODY
            return when {
                first.startsWith(GitConstant.BREAKING_CHANGE) -> BlockType.BREAKING
                first.startsWith(GitConstant.CLOSES) -> BlockType.CLOSES
                SkipCI.isSelf(first) -> BlockType.SKIP_CI
                else -> BlockType.BODY
            }
        }

        /**
         * Automatically wrap commit message text by configured line length.
         * Only applies if wrapTextEnabled is true in settings.
         */
        fun wrapText(message: String): String {
            if (!state.wrapTextEnabled) return message

            return message.lineSequence()
                .flatMap { WordUtils.wrap(it, GitConstant.MAX_LINE_LENGTH).lineSequence() }
                .joinToString(System.lineSeparator())
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
