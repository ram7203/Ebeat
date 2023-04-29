package com.example.ebeat.Database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connection;

    private final String host = "10.0.2.2";

    private final String database = "NewDB";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "root";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status = false;
    private String name = "";
    List<ReportList> reportList;
    private String beat="";

    public String login(String id, String password)
    {
        status = false;
        this.url = String.format(this.url, this.host, this.port, this.database);
        login_connect(id, password);
//        this.disconnect();
        System.out.println("connection status:" + status);
        return name;
    }

    private void login_connect(String id, String password)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully");

                    String sql = "SELECT * FROM officers WHERE officer_id=? and pass=?";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setString(1, id);
                    statement.setString(2, password);

                    ResultSet result = statement.executeQuery();
                    if(result!=null)
                    {
                        status = true;
                        while(result.next())
                            name = result.getString("officer_name");
                    }
                    else
                        status = false;

                    connection.close();
                }
                catch (Exception e)
                {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.status = false;
        }
    }

    public Connection getExtraConnection()
    {
        Connection c = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, pass);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return c;
    }

    public boolean add_report(String id, String pname, String type, byte[] imageBytes)
    {
        status = false;

        this.url = String.format(this.url, this.host, this.port, this.database);
        add_connect(id, pname, type, imageBytes);

        return status;
    }
    void add_connect(String id, String pname, String type, byte[] imageBytes)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully");

                    String sql = "INSERT INTO report(officer_id, place_name, place_type, image)values(?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setString(1, id);
                    statement.setString(2, pname);
                    statement.setString(3, type);
                    statement.setBytes(4, imageBytes);

                    int rows = statement.executeUpdate();
                    if(rows>0)
                    {
                        status = true;
                        Log.d("add", "run: Successfull");
                    }
                    else
                        status = false;

                    connection.close();
                }
                catch (Exception e)
                {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.status = false;
        }
    }

    public List<ReportList> get_reports(String id, boolean filter)
    {
        reportList = new ArrayList<>();

        status = false;

        this.url = String.format(this.url, this.host, this.port, this.database);
        get_reports_connect(id, filter);

        return reportList;
    }
    void get_reports_connect(String id, boolean filter)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully1");

                    String sql = "SELECT assigned_to_beat FROM officers WHERE officer_id=?";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setString(1, id);

                    ResultSet resultSet = statement.executeQuery();
                    if(resultSet!=null)
                    {
                        while(resultSet.next())
                            beat = resultSet.getString("assigned_to_beat");
                        Log.d("beat", "run: "+beat);

                        String sql1;
                        if(filter)
                            sql1 = "SELECT officer_id, officer_name, place_name, place_type, image, date(time_stamp) as report_date, time_stamp FROM officers NATURAL JOIN report WHERE assigned_to_beat=? and time_stamp>=current_date and time_stamp<current_date + interval '1 day'";
                        else
                            sql1 = "SELECT officer_id, officer_name, place_name, place_type, image, date(time_stamp) as report_date, time_stamp FROM officers NATURAL JOIN report WHERE assigned_to_beat=?";

                        PreparedStatement statement1 = connection.prepareStatement(sql1);

                        statement1.setString(1, beat);

                        ResultSet result = statement1.executeQuery();
                        while(result.next())
                        {
                            Log.d("report", "run: "+result.getString("officer_name")+result.getString("place_name")+result.getTimestamp("time_stamp"));
                            reportList.add(new ReportList(result.getString("officer_name"), result.getString("officer_id"), result.getDate("report_date"),result.getTimestamp("time_stamp"),result.getString("place_name"), result.getString("place_type"), result.getBytes("image")));
                        }
                    }


                    connection.close();
                }
                catch (Exception e)
                {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.status = false;
        }
    }
}
