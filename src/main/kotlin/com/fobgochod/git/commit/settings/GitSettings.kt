package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.domain.option.SkipCI
import com.fobgochod.git.commit.domain.option.ViewMode
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = GitSettings.NAME, storages = [Storage(GitSettings.STORAGES)], category = SettingsCategory.TOOLS)
class GitSettings : PersistentStateComponent<GitSettingsState> {

    private val state = GitSettingsState()

    override fun getState() = state

    override fun loadState(state: GitSettingsState) = XmlSerializerUtil.copyBean(state, this.state)

    var typeRows: MutableList<TypeRow>
        get() {
            return if (state.typeRows.isEmpty()) {
                CommitType.typeRows
            } else {
                state.typeRows
            }
        }
        set(value) {
            if (value == CommitType.typeRows) {
                state.typeRows.clear()
            } else {
                state.typeRows.clear()
                state.typeRows.addAll(value)
            }
        }

    var typeCount: Int
        get() = state.typeCount
        set(value) {
            state.typeCount = value
        }

    var viewMode: ViewMode
        get() = state.viewMode
        set(value) {
            state.viewMode = value
        }

    var skipCI: SkipCI
        get() = state.skipCI
        set(value) {
            state.skipCI = value
        }

    var wrapTextEnabled: Boolean
        get() = state.wrapTextEnabled
        set(value) {
            state.wrapTextEnabled = value
        }

    var hideTypeGroup: Boolean
        get() = state.hideTypeGroup
        set(value) {
            state.hideTypeGroup = value
        }

    var hideType: Boolean
        get() = state.hideType
        set(value) {
            state.hideType = value
        }

    var hideScope: Boolean
        get() = state.hideScope
        set(value) {
            state.hideScope = value
        }

    var hideSubject: Boolean
        get() = state.hideSubject
        set(value) {
            state.hideSubject = value
        }

    var hideBody: Boolean
        get() = state.hideBody
        set(value) {
            state.hideBody = value
        }

    var hideWrapText: Boolean
        get() = state.hideWrapText
        set(value) {
            state.hideWrapText = value
        }

    var hideBreaking: Boolean
        get() = state.hideBreaking
        set(value) {
            state.hideBreaking = value
        }

    var hideIssues: Boolean
        get() = state.hideIssues
        set(value) {
            state.hideIssues = value
        }

    var hideSkipCI: Boolean
        get() = state.hideSkipCI
        set(value) {
            state.hideSkipCI = value
        }

    var scopeEnabled: Boolean
        get() = state.scopeEnabled
        set(value) {
            state.scopeEnabled = value
        }

    var scopeRows: MutableList<String>
        get() = state.scopeRows
        set(value) {
            state.scopeRows = value
        }

    fun isValidRow(index: Int): Boolean {
        return index >= 0 && index < typeRows.size
    }

    fun getTypeFromName(name: String): TypeRow {
        val name0: String = name.split("-")[0].trim()
        for (typeRow in typeRows) {
            if (name0 == typeRow.name) {
                return typeRow
            }
        }
        return typeRows[0]
    }

    companion object {

        const val NAME = "GitCommitMessageFormat"
        const val STORAGES = "git.commit.message.format.xml"

        @JvmStatic
        val instance: GitSettings
            get() = service()
    }
}
