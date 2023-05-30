package com.fobgochod.git.commit

import java.util.regex.Pattern
import java.util.stream.IntStream

class DemoPattern {

    companion object {

        private val HEADER_PATTERN: Pattern = Pattern.compile("^([a-z]+)(\\((.+)?\\))?: (.+)?")

        /**
         * 匹配如下：
         * 1. feat(compile): hello world
         * 2. feat(compile):
         * 3. feat(): hello world
         * 4. feat:
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val message1 = "feat(compile): hello world"
            val message2 = "feat(compile): "
            val message3 = "feat(): hello world"
            val message4 = "feat: "

            val messages = listOf(message1, message2, message3, message4)

            messages.forEach { pattern(it) }
        }


        private fun pattern(message: String) {
            println("-------------------------------")
            println(message)
            val matcher = HEADER_PATTERN.matcher(message)

            if (matcher.find()) {
                IntStream.rangeClosed(0, matcher.groupCount())
                    .mapToObj { i: Int -> i.toString() + ". " + matcher.group(i) }
                    .forEach { println(it) }
            }
        }
    }
}


