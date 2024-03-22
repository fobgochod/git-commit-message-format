package com.fobgochod.git.commit.util

import com.fobgochod.git.commit.constant.GitConstant

/**
 * String Utils
 *
 * @author fobgochod
 * @since 2.0.11
 */
object StringUtils {

    @JvmStatic
    fun formatClosedIssue(closedIssue: String): String {
        val issue = closedIssue.trim()
        val issueNum = issue.trim('#').trim()
        return when {
            isNumeric(issue) -> "#$issue"
            isNumeric(issueNum) -> "#$issueNum"
            else -> GitConstant.EMPTY
        }
    }

    @JvmStatic
    fun isNumeric(str: String?): Boolean {
        if (str.isNullOrEmpty()) {
            return false
        }
        return str.all { it.isDigit() }
    }

    @JvmStatic
    fun isBlank(str: String?): Boolean {
        if (str.isNullOrEmpty()) {
            return true
        }
        return str.isBlank()
    }
}