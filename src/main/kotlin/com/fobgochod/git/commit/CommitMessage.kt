package com.fobgochod.git.commit

import com.fobgochod.git.commit.constant.GitCommitConstant
import com.fobgochod.git.commit.settings.GitCommitHelperState
import com.intellij.openapi.diagnostic.Logger
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils
import java.util.regex.Pattern

class CommitMessage {

    companion object {
        private val logger = Logger.getInstance(CommitMessage::class.java)

        private val COMMIT_FIRST_LINE_FORMAT: Pattern = Pattern.compile("^([a-z]+)(\\((.+)\\))?: (.+)")
        private val COMMIT_CLOSED_ISSUE_FORMAT: Pattern = Pattern.compile("Closes (.+)")

        fun parse(message: String): CommitMessage {
            val commitMessage = CommitMessage()
            try {
                var matcher = COMMIT_FIRST_LINE_FORMAT.matcher(message)
                if (!matcher.find()) return commitMessage

                commitMessage.changeType = GitCommitHelperState.getInstance().getTypeFromName(matcher.group(1)).title
                commitMessage.changeScope = if (matcher.group(3) != null) matcher.group(3) else ""
                commitMessage.changeSubject = matcher.group(4)

                val messages = message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (messages.size < 2) {
                    return commitMessage
                }

                var pos = 1
                var builder: StringBuilder = StringBuilder()
                while (pos < messages.size) {
                    val lineString = messages[pos]
                    if (lineString.startsWith(GitCommitConstant.BREAKING_CHANGE_PREFIX)
                        || lineString.startsWith(GitCommitConstant.CLOSES_PREFIX)
                        || lineString.equals(GitCommitConstant.SKIP_CI, ignoreCase = true)
                    ) break
                    builder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.changeBody = builder.toString().trim { it <= ' ' }

                builder = StringBuilder()
                while (pos < messages.size) {
                    val lineString = messages[pos]
                    if (lineString.startsWith(GitCommitConstant.CLOSES_PREFIX)
                        || lineString.equals(GitCommitConstant.SKIP_CI, ignoreCase = true)
                    ) break
                    builder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.breakingChanges =
                    builder.toString().trim { it <= ' ' }.replace(GitCommitConstant.BREAKING_CHANGE_PREFIX, "")

                matcher = COMMIT_CLOSED_ISSUE_FORMAT.matcher(message)
                builder = StringBuilder()
                while (matcher.find()) {
                    builder.append(matcher.group(1)).append(',')
                }
                if (builder.isNotEmpty()) builder.delete(builder.length - 1, builder.length)
                commitMessage.closedIssues = builder.toString()

                commitMessage.skipCI = message.contains(GitCommitConstant.SKIP_CI)
            } catch (e: RuntimeException) {
                logger.error(e.message)
            }
            return commitMessage
        }
    }


    var changeType: String = ""
    var changeScope: String = "";
    var changeSubject: String = "";
    var changeBody: String = "";
    var wrapText: Boolean = true;
    var breakingChanges: String = "";
    var closedIssues: String = "";
    var skipCI: Boolean = false;

    constructor()

    constructor(
        changeType: String,
        changeScope: String,
        changeSubject: String,
        changeBody: String,
        wrapText: Boolean,
        breakingChanges: String,
        closedIssues: String,
        skipCI: Boolean
    ) {
        this.changeType = changeType;
        this.changeScope = changeScope;
        this.changeSubject = changeSubject;
        this.changeBody = changeBody;
        this.breakingChanges = breakingChanges;
        this.closedIssues = closedIssues;
        this.wrapText = wrapText;
        this.skipCI = skipCI;
    }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder();
        builder.append(changeType);
        if (StringUtils.isNotBlank(changeScope)) {
            builder.append('(').append(changeScope).append(')');
        }
        builder.append(": ").append(changeSubject);

        if (StringUtils.isNotBlank(changeBody)) {
            builder.append(System.lineSeparator()).append(System.lineSeparator())
            if (wrapText) {
                builder.append(WordUtils.wrap(changeBody, GitCommitConstant.MAX_LINE_LENGTH))
            } else {
                builder.append(changeBody)
            }
        }

        if (StringUtils.isNotBlank(breakingChanges)) {
            val breakingContent = GitCommitConstant.BREAKING_CHANGE_PREFIX + breakingChanges;
            builder.append(System.lineSeparator()).append(System.lineSeparator())
            if (wrapText) {
                builder.append(WordUtils.wrap(breakingContent, GitCommitConstant.MAX_LINE_LENGTH))
            } else {
                builder.append(breakingContent)
            }
        }

        if (StringUtils.isNotBlank(closedIssues)) {
            builder.append(System.lineSeparator());
            for (closedIssue: String in closedIssues.split(",")) {
                builder.append(System.lineSeparator()).append("Closes ").append(formatClosedIssue(closedIssue));
            }
        }

        if (skipCI) {
            builder.append(System.lineSeparator()).append(System.lineSeparator()).append("[skip ci]");
        }

        return builder.toString();
    }


    private fun formatClosedIssue(closedIssue: String): String {
        val issue = closedIssue.trim { it <= ' ' }
        if (StringUtils.isNumeric(issue)) {
            return "#$issue"
        }
        return issue
    }
}
