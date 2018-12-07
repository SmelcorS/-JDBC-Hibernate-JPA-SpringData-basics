package app;


import app.entities.User;
import app.interfaces.DbContext;
import app.orm.Connector;
import app.orm.EntityManager;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException {
        Connector connector = new Connector();
        connector.createConnection("root", "admin", "exer");
        DbContext<User> entityManager = new EntityManager(connector.getConnection());

        User user = new User("Ionko", "3123", 15, LocalDate.now());

        User user1 = new User("ceci", "1231", 18, LocalDate.now());

        entityManager.persist(user);
        entityManager.persist(user1);
        User found = entityManager.findFirst(User.class);
        System.out.println(found.toString());


    }
}
