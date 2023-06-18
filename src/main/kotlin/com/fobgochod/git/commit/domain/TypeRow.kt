package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.domain.option.CommitType

/**
 * commit type row
 *
 * @author fobgochod
 * @date 2022/12/11 23:16
 */
data class TypeRow(
    var name: String = "",
    var description: String = ""
) {

    constructor(commitType: CommitType) : this() {
        this.name = commitType.type()
        this.description = commitType.description()
    }

    override fun toString(): String {
        return String.format("%s - %s", name, description)
    }
}
