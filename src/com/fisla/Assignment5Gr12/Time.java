// Christian Fisla

package com.fisla.Assignment5Gr12;

public class Time {

    // Attributes about the Time object
    private int seconds, minutes, hours;

    // Desc: Time object Constructor. Takes in attributes and creates the Time object
    // Param: Time Object
    // Return: n/a
    public Time (int hours, int minutes, int seconds) {

        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
    }

    // Desc: Adds the values of two Time objects
    // Param: Time Object
    // Return: Time Object
    public Time addTime(Time t) {
        this.seconds = this.seconds + t.getSeconds();
        this.minutes = this.minutes + t.getMinutes();
        this.hours = this.hours + t.getHours();

        // Carry over if overflow
        if (this.seconds > 59) {
            this.seconds -= 60;
            this.minutes++;
        }
        // Carry over if overflow
        if (this.minutes > 59) {
            this.minutes -= 60;
            this.hours++;
        }

        return this;
    }

    // Desc: Subtracts the values of two Time objects
    // Param: Time Object
    // Return: Time Object
    public Time subTime(Time t) {
        this.seconds = this.seconds - t.getSeconds();
        this.minutes = this.minutes - t.getMinutes();
        this.hours = this.hours - t.getHours();

        // Carry over if below zero
        if (this.minutes < 0) {
            this.minutes += 60;
            this.hours--;
        }
        // Carry over if below zero
        if (this.seconds < 0) {
            this.seconds += 60;
            this.minutes++;
        }

        return this;
    }

    // GETTERS AND SETTERS
    public int getSeconds() {
        return this.seconds;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public int getHours() {
        return this.hours;
    }

    // toString()
    public String toString() {
        return String.format("Duration: %d %s, %d %s, %d %s", this.hours, (this.hours == 1) ? "hour" : "hours", this.minutes, (this.minutes == 1) ? "minute" : "minutes", this.seconds, (this.seconds == 1) ? "second" : "seconds");
    }
}