package loli.io.db;

import loli.helper.AssTime;
import loli.helper.OnError;
import loli.ui.control.audiovideo.AudioImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AudioDB {

    private final Path dbPath;
    private final String tableName;

    public AudioDB(Path dbPath, String tableName) {
        this.dbPath = dbPath;
        this.tableName = tableName;
    }

    public AudioDB(String tableName){
        this(Path.of(Path.of("").toAbsolutePath() + "\\conf\\wav.db"), tableName);
    }

    public AudioDB(){
        this(Path.of(Path.of("").toAbsolutePath() + "\\conf\\wav.db"),
                "audio");
    }

    public boolean create() {
        try(Statement statement = Database.connect(dbPath)){
            statement.executeUpdate(String.format(
                    "create table %s (micros integer, image text)",
                    tableName
            ));
        } catch (SQLException e) {
            OnError.dialogErr(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public boolean add(AudioImage audioImage) {
        try(Statement statement = Database.connect(dbPath)){
            String base64 = "";
            long micros = (long) (audioImage.getStartTime().getMsTime() * 1000L);
            try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
                ImageIO.write(audioImage.getImage(), "png", out);
                base64 = Base64.getEncoder().encodeToString(out.toByteArray());
            }
            if(!base64.isEmpty()){
                statement.executeUpdate(String.format(
                        "insert into %s values (%d, %s)",
                        tableName, micros, base64));
            }
        } catch (SQLException | IOException e) {
            OnError.dialogErr(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public List<AudioImage> get(AssTime start, AssTime end){
        try(Statement statement = Database.connect(dbPath)){
            List<AudioImage> images = new ArrayList<>();
            long s = (long) (start.getMsTime() * 1000L);
            long e = (long) (end.getMsTime() * 1000L);

            ResultSet rs = statement.executeQuery(String.format(
                    "select * from %s where %s between %d and %d",
                    tableName, "micros", s, e
            ));

            while(rs.next()) {
                long micros = rs.getLong("micros");
                String base64 = rs.getString("image");
                byte[] image = Base64.getDecoder().decode(base64);
                try(ByteArrayInputStream in = new ByteArrayInputStream(image)){
                    BufferedImage img = ImageIO.read(in);
                    if(img != null){
                        AssTime startImg = new AssTime(((double)micros) / 1000d);
                        images.add(new AudioImage(img, startImg)); // 1 second duration
                    }
                }
            }

            return images;
        } catch (SQLException | IOException e) {
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