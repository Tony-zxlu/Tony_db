package com.tony.db;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tony.db.bean.Employee;
import com.tony.db.bean.Skill;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    Employee employee = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DBManager.getInstance(getApplication());


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

    @SmallTest
    public void test() throws Exception {
        int result = 1 + 2;
        assertEquals(3, result);
    }

    @SmallTest
    public void testDBAdd() throws Exception {
        BaseDao<Employee> employeeBaseDao = DBManager.getInstance(getApplication()).getDao(Employee.class);
        employeeBaseDao.newOrUpdate(employee);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        DBManager.getInstance(getApplication()).release();
    }
}