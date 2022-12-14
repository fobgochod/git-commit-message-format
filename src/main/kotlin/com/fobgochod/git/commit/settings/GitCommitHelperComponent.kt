package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.TypeTable
import com.intellij.codeInsight.template.HtmlContextType
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.impl.TemplateEditorUtil
import com.intellij.codeInsight.template.impl.TemplateImpl
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.ui.AnActionButton
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.tabs.JBTabs
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.impl.JBTabsImpl
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.MouseEvent
import java.util.*
import java.util.stream.IntStream
import javax.swing.JLabel
import javax.swing.JPanel


class GitCommitHelperComponent : Disposable {

    private val state: GitCommitHelperState = GitCommitHelperState.getInstance();
    var typeRows: MutableList<TypeRow> = LinkedList()

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
    private val typePanel: JPanel
    val typeTable: TypeTable

    /**
     * template标签
     */
    @Transient
    private val templateTab: TabInfo
    private val templatePanel: JBSplitter
    var templateEditor: Editor

    init {
        mainPanel.add(JLabel("Personalize your git commit types and templates ."), BorderLayout.NORTH)
        tabbedPane = JBTabsImpl(null, null, this)
        mainPanel.add(tabbedPane.component, BorderLayout.CENTER)
        typePanel = JPanel(BorderLayout())
        typeTable = TypeTable()
        typePanel.add(
            ToolbarDecorator.createDecorator(typeTable)
                .setAddAction { _: AnActionButton? -> typeTable.addRow() }
                .setRemoveAction { _: AnActionButton? -> typeTable.removeRow() }
                .setEditAction { _: AnActionButton? -> typeTable.editRow() }
                .setMoveUpAction { _: AnActionButton? -> typeTable.moveUp() }
                .setMoveDownAction { _: AnActionButton? -> typeTable.moveDown() }
                .addExtraAction(
                    object : AnAction("Reset", "Reset Default Aliases", AllIcons.Actions.Rollback) {
                        override fun actionPerformed(anActionEvent: AnActionEvent) {
                            typeTable.resetRow()
                        }
                    })
                .createPanel(), BorderLayout.CENTER
        )
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                return typeTable.editRow()
            }
        }.installOn(typeTable)
        GIT_COMMIT_TEMPLATE.string = state.template
        GIT_COMMIT_TEMPLATE.addVariable("type", ConstantNode("type"), true)
        GIT_COMMIT_TEMPLATE.addVariable("scope", "", "scope", true)
        GIT_COMMIT_TEMPLATE.addVariable("subject", "", "subject", true)
        GIT_COMMIT_TEMPLATE.addVariable("body", "", "body", true)
        GIT_COMMIT_TEMPLATE.addVariable("changes", "", "changes", true)
        GIT_COMMIT_TEMPLATE.addVariable("closes", "", "closes", true)
        templateEditor =
            TemplateEditorUtil.createEditor(false, GIT_COMMIT_TEMPLATE.string, GIT_COMMIT_TEMPLATE.createContext())
        TemplateEditorUtil.setHighlighter(templateEditor, HtmlContextType())
        val upPanel = JPanel(BorderLayout())
        upPanel.add(JLabel("Template variables:"), BorderLayout.NORTH)
        val templateVariables = JPanel(GridBagLayout())
        upPanel.add(templateVariables, BorderLayout.CENTER)
        val labels: MutableList<JLabel> = ArrayList()
        labels.add(JLabel("\${type} corresponds to the submission menu 'Type of chang'"))
        labels.add(JLabel("\${scope} corresponds to the submission menu 'Scope of this change'"))
        labels.add(JLabel("\${subject} corresponds to the submission menu 'Short description'"))
        labels.add(JLabel("\${body} corresponds to the submission menu 'Long description'"))
        labels.add(JLabel("\${changes} corresponds to the submission menu 'Breaking changes'"))
        labels.add(JLabel("\${closes} corresponds to the submission menu 'Closed issues'"))
        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.insets = JBUI.insets(2, 10, 0, 0)
        IntStream.range(0, labels.size).forEach { i: Int ->
            constraints.weightx = 1.0
            constraints.gridx = 0
            constraints.gridy = i
            templateVariables.add(labels[i], constraints)
        }
        val downPanel = JPanel(BorderLayout())
        downPanel.add(JLabel("Template text:"), BorderLayout.NORTH)
        downPanel.add(templateEditor.component, BorderLayout.CENTER)
        templatePanel = JBSplitter(true, "", 0.3f)
        templatePanel.firstComponent = upPanel
        templatePanel.secondComponent = downPanel
        typeTab = TabInfo(typePanel)
        typeTab.text = "type"
        tabbedPane.addTab(typeTab)
        templateTab = TabInfo(templatePanel)
        templateTab.text = "template"
        tabbedPane.addTab(templateTab)
    }

    fun reset() {
        val state = GitCommitHelperState.getInstance();
        templateEditor.document.setText(state.template)
        typeRows = state.typeRows
        typeTable.reset(state)
        ApplicationManager.getApplication().runWriteAction {
            templateEditor.document.setText(state.template)
        }
    }

    companion object {
        private val GIT_COMMIT_TEMPLATE = TemplateImpl("commit-template", "Git")
    }

    override fun dispose() {
    }
}
