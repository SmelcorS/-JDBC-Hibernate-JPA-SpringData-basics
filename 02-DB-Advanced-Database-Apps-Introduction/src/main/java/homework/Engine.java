package homework;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Engine implements Runnable {

    private Connection connection;

    public Engine(Connection connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            this.printAllMinionNames    ();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

/**
*    P02 Get Villains’ Names
*/

    private void getVillainNames() throws SQLException {
        String query =
                "SELECT v.name, count(mv.minion_id) AS count\n" +
                "FROM minions_db.villains v\n" +
                "JOIN minions_db.minions_villains mv\n" +
                "ON v.id = mv.villain_id\n" +
                "GROUP BY v.id\n" +
                "HAVING count > ?\n" +
                "ORDER BY count DESC;";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);

        preparedStatement.setInt(1, 5);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(String.format("%s %d",
                    resultSet.getString(1),
                    resultSet.getInt(2)));
        }

    }

/**
 *    P03 Get Minion Names
 */

    private void getMinionNames() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        int villainId = Integer.parseInt(scanner.nextLine());


        try {
            String getVillainNameQuery  =
                    "SELECT v.name \n" +
                            "FROM minions_db.villains v\n" +
                            "WHERE v.id = ?;";

            PreparedStatement preparedStatement;
            ResultSet resultSet;

            preparedStatement = this.connection.prepareStatement(getVillainNameQuery);
            preparedStatement.setInt(1, villainId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.println(String.format("Villain: %s", resultSet.getString(1)));


            String getMinionNamesQuery =
                    "SELECT m.name, m.age\n" +
                    "FROM minions_db.minions m\n" +
                    "JOIN minions_db.minions_villains mv\n" +
                    "ON m.id = mv.minion_id\n" +
                    "WHERE mv.villain_id = ?;";

            preparedStatement = this.connection.prepareStatement(getMinionNamesQuery);
            preparedStatement.setInt(1, villainId);
            resultSet = preparedStatement.executeQuery();

            int index = 0;
            while (resultSet.next()) {
                System.out.println(String.format("%d. %s %d", ++index, resultSet.getString(1), resultSet.getInt(2)));
            }

            if (index == 0) {
                System.out.println("<no minions>");
            }


        } catch (SQLException e) {
            System.out.println(String.format("No villain ID %s exists in the database", villainId));
        }

    }


/**
 *    P04 Add Minion
 */

    private void addMinion() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String[] minionArgs = Arrays.stream(scanner.nextLine().split("[\\s+:]")).skip(2).toArray(String[]::new);
        String[] villainArgs = Arrays.stream(scanner.nextLine().split("[\\s+:]")).skip(2).toArray(String[]::new);

        PreparedStatement preparedStatement;
        ResultSet resultSet;

        String minionTown = minionArgs[2];
        String townQuery = "SELECT t.name FROM minions_db.towns t WHERE t.name = '" + minionTown + "'";

        preparedStatement = this.connection.prepareStatement(townQuery);
        resultSet = preparedStatement.executeQuery();

        if (!checkIfExists(resultSet)) {
            String addTownQuery = "INSERT INTO minions_db.towns(name, country) VALUES ('" + minionTown + "', NULL);";
            executeUpdate(addTownQuery);
            System.out.println(String.format("Town %s was added to the database.", minionTown));
        }

        String villainName = villainArgs[0];
        String villainQuery = "SELECT v.name FROM minions_db.villains v WHERE v.name = '" + villainName + "'";

        preparedStatement = this.connection.prepareStatement(villainQuery);
        resultSet = preparedStatement.executeQuery();

        if (!checkIfExists(resultSet)) {
            String addVillainQuery = "INSERT INTO minions_db.villains(name, evilness_factor) VALUES ('" + villainName + "', 'evil');";
            executeUpdate(addVillainQuery);
            System.out.println(String.format("Villain %s was added to the database.", villainName));
        }

        String minionName = minionArgs[0];
        int minionAge = Integer.valueOf(minionArgs[1]);
        String minionNameQuery = "SELECT m.name FROM minions_db.minions m WHERE m.name = '" + minionName + "'";

        preparedStatement = this.connection.prepareStatement(minionNameQuery);
        resultSet = preparedStatement.executeQuery();

        if (!checkIfExists(resultSet)) {
            String getTownId = "SELECT t.id FROM minions_db.towns t WHERE t.name =  '" + minionTown +"';";
            preparedStatement = this.connection.prepareStatement(getTownId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int townId = resultSet.getInt(1);

            String addMinionQuery = "INSERT INTO minions_db.minions(name, age, town_id) VALUES ('" + minionName + "', " + minionAge + ", " + townId + ");";
            executeUpdate(addMinionQuery);

            String getMinionIdQuery = "SELECT m.id FROM minions_db.minions m WHERE m.name =  '" + minionName +"';";
            preparedStatement = this.connection.prepareStatement(getMinionIdQuery);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int minionId = resultSet.getInt(1);

            String getVillainIdQuery = "SELECT v.id FROM minions_db.villains v WHERE v.name =  '" + villainName +"';";
            preparedStatement = this.connection.prepareStatement(getVillainIdQuery);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int villainId = resultSet.getInt(1);

            String addMinionVillainRelation = "INSERT INTO minions_db.minions_villains(minion_id, villain_id) VALUES (" + minionId +", " + villainId + ");";
            executeUpdate(addMinionVillainRelation);

            System.out.println(String.format("Successfully added %s to be minion of %s", minionName, villainName));


        } else {
            String getMinionIdQuery = "SELECT m.id FROM minions_db.minions m WHERE m.name =  '" + minionName +"';";
            preparedStatement = this.connection.prepareStatement(getMinionIdQuery);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int minionId = resultSet.getInt(1);

            String getVillainIdQuery = "SELECT v.id FROM minions_db.villains v WHERE v.name =  '" + villainName +"';";
            preparedStatement = this.connection.prepareStatement(getVillainIdQuery);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int villainId = resultSet.getInt(1);

            String addMinionVillainRelation = "INSERT INTO minions_db.minions_villains(minion_id, villain_id) VALUES (" + minionId +", " + villainId + ");";
            executeUpdate(addMinionVillainRelation);

            System.out.println(String.format("Successfully added %s to be minion of %s", minionName, villainName));
        }

    }

    private void executeUpdate(String addVillainQuery) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(addVillainQuery);
        preparedStatement.executeUpdate();
    }


    private boolean checkIfExists(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return true;
        }
        return false;
    }


