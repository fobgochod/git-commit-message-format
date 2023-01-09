package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.ui.EditorTextField
import com.intellij.util.LocalTimeCounter
import com.intellij.util.ui.JBUI
import javax.swing.border.Border

class TemplateEditor(text: String, project: Project, fileType: FileType) : EditorTextField(text, project, fileType) {

    init {
        isViewer = true
        isOneLineMode = false
    }

    override fun setFileType(fileType: FileType) {
        setNewDocumentAndFileType(fileType, createDocument(text))
    }

    override fun createDocument(): Document? {
        return createDocument("")
    }

    override fun createEditor(): EditorEx {
        val editor = super.createEditor()
        initOneLineMode(editor)
        setupTextFieldEditor(editor)
        return editor
    }

    override fun repaint(tm: Long, x: Int, y: Int, width: Int, height: Int) {
        super.repaint(tm, x, y, width, height)
        if (editor is EditorEx) {
            initOneLineMode((editor as EditorEx?)!!)
        }
    }

    override fun setBorder(border: Border?) {
        super.setBorder(JBUI.Borders.empty())
    }

    private fun createDocument(text: String): Document? {
        val factory = PsiFileFactory.getInstance(project)
        val stamp = LocalTimeCounter.currentTime()
        val psiFile = factory.createFileFromText(
            GitBundle.message("plugin.name"),
            fileType, text, stamp, true, false
        )
        return PsiDocumentManager.getInstance(project).getDocument(psiFile)
    }

    companion object {

        private fun initOneLineMode(editor: EditorEx) {
            editor.isOneLineMode = false
            editor.colorsScheme = editor.createBoundColorSchemeDelegate(null)
            editor.settings.isCaretRowShown = false
        }

        private fun setupTextFieldEditor(editor: EditorEx) {
            val settings = editor.settings
            settings.isFoldingOutlineShown = false
            settings.isLineNumbersShown = true
            settings.isIndentGuidesShown = false
            editor.setHorizontalScrollbarVisible(true)
            editor.setVerticalScrollbarVisible(true)
        }
    }
}
