package com.fobgochod.git.commit.constant

object GitCommitConstant {

    /**
     * https://stackoverflow.com/a/2120040/5138796
     */
    const val MAX_LINE_LENGTH = 72

    /**
     * 常用类型个数
     */
    const val COMMON_TYPE_COUNT = 3;

    const val BREAKING_CHANGE_PREFIX = "BREAKING CHANGE: "
    const val CLOSES_PREFIX = "Closes "
    const val SKIP_CI = "[skip ci]"

    val DEFAULT_TEMPLATE = """
                <type>(<scope>): <subject>
                <BLANK LINE>
                <body>
                <BLANK LINE>
                <footer>
            """.trimIndent()
}
