// Christian Fisla

package com.fisla.Assignment5Gr12;

import java.util.StringTokenizer;

public class Episode implements Comparable<Episode> {

    // Attributes to store info about each Episode Object
    private int episode, season;
    private String title;
    private boolean isWatched;
    private Time time;

    // Desc: Episode object Constructor. Takes in attributes and creates the Episode object
    // Param: The episode, the title, the timestamp in format ("xx:yy:zz:"), if it's been watched already, and the season
    // Return: n/a
    public Episode(int episode, String title, String timeStamp, boolean isWatched, int season) {
        this.episode = episode;
        this.title = title;
        this.isWatched = isWatched;
        this.season = season;

        StringTokenizer tS = new StringTokenizer(timeStamp, ":");

        this.time = new Time(Integer.parseInt(tS.nextToken()), Integer.parseInt(tS.nextToken()), Integer.parseInt(tS.nextToken()));

    }
    // Desc: Secondary Episode Constructor for the purpose of using it for binary search.
    // Param: isKey with is used to differentiate between Constructors, and the title
    // Return: n/a
    public Episode(boolean isKey, String title) {
        this.title = title;
    }
    // Desc: Watch an episode
    // Param: n/a
    // Return: n/a
    public void watch() {
        isWatched = true;
    }

    // GETTERS AND SETTERS
    public int getSeason() {
        return this.season;
    }
    public int getEpisode() {
        return this.episode;
    }
    public String getTitle() {
        return this.title;
    }
    public boolean getIsWatched() {
        return this.isWatched;
    }
    public Time getTime() {
        return this.time;
    }

    // toString()
    public String toString() {

        return String.format("%nTitle: %s%nSeason: %d%nEpisode: %s%nSeen Before?: %s%nDuration: %d %s, %d %s, %d %s", this.title, this.season, this.episode, (this.isWatched) ? "Yes!" : "No", this.time.getHours(), (this.time.getHours() == 1) ? "hour" : "hours", this.time.getMinutes(), (this.time.getMinutes() == 1) ? "minute" : "minutes", this.time.getSeconds(), (this.time.getSeconds() == 1) ? "second" : "seconds");
    }

    // Specify default sorting/searching
    @Override
    public int compareTo(Episode o1) {
        return this.title.compareTo(o1.title);
    }
}