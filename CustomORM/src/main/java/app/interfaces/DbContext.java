package app.interfaces;


import java.sql.SQLException;
import java.util.Iterator;

public interface DbContext<E>  {
    boolean persist(E entity) throws IllegalAccessException, SQLException;
    Iterator<E> find(Class<E> table);
    Iterable<E> find(Class<E> table, String where);
    E findFirst(Class<E> table) throws IllegalAccessException, SQLException, InstantiationException;
    E findFirst(Class<E> table, String where) throws SQLException, IllegalAccessException, InstantiationException;
}
