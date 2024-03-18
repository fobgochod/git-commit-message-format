package com.fobgochod.git.commit.constant

import java.util.regex.Pattern

object GitConstant {

    const val EMPTY = ""
    const val COMMA = ","
    const val COLON_EMPTY = ": "
    const val NEWLINE = "\n"

    const val RADIO_BUTTON_TYPE_COUNT = 3

    /**
     * [How to wrap git commit comments?](https://stackoverflow.com/a/2120040/5138796)
     */
    const val MAX_LINE_LENGTH = 72

    const val BREAKING_CHANGE = "BREAKING CHANGE: "
    const val CLOSES = "Closes "

    val HEADER_PATTERN: Pattern = Pattern.compile("^([a-zA-Z0-9\\u4e00-\\u9fa5]+)(\\((.+)?\\))?[:：]( )?(.+)?")

    /**
     * commit ui default size
     */
    const val PREFERRED_WIDTH: Int = 800
}
