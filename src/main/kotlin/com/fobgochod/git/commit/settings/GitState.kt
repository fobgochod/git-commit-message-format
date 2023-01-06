package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.TypeEnum
import com.fobgochod.git.commit.domain.TypeRow
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import java.util.*

/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
    name = GitState.NAME,
    storages = [Storage(GitState.STORAGES)]
)
class GitState : PersistentStateComponent<GitState?> {

    var commonCount: Int = GitConstant.COMMON_TYPE_COUNT
    var template: String = GitConstant.DEFAULT_TEMPLATE
    var typeRows: MutableList<TypeRow> = LinkedList()

    init {
        if (typeRows.isEmpty()) {
            for (type in TypeEnum.values()) {
                typeRows.add(TypeRow(type.title(), type.description()))
            }
        }
    }

    override fun getState(): GitState {
        return this
    }

    override fun loadState(state: GitState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun getTypeFromName(title: String): TypeRow {
        val name: String = title.split("-".toRegex())[0].trim { it <= ' ' }
        for (typeRow in typeRows) {
            if (name == typeRow.title) {
                return typeRow
            }
        }
        return typeRows[0]
    }

    companion object {

        const val NAME = "GitCommitHelper"
        const val STORAGES = "git.commit.helper.xml"

        // CommandLineProjectOpenProcessor
        // PlatformProjectOpenProcessor
        @JvmStatic
        fun getInstance(): GitState =
            ApplicationManager.getApplication().getService(GitState::class.java)
    }
}