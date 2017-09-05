package util;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.Properties;

import static javafx.scene.control.Alert.AlertType;

public class Util {
    public static final String VERSION = "alpha-2.0";
    private String mediaDirectory;
    private String secondMediaDirectory;
    private File configFile = new File(getUserDataDirectory() + "config.properties");
    private int timesStarted;
    private String activeTheme;

    public Util() {
    }

    public void createAlert(String header, String alert, AlertType alertType) {
        Alert alert1 = new Alert(alertType);
        alert1.setTitle(alertType.name());
        alert1.setHeaderText(header);
        alert1.setContentText(alert);
        alert1.showAndWait();
    }

    public String getActiveTheme() {
        if (activeTheme == null) {
            return "./src/themes/metroLight.css";
        } else {
            return activeTheme;
        }
    }

    public void setActiveTheme(String activeTheme) {
        this.activeTheme = activeTheme;
    }

    public int getTimesStarted() {
        return timesStarted;
    }

    public void setTimesStarted(int timesStarted) {
        this.timesStarted = timesStarted;
    }

    public String getUserDataDirectory() {
        return System.getProperty("user.home") + File.separator + ".mmp" + File.separator;
    }

    public String getMediaDirectory() {
        if (mediaDirectory != null) {
            return mediaDirectory;
        } else {
            return System.getProperty("user.home") + File.separator + "media" + File.separator;
        }
    }

    public String getSecondMediaDirectory() {
        if (secondMediaDirectory == null) {
            return "";
        } else {
            return secondMediaDirectory;
        }
    }

    public void setSecondMediaDirectory(String secondMediaDirectory) {
        this.secondMediaDirectory = secondMediaDirectory;
    }
    public void setMediaDirectory(String mediaDirectory) {
        this.mediaDirectory = mediaDirectory;
    }

    public String getDefaultMediaDirectory() {
        return System.getProperty("user.home") + File.separator + "media" + File.separator;
    }

    public void createPropertiesFile() {

        try {
            Properties properties = new Properties();

            properties.setProperty("mediaDirectory", getDefaultMediaDirectory());
            properties.setProperty("secondMediaDirectory", "");
            properties.setProperty("timesStarted", "0");
            properties.setProperty("activeTheme", getActiveTheme());

            FileWriter fileWriter = new FileWriter(configFile);
            properties.store(fileWriter, "path settings");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("created");
    }

    public void loadProperties() {

        try {
            FileReader fileReader = new FileReader(configFile);

            Properties properties = new Properties();
            properties.load(fileReader);

            setMediaDirectory(properties.getProperty("mediaDirectory"));
            setSecondMediaDirectory(properties.getProperty("secondMediaDirectory"));
            setTimesStarted(Integer.parseInt(properties.getProperty("timesStarted")));
            setActiveTheme(properties.getProperty("activeTheme"));

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("loaded");
    }

    public void saveProperties() {

        try {
            Properties properties = new Properties();

            properties.setProperty("mediaDirectory", getMediaDirectory());
            properties.setProperty("secondMediaDirectory", getSecondMediaDirectory());
            properties.setProperty("timesStarted", String.valueOf(getTimesStarted()));
            properties.setProperty("activeTheme", getActiveTheme());

            FileWriter fileWriter = new FileWriter(configFile);
            properties.store(fileWriter, "path settings");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("saved");
    }

    public void reset() {
        setMediaDirectory(System.getProperty("user.home") + File.separator + "media" + File.separator);
        setSecondMediaDirectory(null);
    }
}