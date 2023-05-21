package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.domain.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.SettingsCategory
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = GitState.NAME,
    storages = [Storage(GitState.STORAGES)],
    category = SettingsCategory.PLUGINS
)
class GitState : PersistentStateComponent<UIGitState> {

    private var state = UIGitState()

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

    var skipCI: SkipCIWord
        get() = state.skipCI
        set(value) {
            state.skipCI = value
        }

    var viewMode: ViewMode
        get() = state.viewMode
        set(value) {
            state.viewMode = value
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

    fun isViewFormHidden(viewForm: ViewForm): Boolean {
        return when (viewForm) {
            ViewForm.TypeGroup -> hideTypeGroup
            ViewForm.Type -> hideType
            ViewForm.Scope -> hideScope
            ViewForm.Subject -> hideSubject
            ViewForm.Body -> hideBody
            ViewForm.WrapText -> hideWrapText
            ViewForm.Breaking -> hideBreaking
            ViewForm.Issues -> hideIssues
            ViewForm.SkipCI -> hideSkipCI
        }
    }

    fun isViewFormShow(viewForm: ViewForm): Boolean {
        return !isViewFormHidden(viewForm)
    }

    override fun getState(): UIGitState {
        return state
    }

    override fun loadState(state: UIGitState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    companion object {

        const val NAME = "GitCommitMessageFormat"
        const val STORAGES = "git.commit.message.format.xml"

        @JvmStatic
        fun getInstance(): GitState =
            ApplicationManager.getApplication().getService(GitState::class.java)
    }
}
