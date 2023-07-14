package com.fobgochod.git.commit

import junit.framework.TestCase
import org.apache.commons.lang.WordUtils

class WrapBodyTest : TestCase() {

    fun testCase1() {
        val message = "Create a commit message with the following template."

        val wrapText = WordUtils.wrap(message, 20).split(System.lineSeparator())
        assertTrue(wrapText[0] == "Create a commit")
        assertTrue(wrapText[1] == "message with the")
        assertTrue(wrapText[2] == "following template.")
    }
}
