package com.revature.music;

import com.fasterxml.jackson.databind.ObjectMapper; 
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//import static sun.security.pkcs11.wrapper.Functions.addMapping;

class Artist {
    private int artistId;
    private String name;
    public Artist(int artistId, String name) {
        this.artistId = artistId;
        this.name = name;
    }
    public Artist() {
    }
    public int getArtistId() {
        return artistId;
    }
    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Artist [artistId=" + artistId + ", name=" + name + "]";
    }

    
}

public class App {
    public static void main(String[] args) throws SQLException {
        // Connect to DB
        Connection connection = DriverManager.getConnection("jdbc:mem:project;MODE-PostgreSQL;DATABASE_TO_LOWER=TRUE;INIT=runscript from 'classpath:schema.sql'","sa","");
      

        HttpServlet artistServlet = new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
                List<Artist> artists = new ArrayList<>();
                ResultSet rs;
                try{
                    ResultSet rs = connection.prepareStatement("select * from artist").executeQuery();
                    while(rs.next()){
                        artists.add(new Artist(rs.getInt("ArtistId"), rs.getString("Name")));
                    }
                } catch (SQLException e){
                    System.err.println("Failed to retrieve from db: " + e.getSQLState());
                }
                
               //Get a JSON Mapper 
               ObjectMapper mapper = new ObjectMapper();
               String results = mapper.writeValueAsString(artists);
               resp.setContentType("application/json");
               resp.getWriter().println(results);
            }
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
                ObjectMapper mapper = new ObjectMapper();
                Artist newArtist = mapper.readValue(req.getInputStream(), Artist.class);
                System.out.println(newArtist);
                try{
                    PreparedStatement stmt = connection.prepareStatement("insert into 'artist' values(?, ?)");
                        stmt.setInt(1, newArtist.getArtistId());
                        stmt.setString(2, newArtist.getName());
                        stmt.executeUpdate();
                } catch(SQLException e){
                    System.err.println("Failed to insert: " + e.getMessage());
                }
                
            }
        };
        
        

        // Run server
        Tomcat server = new Tomcat();
        server.getConnector();
        server.addContext("", null);
        server.addServlet("", "defaultServlet", new Server()).addMapping("/*");

        server.addServlet("", "artistServlet", artistServlet).addMapping("/artists");
        try {
            server.start();
        } catch (LifecycleException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
