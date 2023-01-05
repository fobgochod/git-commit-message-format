package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetChangeTypeAction
import com.fobgochod.git.commit.domain.TypeTable
import com.intellij.codeInsight.template.HtmlContextType
import com.intellij.codeInsight.template.impl.TemplateEditorUtil
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.ex.EditorEx
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


class GitCommitHelperComponent : Disposable {

    /**
     * main panel
     */
    val mainPanel: JPanel = JPanel(BorderLayout())
    private val tabbedPane: JBTabs

    /**
     * type标签
     */
    @Transient
    private val typeTab: TabInfo
    private val typePanel: JPanel = JPanel(BorderLayout())
    val typeTable: TypeTable = TypeTable()

    /**
     * template标签
     */
    @Transient
    private val templateTab: TabInfo
    var templateEditor: EditorEx
    val countTextField: JTextField = JTextField()

    init {
        // 1.type标签
        typePanel.add(
            ToolbarDecorator.createDecorator(typeTable)
                .setAddAction { typeTable.addRow() }
                .setRemoveAction { typeTable.removeRow() }
                .setEditAction { typeTable.editRow() }
                .setMoveUpAction { typeTable.moveUp() }
                .setMoveDownAction { typeTable.moveDown() }
                .addExtraAction(ResetChangeTypeAction(typeTable))
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
                .addLabeledComponent(JLabel("common type count: "), countTextField).panel,
            BorderLayout.SOUTH
        )

        // 2.template标签
        templateEditor = TemplateEditorUtil.createEditor(true, "") as EditorEx
        templateEditor.settings.isLineNumbersShown = true
        TemplateEditorUtil.setHighlighter(templateEditor, HtmlContextType())

        // tabs
        val project = ProjectManager.getInstance().openProjects[0]
        tabbedPane = JBTabsImpl(project)
        typeTab = TabInfo(typePanel)
        typeTab.text = "type"
        tabbedPane.addTab(typeTab)

        templateTab = TabInfo(templateEditor.component)
        templateTab.text = "template"
        tabbedPane.addTab(templateTab)

        // main
        mainPanel.add(JLabel("Personalize your git commit types and template."), BorderLayout.NORTH)
        mainPanel.add(tabbedPane.component, BorderLayout.CENTER)
    }

    override fun dispose() {
    }
}
