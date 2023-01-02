package com.fobgochod.git.commit

import com.fobgochod.git.commit.domain.TemplateVarType
import com.fobgochod.git.commit.settings.GitCommitHelperState
import com.intellij.codeInsight.template.impl.TemplateImpl

class GitCommitMessage {

    companion object {
        private val TEMPLATE = TemplateImpl("commit-template", "Git")

        fun parse(message: String): GitCommitMessage {
            val commitMessage = GitCommitMessage()

            return commitMessage
        }

        fun format(): String {
            TEMPLATE.string = GitCommitHelperState.getInstance().template
            TEMPLATE.addVariable(TemplateVarType.TYPE.title, "", "fix", true);
            TEMPLATE.addVariable(TemplateVarType.SCOPE.title, "", "compile", true);
            TEMPLATE.addVariable(TemplateVarType.SUBJECT.title, "", "hello world", true);

            return TEMPLATE.templateText;
        }
    }
}
