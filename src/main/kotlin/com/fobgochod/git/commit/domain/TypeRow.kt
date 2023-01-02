package com.fobgochod.git.commit.domain

import java.util.*

/**
 * 类型
 *
 * @author fobgochod
 * @date 2022/12/11 23:16
 */
class TypeRow(var title: String?, var description: String?) : Cloneable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val typeRow = other as TypeRow
        return title == typeRow.title && description == typeRow.description
    }

    override fun hashCode(): Int {
        return Objects.hash(title, description)
    }

    override fun toString(): String {
        return String.format("%s - %s", title, description)
    }

    public override fun clone(): TypeRow {
        return super.clone() as TypeRow
    }
}