/**
 *    P05 Change Town Names Casing
 */

    private void changeTownNameCasing() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        String countryName = scanner.nextLine();

        String townUpdate = "UPDATE minions_db.towns t\n" +
                "SET t.name = UPPER(t.name)\n" +
                "WHERE t.country = '" + countryName + "';";

        PreparedStatement preparedStatement;
        preparedStatement= this.connection.prepareStatement(townUpdate);
        int result =  preparedStatement.executeUpdate();

        if (result == 0) {
            System.out.println("No town names were affected.");
        } else {
            String townSelect = "SELECT t.name " +
                    "FROM minions_db.towns t\n" +
                    "WHERE t.country = '" + countryName + "';";

            preparedStatement = this.connection.prepareStatement(townSelect);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> towns = new ArrayList<>();
            while (resultSet.next()) {
                towns.add(resultSet.getString(1));
            }

            System.out.println("[" + String.join(", ", towns) + "]");

        }
    }


/**
 *    P06 *Remove Villain
 */

    private void removeVillain() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int villainId = Integer.valueOf(scanner.nextLine());

        String selectVillainName =
                "SELECT v.name FROM minions_db.villains v\n" +
               "WHERE v.id = ?;";

        PreparedStatement preparedStatement;
        ResultSet resultSet;

        preparedStatement = this.connection.prepareStatement(selectVillainName);
        preparedStatement.setInt(1, villainId);
        resultSet = preparedStatement.executeQuery();

        if (!checkIfExists(resultSet)) {
            System.out.println("No such villain was found");
            return;
        }
        resultSet.beforeFirst();
        resultSet.next();
        String name = resultSet.getString(1);



        String releaseMinions =
                "DELETE FROM minions_db.minions_villains \n" +
                        "WHERE villain_id = ?;";

        preparedStatement = this.connection.prepareStatement(releaseMinions);
        preparedStatement.setInt(1, villainId);
        int releasedMinionsCount = preparedStatement.executeUpdate();

        String deleteVillain =
                "DELETE FROM minions_db.villains WHERE id = ?;";

        preparedStatement = this.connection.prepareStatement(deleteVillain);
        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();

        System.out.println(String.format("%s was deleted", name));
        System.out.println(String.format("%d minions released", releasedMinionsCount));



    }

/**
 *    P07 Print All Minion Names
 */

    private void printAllMinionNames() throws SQLException {


        String minionNamesQuery =
                "SELECT m.name FROM minions_db.minions m;";

        PreparedStatement preparedStatement;
        ResultSet resultSet;

        preparedStatement = this.connection.prepareStatement(minionNamesQuery);
        resultSet = preparedStatement.executeQuery();

        List<String> minionNames = new ArrayList<>();
        while (resultSet.next()) {
            minionNames.add(resultSet.getString(1));

        }

        for (int i = 0; i < minionNames.size() / 2; i++) {
            System.out.println(minionNames.get(i));
            System.out.println(minionNames.get(minionNames.size() - i - 1));
        }

    }

/**
 *    P08 Increase Minions Age
 */
    private void increaseMinionAge() throws SQLException {


    }

}
