package com.fobgochod.git.commit.domain

/**
 * [Skipping workflow runs](https://docs.github.com/en/actions/managing-workflow-runs/skipping-workflow-runs)
 */
enum class SkipCIWord(val label: String) {

    SKIP_CI("[skip ci]"),
    CI_SKIP("[ci skip]"),
    NO_CI("[no ci]"),
    SKIP_ACTIONS("[skip actions]"),
    ACTIONS_SKIP("[actions skip]"),
    SKIP_CHECKS("skip-checks:true", true),
    SKIP_CHECKS_("skip-checks: true", true);

    private var twoEmpty: Boolean = false

    constructor(label: String, twoEmpty: Boolean) : this(label) {
        this.twoEmpty = twoEmpty
    }

    fun isTwoEmpty(): Boolean {
        return twoEmpty
    }

    override fun toString(): String {
        return this.name
    }
}
