package com.mysite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private String username;
    private String content;
    private Date messageDate;

    public Message(String username, String content, Date date) {
        setUsername(username);
        setContent(content);
        setMessageDate(date);
    }

    @Override
    public String toString() {
        String toReturn = String.format("<b>%s</b><br>Name: %s, %s<br><br>", this.content, this.username, getMessageDateAsString());

        return toReturn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageDateAsString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(this.messageDate);
    }
}
