package com.fobgochod.git.commit

import com.fobgochod.git.commit.domain.ChangeType
import com.intellij.openapi.diagnostic.logger
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils
import java.util.*
import java.util.regex.Pattern

class CommitMessage {

    companion object {
        private const val MAX_LINE_LENGTH = 72 // https://stackoverflow.com/a/2120040/5138796
        private val COMMIT_FIRST_LINE_FORMAT: Pattern = Pattern.compile("^([a-z]+)(\\((.+)\\))?: (.+)")
        private val COMMIT_CLOSED_ISSUE_FORMAT: Pattern = Pattern.compile("Closes (.+)")

        fun parse(message: String): CommitMessage {
            val commitMessage = CommitMessage()
            try {
                var matcher = COMMIT_FIRST_LINE_FORMAT.matcher(message)
                if (!matcher.find()) return commitMessage
                commitMessage.changeType = ChangeType.valueOf(matcher.group(1).uppercase(Locale.getDefault())).title
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
                    if (lineString.startsWith("Breaking") || lineString.startsWith("Closes") || lineString.equals(
                            "[skip ci]",
                            ignoreCase = true
                        )
                    ) break
                    builder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.changeBody = builder.toString().trim { it <= ' ' }

                builder = StringBuilder()
                while (pos < messages.size) {
                    val lineString = messages[pos]
                    if (lineString.startsWith("Closes") || lineString.equals("[skip ci]", ignoreCase = true)) break
                    builder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.breakingChanges = builder.toString().trim { it <= ' ' }.replace("Breaking Changes: ", "")

                matcher = COMMIT_CLOSED_ISSUE_FORMAT.matcher(message)
                builder = StringBuilder()
                while (matcher.find()) {
                    builder.append(matcher.group(1)).append(',')
                }
                if (builder.isNotEmpty()) builder.delete(builder.length - 1, builder.length)
                commitMessage.closedIssues = builder.toString()
                commitMessage.skipCI = message.contains("[skip ci]")
            } catch (e: RuntimeException) {
                logger<String>().error(e.message)
            }
            return commitMessage
        }
    }


    var changeType: String = ""
    var changeScope: String = "";
    var changeSubject: String = "";
    var changeBody: String = "";
    var breakingChanges: String = "";
    var closedIssues: String = "";
    private var wrapText: Boolean = true;
    var skipCI: Boolean = false;

    constructor()

    constructor(
        changeType: String,
        changeScope: String,
        changeSubject: String,
        changeBody: String,
        breakingChanges: String,
        closedIssues: String,
        wrapText: Boolean,
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
                builder.append(WordUtils.wrap(changeBody, MAX_LINE_LENGTH))
            } else {
                builder.append(changeBody)
            }
        }

        if (StringUtils.isNotBlank(breakingChanges)) {
            val breakingContent = "Breaking Changes: $breakingChanges";
            builder.append(System.lineSeparator()).append(System.lineSeparator())
            if (wrapText) {
                builder.append(WordUtils.wrap(breakingContent, MAX_LINE_LENGTH))
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
