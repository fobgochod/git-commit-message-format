package com.fobgochod.git.commit.domain

import java.util.*

enum class ChangeType(val title: String, val description: String) {
    FEAT("feat", "A new feature"),
    FIX("fix", "A bug fix"),
    DOCS(
        "docs", "Documentation only changes"
    ),
    STYLE(
        "style",
        "Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)"
    ),
    REFACTOR("refactor", "A code change that neither fixes a bug nor adds a feature"),
    PERF(
        "perf", "A code change that improves performance"
    ),
    TEST("test", "Adding missing tests or correcting existing tests"),
    BUILD(
        "build", "Changes that affect the build system or external dependencies (example scopes: gulp, broccoli, npm)"
    ),
    CI(
        "ci",
        "Changes to our CI configuration files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)"
    ),
    CHORE("chore", "Other changes that don't modify src or test files"),
    REVERT("revert", "Reverts a previous commit");

    fun label(): String {
        return name.lowercase(Locale.getDefault());
    }

    override fun toString(): String {
        return this.label() + " - " + this.description;
    }
}
