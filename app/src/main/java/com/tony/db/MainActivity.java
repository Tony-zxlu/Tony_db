package com.tony.db;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tony.db.bean.Employee;
import com.tony.db.bean.Skill;
import com.tony.db.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final Employee employee = new Employee();
        employee.setId("10000");
        employee.setAge(25);
        employee.setSalary(100l);
        employee.setManager(false);
        employee.setScore((short) 90);
        employee.setName("小明");

        List<Skill> skills = new ArrayList<>();

        final Skill skill = new Skill();
        skill.setId("php01");
        skill.setName("php");
        skill.setLevel(2);
        skills.add(skill);

        final Skill skill1 = new Skill();
        skill1.setId("java01");
        skill1.setName("php");
        skill1.setLevel(2);
        skills.add(skill1);

        employee.setSkills(skills);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDao<Employee> employeeBaseDao = DBManager.getInstance(MainActivity.this).getDao(Employee.class);
                employee.setName("小明");
                skill.setLevel(0);
                employeeBaseDao.newOrUpdate(employee);
            }
        });

        findViewById(R.id.add1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Employee employee = new Employee();
                employee.setId("10001");
                employee.setAge(25);
                employee.setSalary(100l);
                employee.setManager(false);
                employee.setScore((short) 90);
                employee.setName("小刚");

                List<Skill> skills = new ArrayList<>();

                final Skill skill = new Skill();
                skill.setId("php01");
                skill.setName("php");
                skill.setLevel(2);
                skills.add(skill);

                final Skill skill1 = new Skill();
                skill1.setId("java01");
                skill1.setName("php");
                skill1.setLevel(2);
                skills.add(skill1);

                employee.setSkills(skills);


                BaseDao<Employee> employeeBaseDao = DBManager.getInstance(MainActivity.this).getDao(Employee.class);
                employeeBaseDao.newOrUpdate(employee);
            }
        });

        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDao<Employee> employeeBaseDao = DBManager.getInstance(MainActivity.this).getDao(Employee.class);
//                employee.setName("小红");
//                employeeBaseDao.newOrUpdate(employee);
                Employee employee1 = employeeBaseDao.queryById("10000");
                LogUtils.db(" query result : " + ((employee1 == null) ? " NULL " : employee1.toString()));
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employee.setName("小红");
                skill.setLevel(5);
                BaseDao<Employee> employeeBaseDao = DBManager.getInstance(MainActivity.this).getDao(Employee.class);
                employeeBaseDao.newOrUpdate(employee);
            }
        });

        findViewById(R.id.queryAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDao<Employee> employeeBaseDao = DBManager.getInstance(MainActivity.this).getDao(Employee.class);
                List<Employee> employees = employeeBaseDao.queryAll();
                for (Employee employee : employees) {
                    LogUtils.db(((employee == null) ? " NULL " : employee.toString()));
                }
                LogUtils.db("---------query all---------");
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDao<Employee> employeeBaseDao = DBManager.getInstance(MainActivity.this).getDao(Employee.class);
                employeeBaseDao.delete("10000");
            }
        });

       /* findViewById(R.id.destory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBManager.getInstance(MainActivity.this).release();
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
