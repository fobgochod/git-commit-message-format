package com.fobgochod.git.commit.constant

object GitCommitConstant {

    val DEFAULT_TEMPLATE = """
            ${'$'}type${'$'}(${'$'}scope${'$'}): ${'$'}subject${'$'}
                        
            ${'$'}body${'$'}
                        
            ${'$'}changes${'$'}
            """.trimIndent()
}
