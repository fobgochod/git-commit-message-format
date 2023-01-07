package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetAllTypesAction
import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.codeInsight.template.HtmlContextType
import com.intellij.codeInsight.template.impl.TemplateEditorUtil
import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.openapi.editor.Editor
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
    var templateEditor: Editor

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
                .addExtraAction(ResetAllTypesAction(typeTable))
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
                .addLabeledComponent(JLabel("common type count: "), commonCountField).panel,
            BorderLayout.SOUTH
        )

        // 2.template标签
        templateEditor = TemplateEditorUtil.createEditor(true, "")
        templateEditor.settings.isLineNumbersShown = true
        TemplateEditorUtil.setHighlighter(templateEditor, HtmlContextType())

        // tabs
        mainTabs = JBTabsImpl(ProjectManager.getInstance().openProjects[0])
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
