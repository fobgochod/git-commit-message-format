package com.fobgochod.git.commit

import java.util.*

enum class ChangeType(val title: String, private val description: String) {
    FEAT("Features", "A new feature"),
    FIX("Bug Fixes", "A bug fix"),
    DOCS("Documentation", "Documentation only changes"),
    STYLE(
        "Styles",
        "Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)"
    ),
    REFACTOR("Code Refactoring", "A code change that neither fixes a bug nor adds a feature"),
    PERF("Performance Improvements", "A code change that improves performance"),
    TEST("Tests", "Adding missing tests or correcting existing tests"),
    BUILD(
        "Builds",
        "Changes that affect the build system or external dependencies (example scopes: gulp, broccoli, npm)"
    ),
    CI(
        "Continuous Integrations",
        "Changes to our CI configuration files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)"
    ),
    CHORE("Chores", "Other changes that don't modify src or test files"),
    REVERT("Reverts", "Reverts a previous commit");


    fun label(): String {
        return name.lowercase(Locale.getDefault());
    }

    override fun toString(): String {
        return this.label() + " - " + this.description;
    }
}
