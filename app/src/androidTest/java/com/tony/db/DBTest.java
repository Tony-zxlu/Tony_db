package com.tony.db;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tony.db.bean.Employee;
import com.tony.db.bean.Skill;

/**
 * Created by tony on 16/4/5.
 */
public class DBTest extends AndroidTestCase {

    private Employee employee;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DBManager.getInstance(getContext());

        employee = new Employee();
        employee.setId("10000");
        employee.setAge(25);
        employee.setSalary(100l);
        employee.setManager(false);
        employee.setScore((short) 90);
        employee.setName("小明");
        final Skill skill = new Skill();
        skill.setName("java");
        skill.setLevel(0);
        employee.setSkill(skill);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        DBManager.getInstance(getContext()).release();
    }

    @SmallTest
    public void testAdd() throws Exception {
        BaseDao<Employee> employeeBaseDao = DBManager.getInstance(getContext()).getDao(Employee.class);
        employeeBaseDao.newOrUpdate(employee);
    }
}
