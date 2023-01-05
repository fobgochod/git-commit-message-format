package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.constant.GitCommitConstant
import com.fobgochod.git.commit.domain.ChangeType
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
    name = GitCommitHelperState.NAME,
    storages = [Storage(GitCommitHelperState.STORAGES)]
)
class GitCommitHelperState : PersistentStateComponent<GitCommitHelperState?> {

    var count: Int = GitCommitConstant.COMMON_TYPE_COUNT
    var template: String = GitCommitConstant.DEFAULT_TEMPLATE
    var typeRows: MutableList<TypeRow> = LinkedList()

    init {
        if (typeRows.isEmpty()) {
            for (type in ChangeType.values()) {
                typeRows.add(TypeRow(type.title(), type.description()))
            }
        }
    }

    override fun getState(): GitCommitHelperState {
        return this
    }

    override fun loadState(state: GitCommitHelperState) {
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
        fun getInstance(): GitCommitHelperState =
            ApplicationManager.getApplication().getService(GitCommitHelperState::class.java)
    }
}
