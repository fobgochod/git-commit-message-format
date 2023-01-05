package com.fobgochod.git.commit.domain

enum class TemplateVariable(val title: String, val description: String) {

    TYPE("type", "\${type} corresponds to the submission menu 'Type of chang'"),
    SCOPE("scope", "\${scope} corresponds to the submission menu 'Scope of this change'"),
    SUBJECT("subject", "\${subject} corresponds to the submission menu 'Short description'"),
    BODY("body", "\${body} corresponds to the submission menu 'Long description'"),
    CHANGE("change", "\${change} corresponds to the submission menu 'Breaking changes'"),
    CLOSED("closed", "\${closed} corresponds to the submission menu 'Closed issues'");

    override fun toString(): String {
        return this.title + " - " + this.description;
    }
}
