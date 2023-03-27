// Christian Fisla

package com.fisla.Assignment5Gr12;

import java.util.*;

public class SortByTitleTVShow implements Comparator<TVShow> {
    @Override
    public int compare(TVShow o1, TVShow o2) {
        return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
    }
}