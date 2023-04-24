package com.fobgochod.git.commit.util

import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.regex.Matcher
import java.util.regex.Pattern

class GitLog(private val project: Project?) {

    companion object {
        const val GIT_LOG_COMMAND: String = "git log --all --format=%s"
        val SCOPE_PATTERN: Pattern = Pattern.compile("^[a-z]+\\((.+)\\):.*")
    }

    fun execute(): Result {
        val basePath = File(project?.basePath)
        try {
            val osName: String = System.getProperty("os.name")
            val processBuilder: ProcessBuilder = if (osName.contains("Windows")) {
                ProcessBuilder("cmd", "/C", GIT_LOG_COMMAND)
            } else {
                ProcessBuilder("sh", "-c", GIT_LOG_COMMAND)
            }

            val process: Process = processBuilder.directory(basePath).start()
            val reader = BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))
            val output: List<String> = reader.lines().toList();

            process.destroy()

            return Result(process.exitValue(), output)
        } catch (e: Exception) {
            return Result(-1)
        }
    }

    inner class Result(exitValue: Int, private val logs: List<String> = emptyList()) {

        val scopes: MutableSet<String> = LinkedHashSet()

        init {
            scopes.add("")
            if (exitValue == 0) {
                initScopesByPattern();
            }
        }

        private fun initScopes() {
            logs.forEach { log ->
                val header = log.split(" ")[0]
                if (header.indexOf('(') > -1 && header.indexOf(')') > -1) {
                    scopes.add(header.substring(header.indexOf('(') + 1, header.indexOf(')')))
                }
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
