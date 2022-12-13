package com.fobgochod.git.commit.domain;

public enum MessageType {

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


    private final String key;
    private final String intro;

    MessageType(String key, String intro) {
        this.key = key;
        this.intro = intro;
    }

    public String key() {
        return key;
    }

    public String intro() {
        return intro;
    }
}
