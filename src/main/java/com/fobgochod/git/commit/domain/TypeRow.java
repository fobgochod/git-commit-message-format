package com.fobgochod.git.commit.domain;

import java.util.Objects;

/**
 * 类型
 *
 * @author fobgochod
 * @date 2022/12/11 23:16
 */
public class TypeRow {

    public String title;
    public String description;

    public TypeRow() {
    }

    public TypeRow(String title, String description) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeRow typeRow = (TypeRow) o;
        return Objects.equals(title, typeRow.title) && Objects.equals(description, typeRow.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getTitle(), this.getDescription());
    }
}
