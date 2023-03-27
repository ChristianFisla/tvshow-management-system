// Christian Fisla

package com.fisla.Assignment5Gr12;

import java.util.*;

public class SortByNumEpisode implements Comparator<Episode> {
    @Override
    public int compare(Episode o1, Episode o2) {
        return o1.getEpisode() - o2.getEpisode();
    }
}