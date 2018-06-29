package com.sunset.trojanos.sunsetapp;


import com.google.gson.annotations.SerializedName;


public class Results {

    @SerializedName("sunrise")
    private String sunrise;

    @SerializedName("solar_noon")
    private String solarNoon;

    @SerializedName("day_length")
    private String dayLength;

    @SerializedName("astronomical_twilight_end")
    private String astronomicalTwilightEnd;

    @SerializedName("astronomical_twilight_begin")
    private String astronomicalTwilightBegin;

    @SerializedName("sunset")
    private String sunset;

    @SerializedName("civil_twilight_end")
    private String civilTwilightEnd;

    @SerializedName("nautical_twilight_end")
    private String nauticalTwilightEnd;

    @SerializedName("civil_twilight_begin")
    private String civilTwilightBegin;

    @SerializedName("nautical_twilight_begin")
    private String nauticalTwilightBegin;

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSolarNoon() {
        return solarNoon;
    }

    public void setSolarNoon(String solarNoon) {
        this.solarNoon = solarNoon;
    }

    public String getDayLength() {
        return dayLength;
    }

    public void setDayLength(String dayLength) {
        this.dayLength = dayLength;
    }

    public String getAstronomicalTwilightEnd() {
        return astronomicalTwilightEnd;
    }

    public void setAstronomicalTwilightEnd(String astronomicalTwilightEnd) {
        this.astronomicalTwilightEnd = astronomicalTwilightEnd;
    }

    public String getAstronomicalTwilightBegin() {
        return astronomicalTwilightBegin;
    }

    public void setAstronomicalTwilightBegin(String astronomicalTwilightBegin) {
        this.astronomicalTwilightBegin = astronomicalTwilightBegin;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getCivilTwilightEnd() {
        return civilTwilightEnd;
    }

    public void setCivilTwilightEnd(String civilTwilightEnd) {
        this.civilTwilightEnd = civilTwilightEnd;
    }

    public String getNauticalTwilightEnd() {
        return nauticalTwilightEnd;
    }

    public void setNauticalTwilightEnd(String nauticalTwilightEnd) {
        this.nauticalTwilightEnd = nauticalTwilightEnd;
    }

    public String getCivilTwilightBegin() {
        return civilTwilightBegin;
    }

    public void setCivilTwilightBegin(String civilTwilightBegin) {
        this.civilTwilightBegin = civilTwilightBegin;
    }

    public String getNauticalTwilightBegin() {
        return nauticalTwilightBegin;
    }

    public void setNauticalTwilightBegin(String nauticalTwilightBegin) {
        this.nauticalTwilightBegin = nauticalTwilightBegin;
    }

    @Override
    public String toString() {
        return
                "Results{" +
                        "sunrise = '" + sunrise + '\'' +
                        ",solar_noon = '" + solarNoon + '\'' +
                        ",day_length = '" + dayLength + '\'' +
                        ",astronomical_twilight_end = '" + astronomicalTwilightEnd + '\'' +
                        ",astronomical_twilight_begin = '" + astronomicalTwilightBegin + '\'' +
                        ",sunset = '" + sunset + '\'' +
                        ",civil_twilight_end = '" + civilTwilightEnd + '\'' +
                        ",nautical_twilight_end = '" + nauticalTwilightEnd + '\'' +
                        ",civil_twilight_begin = '" + civilTwilightBegin + '\'' +
                        ",nautical_twilight_begin = '" + nauticalTwilightBegin + '\'' +
                        "}";
    }
}