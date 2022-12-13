package com.fobgochod.git.commit.domain;

public enum TypeEnum {

    feature("feature", "A new feature"),
    fix("fix", "A bug fix"),
    docs("docs", "Documentation only changes"),
    style("style", "Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)"),
    refactor("refactor", "A code change that neither fixes a bug nor adds a feature"),
    perf("perf", "A code change that improves performance"),
    test("test", "Adding missing tests or correcting existing tests"),
    build("build", "Changes that affect the build system or external dependencies (example scopes: gulp, broccoli, npm)"),
    ci("ci", "Changes to our CI configuration files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)"),
    chore("chore", "Other changes that don't modify src or test files"),
    revert("revert", "Reverts a previous commit");

    private final String title;
    private final String description;

    TypeEnum(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }
}
