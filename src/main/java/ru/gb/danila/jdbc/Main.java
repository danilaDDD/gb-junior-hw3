package ru.gb.danila.jdbc;

import org.h2.jdbc.JdbcSQLNonTransientConnectionException;

import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");) {
           createTables(connection);
           addDepartmentColumn(connection);
           fillData(connection);
            
           Optional.ofNullable(getDepartmentNameByPersonId(connection, 1L))
                   .ifPresentOrElse(System.out::println,
                   () -> System.out.println("department not found"));

            System.out.println(getDepartmentNameToPersonNameMap(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<String>> getDepartmentNameToPersonNameMap(Connection connection) {
        try(PreparedStatement prepared = connection.prepareStatement("""
        SELECT d.name, p.name FROM department as d 
        INNER JOIN person as p ON p.department_id = d.id;
""")) {
        ResultSet resultSet = prepared.executeQuery();
        Map<String, List<String>> resultMap = new HashMap<>();

        while(resultSet.next()){
            String dName, pName;
            dName = resultSet.getString(1);
            pName = resultSet.getString(2);
            resultMap.computeIfAbsent(dName, k -> new ArrayList<>()).add(pName);
        }

        return resultMap;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of();
    }

    private static String getDepartmentNameByPersonId(Connection connection, long personId) {
        try (PreparedStatement prepared = connection.prepareStatement("""
        SELECT d.name FROM department as d
        INNER JOIN person as p ON p.department_id=d.id
        WHERE p.id=?
""")) {
            prepared.setLong(1, personId);
            ResultSet resultSet = prepared.executeQuery();
            if(resultSet.next())
                resultSet.getString(1);
            else
                return null;

        } catch (JdbcSQLNonTransientConnectionException e) {
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void fillData(Connection connection) throws SQLException {
        Department department1, department2;
        department1 = new Department(0L, "department1");
        department2 = new Department(1L, "department2");

        saveDepartment(connection, department1);
        saveDepartment(connection, department2);

        List<Person> personList = new ArrayList<>(List.of(
                new Person(0L, "Bob", 18, department1),
                new Person(1L, "Ivan", 20, department2),
                new Person(2L, "Dima", 30, department1),
                new Person(3L, "Peter", 42, department2)
        ));
        for(Person p: personList)
            savePerson(connection, p);

        System.out.println("fill data");
    }

    private static void savePerson(Connection connection, Person person) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement("""
            INSERT INTO person(id, name, age, active, department_id) VALUES (?, ?, ?, ?, ?)
""")){
            statement.setLong(1, person.getId());
            statement.setString(2, person.getName());
            statement.setInt(3, person.getAge());
            statement.setBoolean(4, person.isActive());
            statement.setLong(5, person.getDepartment().getId());

            statement.execute();
            System.out.println("fill person");
        }
    }

    private static void saveDepartment(Connection connection, Department department) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("""
        INSERT INTO department(id, name) VALUES (?, ?)
""")){
            preparedStatement.setLong(1, department.getId());
            preparedStatement.setString(2, department.getName());
            preparedStatement.execute();
        }
    }

    private static void addDepartmentColumn(Connection connection) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("""
        ALTER TABLE person ADD COLUMN department_id BIGINT NULL;
            ALTER TABLE person ADD CONSTRAINT department_fk
                FOREIGN KEY (department_id) REFERENCES department(id);
        """)){
            if(preparedStatement.execute())
                System.out.println("add column department");
        }catch (SQLException e){
            throw e;
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("""
            CREATE TABLE IF NOT EXISTS person(
                id bigint PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                age INTEGER NULL,
                active BOOLEAN
            );
            
            CREATE TABLE IF NOT EXISTS department(
                id BIGINT PRIMARY KEY,
                name VARCHAR(128) NOT NULL 
            );
""")){
        if(preparedStatement.execute()){
            System.out.println("created table");
        }else {
            System.out.println("table has not created");
        }
        }
    }
}

