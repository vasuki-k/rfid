/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rfidjava;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;

public class SendToREST {

    public SendToREST(JSONObject json) throws UnsupportedEncodingException, MalformedURLException, IOException, SQLException, JSONException {

        String url = "http://localhost:3091/api";
        URL object = new URL(url);
        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");
        DataOutputStream wr=new DataOutputStream(con.getOutputStream());
        wr.writeBytes(json.toString());
        wr.flush();

        StringBuilder sb = new StringBuilder();
        int HttpResult = con.getResponseCode();
        System.out.println(HttpResult);

    }

}
