package com.fobgochod.git.commit.constant

object GitCommitConstant {

    val DEFAULT_TEMPLATE = """
            ${'$'}type${'$'}(${'$'}scope${'$'}): ${'$'}subject${'$'}

            ${'$'}body${'$'}

            ${'$'}changes${'$'}
            """.trimIndent()


    val DEFAULT_TEMPLATE_ = """
                <type>(<scope>): <subject>
                <blank line>
                <body>
                <blank line>
                Breaking changes: <breaking>
                <blank line>
                Closes #<closed>
                <blank line>
            """.trimIndent()
}
