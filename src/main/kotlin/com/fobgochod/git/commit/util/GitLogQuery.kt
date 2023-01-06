package com.fobgochod.git.commit.util

import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

class GitLogQuery(private val project: Project?) {

    companion object {
        const val GIT_LOG_COMMAND: String = "git log --all --format=%s";
        val SCOPE_PATTERN: Pattern = Pattern.compile("^[a-z]+\\((.+)\\):.*");
    }

    fun execute(): Result {
        val basePath = File(project?.basePath)
        try {
            val osName: String = System.getProperty("os.name");
            val processBuilder: ProcessBuilder = if (osName.contains("Windows")) {
                ProcessBuilder("cmd", "/C", GIT_LOG_COMMAND);
            } else {
                ProcessBuilder("sh", "-c", GIT_LOG_COMMAND);
            }

            val process: Process = processBuilder
                .directory(basePath)
                .start();

            val reader = BufferedReader(InputStreamReader(process.inputStream));
            val output: List<String> = reader.lines().toList();

            process.waitFor(2, TimeUnit.SECONDS);
            process.destroy();
            process.waitFor();

            return Result(process.exitValue(), output);
        } catch (e: Exception) {
            return Result(-1);
        }
    }

    inner class Result(private val exitValue: Int) {

        private var logs: List<String> = emptyList()

        constructor(exitValue: Int, logs: List<String>) : this(exitValue) {
            this.logs = logs;
        }

        fun isSuccess(): Boolean {
            return exitValue == 0;
        }

        fun getScopes(): Set<String> {
            val scopes: Set<String> = HashSet();

            logs.forEach { log ->
                val matcher: Matcher = SCOPE_PATTERN.matcher(log);
                if (matcher.find()) {
                    scopes.plus(matcher.group(1));
                }
            }
            return scopes;
        }
    }

}
