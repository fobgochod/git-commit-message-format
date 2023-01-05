package com.fobgochod.git.commit.domain

/**
 * 类型
 *
 * @author fobgochod
 * @date 2022/12/11 23:16
 */
class TypeRow(var title: String = "", var description: String = "") : Cloneable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TypeRow

        if (title != other.title) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun toString(): String {
        return String.format("%s - %s", title, description)
    }

    public override fun clone(): TypeRow {
        return super.clone() as TypeRow
    }
}
