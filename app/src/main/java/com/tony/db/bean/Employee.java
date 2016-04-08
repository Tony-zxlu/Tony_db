package com.tony.db.bean;

import com.tony.db.annotation.Column;
import com.tony.db.annotation.Table;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tony on 16/4/5.
 */
@Table(name = "employee")
public class Employee implements Serializable {

    @Column(id = true, name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "salary")
    private long salary;

    @Column(name = "score")
    private short score;

    @Column(name = "manager")
    private boolean manager;

//    @Column(name = "skillId", type = Column.ColumnType.ONE2ONE)
//    private Skill skill;

    @Column(name = "skill", type = Column.ColumnType.ONE2MANY)
    private List<Skill> skills;




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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public short getScore() {
        return score;
    }

    public void setScore(short score) {
        this.score = score;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                ", score=" + score +
                ", manager=" + manager +
                ", skills=" + Arrays.toString(skills.toArray(new Skill[skills.size()])) +
                '}';
    }
}
