package com.fobgochod.git.commit.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * TypeItem
 *
 * @author fobgochod
 * @date 2022/12/11 23:16
 */
public class TypeItem {

    public String title;
    public String description;

    public TypeItem() {
    }

    public TypeItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getTitle(), this.getDescription());
    }
}
