package com.example.ebeat.Database;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connection;

    private final String host = "192.168.10.43";
//    private final String host = "10.0.2.2";

    private final String database = "ebeat";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "root";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status = false;
    private String [] details = new String[2], details_supervisor = new String[1], details_admin = new String[1];
    List<ReportList> reportList;
    private int count;
    private String[] places;
    List<OfficerList> officerList;
    OfficerList profile;
    List<LatLng> position_list;

    public String[] login(String id, String password, String rank)
    {
        reportList = new ArrayList<>();
        status = false;
        this.url = String.format(this.url, this.host, this.port, this.database);
        login_connect(id, password, rank);
//        this.disconnect();
        System.out.println("connection status:" + status);
        switch (rank)
        {
            case "Admin":
                return details_admin;
            case "Supervisor":
                return details_supervisor;
            case "Beat Officer":
                return details;
            default:
                return details;
        }
    }

    private void login_connect(String id, String password, String rank)
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

                    switch(rank)
                    {
                        case "Admin":
                            String sql_admin = "SELECT * FROM admin WHERE admin_id=? and admin_pass=?";
                            PreparedStatement statement_admin = connection.prepareStatement(sql_admin);

                            statement_admin.setString(1, id);
                            statement_admin.setString(2, password);

                            ResultSet result_admin = statement_admin.executeQuery();
                            if(result_admin!=null)
                            {
                                status = true;
                                while(result_admin.next())
                                {
                                    details_admin[0] = result_admin.getString("admin_name");
                                }
                            }
                            else
                                status = false;
                            break;
                        case "Supervisor":
                            String sql_supervisor = "SELECT * FROM supervisors WHERE supervisor_id=? and pass=?";
                            PreparedStatement statement_supervisor = connection.prepareStatement(sql_supervisor);

                            statement_supervisor.setString(1, id);
                            statement_supervisor.setString(2, password);

                            ResultSet result_supervisor = statement_supervisor.executeQuery();
                            if(result_supervisor!=null)
                            {
                                status = true;
                                while(result_supervisor.next())
                                {
                                    details_supervisor[0] = result_supervisor.getString("supervisor_name");
                                }
                            }
                            else
                                status = false;
                            break;
                        case "Beat Officer":
                            String sql = "SELECT * FROM beat_assigned WHERE officer_id=? and password=?";
                            PreparedStatement statement = connection.prepareStatement(sql);

                            statement.setString(1, id);
                            statement.setString(2, password);

                            ResultSet result = statement.executeQuery();

                            if(result!=null)
                            {
                                status = true;
                                while(result.next())
                                {
                                    details[0] = result.getString("name");
                                    details[1] = result.getString("beat_id");

                                }
                            }
                            else
                                status = false;
                            break;
                    }
                    connection.close();
                }
                catch (Exception e)
                {
                    Log.d("Database", "Error "+e.getMessage());
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

    public boolean add_report(String id, String pname, String type, byte[] imageBytes, Double latitude, Double longitude, String remarks)
    {
        status = false;

        this.url = String.format(this.url, this.host, this.port, this.database);
        add_connect(id, pname, type, imageBytes, latitude, longitude, remarks);

        return status;
    }
    void add_connect(String id, String pname, String type, byte[] imageBytes, Double latitude, Double longitude, String remarks)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully!!!");

                    String sql = "INSERT INTO report(officer_id, place_name, place_type, image, remarks, latitude, longitude)values(?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setString(1, id);
                    statement.setString(2, pname);
                    statement.setString(3, type);
                    statement.setBytes(4, imageBytes);
                    statement.setString(5, remarks);
                    statement.setDouble(6, latitude);
                    statement.setDouble(7, longitude);

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
    void get_reports_connect(String beat_id, boolean filter)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully1"+beat_id);

                    String sql1;
                    if(filter)
                        sql1 = "SELECT officer_id, name, place_name, place_type, image, remarks, date(time_stamp) as report_date, time_stamp FROM beat_assigned NATURAL JOIN report WHERE beat_id=? and time_stamp>=current_date and time_stamp<current_date + interval '1 day' order by time_stamp desc";
                    else
                        sql1 = "SELECT officer_id, name, place_name, place_type, image, remarks, date(time_stamp) as report_date, time_stamp FROM beat_assigned NATURAL JOIN report WHERE beat_id=? order by time_stamp desc";

                    PreparedStatement statement1 = connection.prepareStatement(sql1);

                    statement1.setString(1, beat_id);

                    ResultSet result = statement1.executeQuery();
                    while(result.next())
                    {
                        Log.d("report", "run: "+result.getString("name")+result.getString("place_name")+result.getTimestamp("time_stamp"));
                        reportList.add(new ReportList(result.getString("name"), result.getString("officer_id"), result.getDate("report_date"),result.getTimestamp("time_stamp"),result.getString("place_name"), result.getString("place_type"), result.getBytes("image"), result.getString("remarks")));
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

    public OfficerList get_profile(String id)
    {
        count = 0;

        this.url = String.format(this.url, this.host, this.port, this.database);
        count_connect(id);

        return profile;
    }
    void count_connect(String id)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully"+id);

                    String sql = "SELECT * FROM report WHERE officer_id=? and time_stamp>=current_date and time_stamp<current_date + interval '1 day'";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setString(1, id);

                    ResultSet result = statement.executeQuery();
                    while(result.next())
                        count++;

                    Log.d("Database", "run: "+count);

                    String sql1 = "SELECT officer_id, name, rank, beat_id, supervisor_name, phone_number FROM beat_assigned NATURAL JOIN supervisors WHERE officer_id=?";
                    PreparedStatement statement1 = connection.prepareStatement(sql1);
                    statement1.setString(1, id);
                    ResultSet resultSet = statement1.executeQuery();

                    while(resultSet.next())
                    {
                        Log.d("Database", "run: "+resultSet.getString("officer_id"));
                        profile = new OfficerList(resultSet.getString("officer_id"), resultSet.getString("name"), resultSet.getString("rank"), resultSet.getString("beat_id"), resultSet.getString("supervisor_name"), resultSet.getLong("phone_number"), count);
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

    public String[] get_places(String type, String beat_id)
    {
        status = false;
        this.url = String.format(this.url, this.host, this.port, this.database);
        get_places_connect(type, beat_id);
//        this.disconnect();
        System.out.println("connection status:" + status);
        return places;
    }

    private void get_places_connect(String type, String beat_id)
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

                    String sql = "SELECT * FROM main WHERE type=? and beat=?";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setString(1, type);
                    statement.setString(2, beat_id);
                    int size = 0;
                    List<String> temp = new ArrayList<>();

                    ResultSet result = statement.executeQuery();
                    while(result.next())
                    {
                        size++;
                        temp.add(result.getString("place"));
                    }
                    places = new String[size];
                    for(int i=0;i<size;i++)
                    {
                        places[i] = temp.get(i);
                    }

                    connection.close();
                }
                catch (Exception e)
                {
                    Log.d("Database", "Error "+e.getMessage());
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

    public List<ReportList> get_subordinate_reports(String supervisor_id, boolean filter)
    {
        reportList = new ArrayList<>();

        status = false;

        this.url = String.format(this.url, this.host, this.port, this.database);
        get_subordinate_reports_connect(supervisor_id, filter);

        return reportList;
    }
    void get_subordinate_reports_connect(String supervisor_id, boolean filter)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully1"+supervisor_id);

                    String sql1;
                    if(filter)
                        sql1 = "SELECT officer_id, name, place_name, place_type, image, remarks, date(time_stamp) as report_date, time_stamp FROM beat_assigned NATURAL JOIN report WHERE supervisor_id=? and time_stamp>=current_date and time_stamp<current_date + interval '1 day' order by time_stamp desc";
                    else
                        sql1 = "SELECT officer_id, name, place_name, place_type, image, remarks, date(time_stamp) as report_date, time_stamp FROM beat_assigned NATURAL JOIN report WHERE supervisor_id=? order by time_stamp desc";

                    PreparedStatement statement1 = connection.prepareStatement(sql1);

                    statement1.setString(1, supervisor_id);

                    ResultSet result = statement1.executeQuery();
                    while(result.next())
                    {
                        Log.d("report", "run: "+result.getString("name")+result.getString("place_name")+result.getTimestamp("time_stamp"));
                        reportList.add(new ReportList(result.getString("name"), result.getString("officer_id"), result.getDate("report_date"),result.getTimestamp("time_stamp"),result.getString("place_name"), result.getString("place_type"), result.getBytes("image"), result.getString("remarks")));
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

    public List<OfficerList> get_subordinates(String supervisor_id)
    {
        officerList = new ArrayList<>();

        status = false;

        this.url = String.format(this.url, this.host, this.port, this.database);
        get_subordinates_connect(supervisor_id);

        return officerList;
    }
    void get_subordinates_connect(String supervisor_id)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully1"+supervisor_id);

                    String sql1;
                    sql1 = "SELECT officer_id, name, rank, beat_id, phone_number, supervisor_name FROM beat_assigned NATURAL JOIN supervisors WHERE supervisor_id=?";

                    PreparedStatement statement1 = connection.prepareStatement(sql1);

                    statement1.setString(1, supervisor_id);

                    ResultSet result = statement1.executeQuery();
                    while(result.next())
                    {
                        Log.d("database", " "+result.getString("name"));
                        officerList.add(new OfficerList(result.getString("officer_id"), result.getString("name"), result.getString("rank"), result.getString("beat_id"), result.getString("supervisor_name"), result.getLong("phone_number")));
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
    public List<LatLng> get_location(String id)
    {
        position_list = new ArrayList<>();

        status = false;

        this.url = String.format(this.url, this.host, this.port, this.database);
        get_location_connect(id);

        return position_list;
    }
    void get_location_connect(String id)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Log.d("Database", "Connected Successfully1"+id);

                    String sql1;
                    sql1 = "SELECT * FROM report WHERE officer_id=? order by time_stamp desc";

                    PreparedStatement statement1 = connection.prepareStatement(sql1);

                    statement1.setString(1, id);

                    ResultSet result = statement1.executeQuery();
                    while(result.next())
                    {
                        Log.d("loc", "run: "+result.getDouble("latitude"));
                        position_list.add(new LatLng(result.getDouble("latitude"), result.getDouble("longitude")));
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
