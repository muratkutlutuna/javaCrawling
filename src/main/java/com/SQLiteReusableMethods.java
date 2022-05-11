package com;

import java.sql.*;


public class SQLiteReusableMethods {

    private static Connection con;
    private static boolean hasData;

    public void displayInitialisedCinemas() throws SQLException, ClassNotFoundException {
        if (con == null) {
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("Select * FROM cinema_table");
        System.out.println("text = " + res.getString("text") + ", image = " + res.getString("image"));
    }

    private void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:TheMovieDB.db";
        con = DriverManager.getConnection(url);
        initialise();
    }

    private void initialise() throws SQLException {
        if (!hasData) {
            hasData = true;
            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='cinema_table'");
            if (!res.next()) {
                System.out.println("Building the \"cinema_table\" table with prepopulated values.");
                //need to build the table
                Statement state2 = con.createStatement();
                state2.execute("CREATE TABLE cinema_table(id integer(60)," +
                        "text varchar(60)," + "image LONGBLOB," +
                        "primary key(id));");

                //inserting some sample data
                PreparedStatement prep = con.prepareStatement("INSERT INTO cinema_table values (?,?,?);");
                prep.setString(2, "text_test_1");
                prep.setString(3, "image_test_1");
                prep.execute();

                PreparedStatement prep2 = con.prepareStatement("INSERT INTO cinema_table values (?,?,?);");
                prep2.setString(2, "text_test_2");
                prep2.setString(3, "image_test_2");
                prep2.execute();

            }
        }
    }

    public void addCinema(String text, String image) {
        try {
            if (con == null) {
                getConnection();
            }
            PreparedStatement prep = con.prepareStatement("INSERT INTO cinema_table values (?,?,?);");
            prep.setString(2, text);
            prep.setString(3, image);
            prep.execute();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
