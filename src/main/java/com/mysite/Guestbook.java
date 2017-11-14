package com.mysite;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Guestbook implements HttpHandler {

    private GuestbookDatabase guestbookDatabase = new GuestbookDatabase();

    Guestbook() throws SQLException {
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

        // Send a form if it wasn't submitted yet.
        if(method.equals("GET")) {
            try {
                ArrayList<Message> messages = guestbookDatabase.getAllMessages();
                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/guestbook.twig");
                JtwigModel model = JtwigModel.newModel().with("messages", messages);
                response = template.render(model);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } // If the form was submitted, retrieve it's content.
        else if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            System.out.println(formData);
            Message message = parseFormData(formData);
            try {
                guestbookDatabase.addNewMessage(message);
                ArrayList<Message> messages = guestbookDatabase.getAllMessages();
                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/guestbook.twig");
                JtwigModel model = JtwigModel.newModel().with("messages", messages);
                response = template.render(model);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static Message parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");

            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return new Message(map.get("yourname"), map.get("message"), new Date());
    }
}
