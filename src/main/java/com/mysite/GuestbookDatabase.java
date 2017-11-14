package com.mysite;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GuestbookDatabase {

    private final Connection connection;
    private PreparedStatement stmt = null;

    public GuestbookDatabase() throws SQLException {
        this.connection = GuestbookDatabaseConnection.getConnection();
    }

    public void addNewMessage(Message message) throws SQLException {

        String sqlQuery = "INSERT INTO guestbook (username, message, message_date) VALUES (?, ?, ?);";

        stmt = connection.prepareStatement(sqlQuery);
        stmt.setString(1, message.getUsername());
        stmt.setString(2, message.getContent());
        stmt.setString(3, message.getMessageDateAsString());

        stmt.executeUpdate();
    }

    public ArrayList<Message> getAllMessages() throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();

        String sqlQuery = "SELECT * FROM guestbook;";

        stmt = connection.prepareStatement(sqlQuery);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            try {
                String name = rs.getString("username");
                String message = rs.getString("message");
                Date date = createDateObjectFromString(rs.getString("message_date"));
                messages.add(new Message(name, message, date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return messages;
    }

    private Date createDateObjectFromString(String messageDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.parse(messageDate);
    }
}