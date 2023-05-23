package com.fobgochod.git.commit.settings.v1

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.domain.option.ComponentType
import com.fobgochod.git.commit.domain.option.SkipCI
import com.fobgochod.git.commit.domain.option.ViewMode
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.SettingsCategory
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import java.util.*

@State(
    name = GitState1.NAME,
    storages = [Storage(GitState1.STORAGES)],
    category = SettingsCategory.PLUGINS
)
class GitState1 : PersistentStateComponent<GitState1> {

    var typeCount: Int = GitConstant.RADIO_BUTTON_TYPE_COUNT
    var skipCI: SkipCI = SkipCI.SKIP_CI
    var viewMode: ViewMode = ViewMode.Window
    var typeRows: MutableList<TypeRow> = LinkedList()
    val componentType: MutableMap<ComponentType, Boolean> = EnumMap(ComponentType::class.java)

    init {
        if (typeRows.isEmpty()) {
            for (type in CommitType.values()) {
                typeRows.add(TypeRow(type.type(), type.description()))
            }
        }
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

    fun isComponentHidden(componentType: ComponentType): Boolean {
        return this.componentType.getOrDefault(componentType, false)
    }

    fun isComponentShow(componentType: ComponentType): Boolean {
        return !isComponentHidden(componentType)
    }

    override fun getState(): GitState1 {
        return this
    }

    override fun loadState(state: GitState1) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {

        const val NAME = "GitCommitMessageFormat"
        const val STORAGES = "git.commit.message.format.xml"

        @JvmStatic
        fun getInstance(): GitState1 =
            ApplicationManager.getApplication().getService(GitState1::class.java)
    }
}
