package com.fobgochod.git.commit.util

import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors

class GitUtil(private val project: Project) {

    companion object {
        const val GIT_LOG_COMMAND: String = "git log --all --format=%s"
        val SCOPE_PATTERN: Pattern = Pattern.compile("^[a-z]+\\((.+)\\):.*")
    }

    fun logs(): Result {
        try {
            val basePath = project.basePath?.let { File(it) }
            if (basePath != null) {
                val osName: String = System.getProperty("os.name")
                val processBuilder: ProcessBuilder = if (osName.contains("Windows")) {
                    ProcessBuilder("cmd", "/C", GIT_LOG_COMMAND)
                } else {
                    ProcessBuilder("sh", "-c", GIT_LOG_COMMAND)
                }

                val process: Process = processBuilder.directory(basePath).start()
                val reader = BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))
                val logs: List<String> = reader.lines().collect(Collectors.toList())

                return Result(process.exitValue(), logs)
            }
        } catch (_: Exception) {
        }
        return Result(-1)
    }

    class Result(exitValue: Int, private val logs: List<String> = emptyList()) {

        val scopes: MutableSet<String> = LinkedHashSet()

        init {
            if (exitValue == 0) {
                initScopesByPattern()
            }
        }

        private fun initScopesByPattern() {
            logs.forEach { log ->
                val matcher: Matcher = SCOPE_PATTERN.matcher(log)
                if (matcher.find()) {
                    scopes.add(matcher.group(1))
                }
            }
        }
    }
}
