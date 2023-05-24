package com.fobgochod.git.commit.domain

/**
 * commit type row
 *
 * @author fobgochod
 * @since 2022/12/11 23:16
 */
data class TypeRow(
    var name: String = "",
    var description: String = ""
) {

    override fun toString(): String {
        return String.format("%s - %s", name, description)
    }
}
