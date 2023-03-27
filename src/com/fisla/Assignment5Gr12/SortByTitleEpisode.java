// Christian Fisla

package com.fisla.Assignment5Gr12;

import java.util.*;

public class SortByTitleEpisode implements Comparator<Episode> {
    @Override
    public int compare(Episode o1, Episode o2) {
        return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
    }
}