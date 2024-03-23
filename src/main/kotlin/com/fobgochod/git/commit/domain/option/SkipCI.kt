package com.fobgochod.git.commit.domain.option

/**
 * [Skipping workflow runs](https://docs.github.com/en/actions/managing-workflow-runs/skipping-workflow-runs)
 */
enum class SkipCI(val label: String) {

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

    companion object {

        fun isSelf(label: String): Boolean {
            values().forEach {
                if (it.label.equals(label, ignoreCase = true)) {
                    return true
                }
            }
            return false
        }
    }

    fun isTwoEmpty(): Boolean {
        return twoEmpty
    }

    override fun toString(): String {
        return this.name
    }
}
