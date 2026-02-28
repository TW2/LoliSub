package loli.io.db;

import loli.exception.HColorException;
import loli.helper.AssStyle;
import loli.helper.OnError;
import loli.helper.StylesCollection;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StylesDB {

    private final Path dbPath;
    private final String tableName;

    public StylesDB(Path dbPath, String tableName) {
        this.dbPath = dbPath;
        this.tableName = tableName;
    }

    public StylesDB(String tableName){
        this(Path.of(Path.of("").toAbsolutePath() + "\\conf\\styles.db"), tableName);
    }

    public StylesDB(){
        this(Path.of(Path.of("").toAbsolutePath() + "\\conf\\styles.db"),
                "def");
    }

    public boolean create() {
        try(Statement statement = Database.connect(dbPath)){
            statement.executeUpdate(String.format(
                    "create table %s (collection text, name text, raw text)",
                    tableName
            ));
        } catch (SQLException e) {
            OnError.dialogErr(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public boolean add(StylesCollection collection){
        try(Statement statement = Database.connect(dbPath)){
            for(AssStyle style : collection.getStyles()){
                statement.executeUpdate(String.format(
                        "insert into %s values (%s, %s, %s)",
                        tableName,
                        collection.getName(),
                        style.getName(),
                        style.toRawLine()
                ));
            }
        }catch(SQLException | HColorException e){
            OnError.dialogErr(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public StylesCollection get(String collection){
        try(Statement statement = Database.connect(dbPath)){

            ResultSet rs = statement.executeQuery(String.format(
                    "select * from %s where %s = %s",
                    tableName, "collection", collection
            ));

            StylesCollection styles = new StylesCollection(collection);

            while(rs.next()) {
                AssStyle style = new AssStyle(rs.getString("raw"));
                style.setName(rs.getString("name"));
                styles.getStyles().add(style);
            }

            return styles;
        }catch(SQLException | HColorException e){
            OnError.dialogErr(e.getLocalizedMessage());
            return null;
        }
    }

    public boolean clear(){
        try(Statement statement = Database.connect(dbPath)){
            statement.executeUpdate(String.format("drop table if exists %s", tableName));
        } catch (SQLException e) {
            OnError.dialogErr(e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
