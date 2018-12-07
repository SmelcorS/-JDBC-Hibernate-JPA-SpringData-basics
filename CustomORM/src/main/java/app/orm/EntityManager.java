package app.orm;


import app.annotations.Column;
import app.annotations.Entity;
import app.annotations.Id;
import app.interfaces.DbContext;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EntityManager<E> implements DbContext<E> {
    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field primary = this.getId(entity.getClass());
        primary.setAccessible(true);
        Object value = primary.get(entity);
        if (value == null || (int)value <= 0) {
            this.doInsert(entity, primary);
        } else {
            this.doUpdate(entity, primary);
        }
        return true;
    }

    private void doUpdate(E entity, Field primary) throws IllegalAccessException, SQLException {
        String tableName = this.getTableName(entity);
        StringBuilder query = new StringBuilder("UPDATE  " + tableName + " SET ");
        StringBuilder whereClause = new StringBuilder(" WHERE ");

        Field[] fields = entity.getClass().getDeclaredFields();


        for (int i = 0; i < fields.length; i++) {
            Field current = fields[i];
            current.setAccessible(true);

            if (current.isAnnotationPresent(Id.class)) {
                whereClause.append(String.format("%s = %d;", current.getName(), (int)current.get(entity)));
                continue;
            }

            String columnName = getColumnName(current);
            Object columnValue = current.get(entity);



            if (current.getType() == Integer.class || current.getType() == int.class) {
                query.append(String.format("%s = %d", columnName , (int) columnValue)).append(", ");
            } else if (current.getType() == String.class){
                query.append(String.format("%s = '%s'",columnName, (String) columnValue)).append(", ");
            } else {
                query.append(String.format("%s = '%s'", columnName, (LocalDate) columnValue)).append(", ");
            }

        }
        query.setLength(query.length() - 2);
        query.append(whereClause);

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            statement.execute();
        }


    }

    private void doInsert(E entity, Field primary) throws SQLException, IllegalAccessException {
        String tableName = this.getTableName(entity);
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + "(");


        List<String> entityColumnNames = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field
                .isAnnotationPresent(Column.class))
                .map(field -> field.getAnnotation(Column.class).name())
                .collect(Collectors.toList());

        query.append(String.join(", ", entityColumnNames)).append(") VALUES (");

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) continue;
            field.setAccessible(true);
            Object entityFieldValue = field.get(entity);

            if (field.getType() == Integer.class || field.getType() == int.class) {
                query.append(String.format("%d", (int) entityFieldValue)).append(", ");
            } else if (field.getType() == String.class){
                query.append(String.format("'%s'", (String) entityFieldValue)).append(", ");
            } else {
                query.append(String.format("'%s'", (LocalDate) entityFieldValue)).append(", ");
            }
        }

        query.setLength(query.length() - 2);

        query .append(");");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            statement.execute();
        }

        updateEntityPrimaryKey(entity, primary, tableName);

    }

    private void updateEntityPrimaryKey(E entity, Field primaryKey, String tableName) throws IllegalAccessException, SQLException {
        Field field = entity.getClass().getDeclaredFields()[1];
        field.setAccessible(true);
        String columnName = getColumnName(field);
        Object columnValue = field.get(entity);

        String selectIdQuery = "SELECT id FROM " + tableName + " WHERE " + columnName + " = '" + columnValue + "';";

        try (PreparedStatement statement = connection.prepareStatement(selectIdQuery)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt("id");

            primaryKey.set(entity, id);
        }
    }




    public Iterator<E> find(Class<E> table) {
        return null;
    }

    public Iterable<E> find(Class<E> table, String where) {
        return null;
    }

    public E findFirst(Class<E> table) throws IllegalAccessException, SQLException, InstantiationException {
        return findFirst(table, null);
    }

    public E findFirst(Class<E> table, String where) throws SQLException, IllegalAccessException, InstantiationException {


        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + table.getAnnotation(Entity.class).name() +
                " WHERE 1 " + (where != null ? " AND " + where : "") + " LIMIT 1";

        ResultSet resultSet = statement.executeQuery(query);
        E entity = table.newInstance();
        resultSet.next();
        this.fillEntity(table, resultSet, entity);
        return entity;
    }

    private void fillEntity(Class<E> table, ResultSet resultSet, E entity) throws SQLException, IllegalAccessException {
        Field pkField = getId(entity.getClass());
        pkField.setAccessible(true);
        String pkName = pkField.getAnnotation(Id.class).name();
        pkField.set(entity, resultSet.getInt(pkName));


        Field[] fields = table.getDeclaredFields();
        Arrays.stream(fields).filter(f -> f.isAnnotationPresent(Column.class)).forEach( x -> {
            x.setAccessible(true);
            try {
                this.fillField(x, entity, resultSet);
            } catch (SQLException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    private void fillField(Field current, E entity, ResultSet resultSet) throws SQLException, IllegalAccessException {
        current.setAccessible(true);
        String columnName = current.getAnnotation(Column.class)
                .name();

        if (current.getType() == int.class || current.getType() == Integer.class) {
            int value = resultSet.getInt(columnName);
            current.set(entity, value);
        } else if (current.getType() == String.class) {
            String value = resultSet.getString(columnName);
            current.set(entity, value);
        } else {
            String value = resultSet.getString(columnName);
            LocalDate date = LocalDate.parse(value);
            current.set(entity, date);
        }
    }


    private Field getId(Class entity) {
        return Arrays.stream(entity.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity does not have primary key"));
    }

    private String getTableName(E entity) {
        return entity.getClass().getAnnotation(Entity.class).name();
    }

    private String getColumnName(Field field) {
        return field.getAnnotation(Column.class).name();
    }
}
