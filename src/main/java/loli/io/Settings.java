package loli.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Settings {

    private boolean defaultFolder;
    private String customFolder;

    private boolean darkMode;

    private String projectSeason;
    private String projectEpisode;
    private String projectStylesPrefix;

    private Settings(){
        defaultFolder = true;
        customFolder = null;
        darkMode = false;
        projectSeason = "Sxx";
        projectEpisode = "Exx";
        projectStylesPrefix = "";
    }

    public static void write(String path, Settings settings){
        try(PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8)) {
            pw.println("defaultFolder=" + (settings.isDefaultFolder() ?
                    "1" : ""));
            pw.println("customFolder=" + (settings.getCustomFolder() == null
                    || settings.getCustomFolder().isEmpty() ?
                    "" : settings.getCustomFolder()));
            pw.println("darkMode=" + (settings.isDarkMode() ?
                    "1" : ""));
            pw.println("projectSeason=" + settings.getProjectSeason());
            pw.println("projectEpisode=" + settings.getProjectEpisode());
            pw.println("projectStylesPrefix=" + settings.getProjectStylesPrefix());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Settings read(String path){
        Settings settings = new Settings();
        if(!(new File(path)).exists()) return settings;

        try(FileReader fr = new FileReader(path, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(fr)
        ) {
            String line;
            while((line = br.readLine()) != null){
                String param = line.substring(0, line.indexOf("=") + 1);
                String value = line.substring(param.length());
                switch(param){
                    case "defaultFolder=" -> settings.setDefaultFolder(!value.isEmpty() && !value.equals("0"));
                    case "customFolder=" -> settings.setCustomFolder(value.isEmpty() ? null : value);
                    case "darkMode=" -> settings.setDarkMode(!value.isEmpty() && !value.equals("0"));
                    case "projectSeason=" -> settings.setProjectSeason(value);
                    case "projectEpisode=" -> settings.setProjectEpisode(value);
                    case "projectStylesPrefix=" -> settings.setProjectStylesPrefix(value);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return settings;
    }

    public boolean isDefaultFolder() {
        return defaultFolder;
    }

    public void setDefaultFolder(boolean defaultFolder) {
        this.defaultFolder = defaultFolder;
    }

    public String getCustomFolder() {
        return customFolder;
    }

    public void setCustomFolder(String customFolder) {
        this.customFolder = customFolder;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public String getProjectSeason() {
        return projectSeason;
    }

    public void setProjectSeason(String projectSeason) {
        this.projectSeason = projectSeason;
    }

    public String getProjectEpisode() {
        return projectEpisode;
    }

    public void setProjectEpisode(String projectEpisode) {
        this.projectEpisode = projectEpisode;
    }

    public String getProjectStylesPrefix() {
        return projectStylesPrefix;
    }

    public void setProjectStylesPrefix(String projectStylesPrefix) {
        this.projectStylesPrefix = projectStylesPrefix;
    }
}
