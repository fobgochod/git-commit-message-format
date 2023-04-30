package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.CommitType
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.ViewForm
import com.fobgochod.git.commit.domain.ViewMode
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import java.util.*

@State(
    name = GitState.NAME,
    storages = [Storage(GitState.STORAGES)]
)
class GitState : PersistentStateComponent<GitState> {

    var typeCount: Int = GitConstant.RADIO_BUTTON_TYPE_COUNT
    var typeRows: MutableList<TypeRow> = LinkedList()
    var viewMode: ViewMode = ViewMode.Window
    val viewForm: MutableMap<ViewForm, Boolean> = EnumMap(ViewForm::class.java)

    init {
        if (typeRows.isEmpty()) {
            for (type in CommitType.values()) {
                typeRows.add(TypeRow(type.type(), type.description()))
            }
        }
    }

    override fun getState(): GitState {
        return this
    }

    override fun loadState(state: GitState) {
        XmlSerializerUtil.copyBean(state, this)
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
        return this.viewForm.getOrDefault(viewForm, false)
    }

    fun isViewFormShow(viewForm: ViewForm): Boolean {
        return !this.viewForm.getOrDefault(viewForm, false)
    }

    companion object {

        const val NAME = "GitCommitMessageFormat"
        const val STORAGES = "git.commit.message.format.xml"

        @JvmStatic
        fun getInstance(): GitState =
            ApplicationManager.getApplication().getService(GitState::class.java)
    }
}
