package com.fobgochod.git.commit.domain

/**
 * 类型
 *
 * @author fobgochod
 * @date 2022/12/11 23:16
 */
data class TypeRow(var title: String, var description: String) {

    override fun toString(): String {
        return String.format("%s - %s", title, description)
    }
}
