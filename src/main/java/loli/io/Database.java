package loli.io;

import loli.exception.HColorException;
import loli.helper.AssStyle;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String sqlID;
    private String uniqueID;

    public Database(Settings settings, String projectPath, String projectTitle,
                    int projectSeason, int projectEpisode, String databaseName) {
        Path path = projectPath == null ?
                Path.of(Path.of("").toAbsolutePath() + "\\conf") :
                Path.of(projectPath);

        String season = settings.getProjectSeason().replace("x", "");
        int countSeason = 0;
        for(char c : season.toCharArray()){
            if(Character.toString(c).equalsIgnoreCase("x")){
                countSeason++;
            }
        }
        String seasonFormat = countSeason != 0 ? "S%0" + countSeason + "d" : "";
        uniqueID = seasonFormat.isEmpty() ?
                projectSeason + "-" :
                String.format(seasonFormat, projectSeason);

        String episode = settings.getProjectEpisode().replace("x", "");
        int countEpisode = 0;
        for(char c : episode.toCharArray()){
            if(Character.toString(c).equalsIgnoreCase("x")){
                countEpisode++;
            }
        }
        String episodeFormat = countEpisode != 0 ? "S%0" + countEpisode + "d" : "";
        uniqueID += episodeFormat.isEmpty() ?
                projectEpisode + "" :
                String.format(episodeFormat, projectEpisode);

        uniqueID = projectTitle + " " + uniqueID;

        sqlID = "jdbc:sqlite:" + Path.of(path.toString(), "\\", databaseName, ".db");
    }

    public Database(){
        sqlID = "jdbc:sqlite:" + Path.of(Path.of("").toAbsolutePath() + "\\conf\\database.db");;
        uniqueID = "";
    }

    public String getSqlID() {
        return sqlID;
    }

    public void setSqlID(String sqlID) {
        this.sqlID = sqlID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    //====================================================================================
    //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    //==-- SQLite
    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888
    //====================================================================================

    public static void recreateStylesDB(String sqlID){
        // create a database connection
        try(Connection connection = DriverManager.getConnection(sqlID);
            Statement statement = connection.createStatement()){
            // 1/ Clear database
            statement.executeUpdate("DROP TABLE IF EXISTS styles");
            // 2/ Re/Create database
            // id index - sec seconds in double (x) - y percent in double (y)
            statement.executeUpdate("CREATE TABLE styles (id integer, tag text," +
                    " style text, comment text)");
        }catch(SQLException e){
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        }
    }

    public static int insertStyle(
            String sqlID, String uniqueID, AssStyle assStyle, String comment, int index){
        // create a database connection
        try(Connection connection = DriverManager.getConnection(sqlID);
            Statement statement = connection.createStatement()){
            // Styles
            // id               integer
            // tag              text
            // style            text
            // comment          text
            statement.executeUpdate(String.format(
                    "INSERT INTO styles VALUES(%d, '%s', '%s', '%s')",
                    index, uniqueID, assStyle.toRawLine(), comment
                    ));
            index++;
        }catch(SQLException e){
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        } catch (HColorException e) {
            throw new RuntimeException(e);
        }

        return index;
    }

    public static void updateStyle(
            String sqlID, String uniqueID, AssStyle assStyle, String comment, AssStyle old){
        // create a database connection
        try(Connection connection = DriverManager.getConnection(sqlID);
            Statement statement = connection.createStatement()){
            // Styles
            // id               integer
            // tag              text
            // style            text
            // comment          text
            statement.executeUpdate(String.format(
                    "UPDATE styles SET tag = '%s', style = '%s', comment = '%s' WHERE style = '%s'",
                    uniqueID, assStyle.toRawLine(), comment, old.toRawLine()
            ));
        }catch(SQLException e){
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        } catch (HColorException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeStyle(String sqlID, AssStyle assStyle){
        // create a database connection
        try(Connection connection = DriverManager.getConnection(sqlID);
            Statement statement = connection.createStatement()){
            // Styles
            // id               integer
            // tag              text
            // style            text
            // comment          text
            statement.executeUpdate(
                    "DELETE FROM styles WHERE style = '" + assStyle.toRawLine() + "'"
            );
        }catch(SQLException e){
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        } catch (HColorException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Object[]> getStyles(String sqlID, String uniqueID){
        final List<Object[]> listOfObjects = new ArrayList<>();
        // create a database connection
        try(Connection connection = DriverManager.getConnection(sqlID);
            Statement statement = connection.createStatement()){
            // Styles
            // id               integer
            // tag              text
            // style            text
            // comment          text
            ResultSet rs = statement.executeQuery("SELECT * FROM styles");
            while(rs.next()){
                Object[] obj = new Object[]{
                        rs.getInt("id"),
                        rs.getString("tag"),
                        rs.getString("style"),
                        rs.getString("comment")
                };
                listOfObjects.add(obj);
            }
        }catch(SQLException e){
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        }
        return listOfObjects;
    }

    //-------------------------------------------------------------------


}
