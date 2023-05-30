package com.example.webmobiweather;

public class Model {
    private int Temp;
    private String Humidity;
    private String WSpeed;
    private String icon;
    private String time;

    public Model(int temp, String humidity, String WSpeed, String icon, String time) {
        this.Temp = temp;
        this.Humidity = humidity;
        this.WSpeed = WSpeed;
        this.icon = icon;
        this.time = time;
    }

    public int getTemp() {
        return Temp;
    }

    public void setTemp(int temp) {
        Temp = temp;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    public String getWSpeed() {
        return WSpeed;
    }

    public void setWSpeed(String WSpeed) {
        this.WSpeed = WSpeed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
