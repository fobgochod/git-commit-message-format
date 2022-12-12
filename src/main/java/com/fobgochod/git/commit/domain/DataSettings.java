package com.fobgochod.git.commit.domain;

import java.util.List;

/**
 *  DataSettings.java
 *
 * @author fobgochod
 * @date 2022/12/13 2:14
 */
public class DataSettings {

    private String template;
    private List<TypeItem> typeItems;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<TypeItem> getTypeAliases() {
        return typeItems;
    }

    public void setTypeAliases(List<TypeItem> typeItems) {
        this.typeItems = typeItems;
    }


}
