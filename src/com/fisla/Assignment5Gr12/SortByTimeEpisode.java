// Christian Fisla

package com.fisla.Assignment5Gr12;

import java.util.*;

public class SortByTimeEpisode implements Comparator<Episode> {
    @Override
    public int compare(Episode o1, Episode o2) {

        int timeTotalInSeconds1 = o1.getTime().getSeconds() + (o1.getTime().getMinutes() * 60) + (o1.getTime().getHours() * 3600);
        int timeTotalInSeconds2 = o2.getTime().getSeconds() + (o2.getTime().getMinutes() * 60) + (o2.getTime().getHours() * 3600);

        return timeTotalInSeconds1 - timeTotalInSeconds2;
    }
}