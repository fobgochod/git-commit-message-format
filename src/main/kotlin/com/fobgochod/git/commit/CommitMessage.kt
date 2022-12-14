package com.fobgochod.git.commit

import com.fobgochod.git.commit.domain.ChangeType
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils
import java.util.*
import java.util.regex.Pattern

class CommitMessage {

    companion object {
        private const val MAX_LINE_LENGTH = 72 // https://stackoverflow.com/a/2120040/5138796
        private val COMMIT_FIRST_LINE_FORMAT: Pattern = Pattern.compile("^([a-z]+)(\\((.+)\\))?: (.+)")
        private val COMMIT_CLOSES_FORMAT: Pattern = Pattern.compile("Closes (.+)")

        fun parse(message: String): CommitMessage {
            val commitMessage = CommitMessage()
            try {
                var matcher = COMMIT_FIRST_LINE_FORMAT.matcher(message)
                if (!matcher.find()) return commitMessage
                commitMessage.changeType = ChangeType.valueOf(matcher.group(1).uppercase(Locale.getDefault()))
                commitMessage.changeScope = matcher.group(3)
                commitMessage.shortDescription = matcher.group(4)
                val strings = message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (strings.size < 2) return commitMessage
                var pos = 1
                var stringBuilder: StringBuilder = StringBuilder()
                while (pos < strings.size) {
                    val lineString = strings[pos]
                    if (lineString.startsWith("BREAKING") || lineString.startsWith("Closes") || lineString.equals(
                            "[skip ci]", ignoreCase = true
                        )
                    ) break
                    stringBuilder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.longDescription = stringBuilder.toString().trim { it <= ' ' }

                stringBuilder = StringBuilder()
                while (pos < strings.size) {
                    val lineString = strings[pos]
                    if (lineString.startsWith("Closes") || lineString.equals("[skip ci]", ignoreCase = true)) break
                    stringBuilder.append(lineString).append('\n')
                    pos++
                }
                commitMessage.breakingChanges =
                    stringBuilder.toString().trim { it <= ' ' }.replace("BREAKING CHANGE: ", "")
                matcher = COMMIT_CLOSES_FORMAT.matcher(message)

                stringBuilder = StringBuilder()
                while (matcher.find()) {
                    stringBuilder.append(matcher.group(1)).append(',')
                }
                if (stringBuilder.isNotEmpty()) stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)
                commitMessage.closedIssues = stringBuilder.toString()
                commitMessage.skipCI = message.contains("[skip ci]")
            } catch (_: RuntimeException) {
            }
            return commitMessage
        }
    }


    var changeType: ChangeType = ChangeType.FEAT;
    var changeScope: String = "";
    var shortDescription: String = "";
    var longDescription: String;
    var breakingChanges: String;
    var closedIssues: String;
    private var wrapText: Boolean = true;
    var skipCI: Boolean = false;

    constructor() {
        this.longDescription = "";
        this.breakingChanges = "";
        this.closedIssues = "";
    }

    constructor(
        changeType: ChangeType,
        changeScope: String,
        shortDescription: String,
        longDescription: String,
        breakingChanges: String,
        closedIssues: String,
        wrapText: Boolean,
        skipCI: Boolean
    ) {
        this.changeType = changeType;
        this.changeScope = changeScope;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.breakingChanges = breakingChanges;
        this.closedIssues = closedIssues;
        this.wrapText = wrapText;
        this.skipCI = skipCI;
    }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder();
        builder.append(changeType.label());
        if (StringUtils.isNotBlank(changeScope)) {
            builder.append('(').append(changeScope).append(')');
        }
        builder.append(": ").append(shortDescription);

        if (StringUtils.isNotBlank(longDescription)) {
            builder.append(System.lineSeparator()).append(System.lineSeparator())
                .append(if (wrapText) WordUtils.wrap(longDescription, MAX_LINE_LENGTH) else longDescription);
        }

        if (StringUtils.isNotBlank(breakingChanges)) {
            val content = "BREAKING CHANGE: $breakingChanges";
            builder.append(System.lineSeparator()).append(System.lineSeparator())
                .append(if (wrapText) WordUtils.wrap(content, MAX_LINE_LENGTH) else content);
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
        val trimmed = closedIssue.trim { it <= ' ' }
        return (if (StringUtils.isNumeric(trimmed)) "#" else "") + trimmed
    }


    fun isSkipCI(): Boolean {
        return skipCI
    }
}
