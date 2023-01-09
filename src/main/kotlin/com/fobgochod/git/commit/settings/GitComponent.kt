package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.view.TemplateEditor
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.tabs.JBTabs
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.impl.JBTabsImpl
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class GitComponent {

    /**
     * main panel
     */
    val mainPanel: JPanel = JPanel(BorderLayout())
    private val mainTabs: JBTabs

    /**
     * type标签
     */
    @Transient
    private val typeTab: TabInfo
    private val typePanel: JPanel = JPanel(BorderLayout())
    val typeTable: TypeTable = TypeTable()
    val commonCountField: JTextField = JTextField()

    /**
     * template标签
     */
    @Transient
    private val templateTab: TabInfo
    private var templateEditor: TemplateEditor

    init {
        // 1.type标签
        typePanel.add(
            ToolbarDecorator.createDecorator(typeTable)
                .setAddAction { typeTable.addRow() }
                .setRemoveAction { typeTable.removeRow() }
                .setEditAction { typeTable.editRow() }
                .setMoveUpAction { typeTable.moveUp() }
                .setMoveDownAction { typeTable.moveDown() }
                .addExtraAction(ResetTypeAction(typeTable))
                .addExtraAction(RestoreTypesAction(typeTable))
                .setToolbarPosition(ActionToolbarPosition.RIGHT)
                .createPanel(),
            BorderLayout.CENTER
        )
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                return typeTable.editRow()
            }
        }.installOn(typeTable)

        typePanel.add(
            FormBuilder.createFormBuilder()
                .addLabeledComponent(JLabel(GitBundle.message("settings.common.type.count")), commonCountField).panel,
            BorderLayout.SOUTH
        )

        val project = ProjectManager.getInstance().openProjects[0]
        // 2.template标签
        templateEditor = TemplateEditor(GitConstant.DEFAULT_TEMPLATE, project, XmlFileType.INSTANCE);

        // tabs
        mainTabs = JBTabsImpl(project)
        typeTab = TabInfo(typePanel)
        typeTab.text = GitBundle.message("settings.tabs.type")
        mainTabs.addTab(typeTab)

        templateTab = TabInfo(templateEditor.component)
        templateTab.text = GitBundle.message("settings.tabs.template")
        mainTabs.addTab(templateTab)

        // main
        mainPanel.add(JLabel(GitBundle.message("settings.desc.text")), BorderLayout.NORTH)
        mainPanel.add(mainTabs.component, BorderLayout.CENTER)
    }
}
