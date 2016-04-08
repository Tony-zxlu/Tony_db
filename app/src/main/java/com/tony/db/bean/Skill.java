package com.tony.db.bean;

import com.tony.db.annotation.Column;
import com.tony.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by tony on 16/4/5.
 */
@Table(name = "skill")
public class Skill implements Serializable {

    @Column(name = "id", id = true)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private int level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }
}
