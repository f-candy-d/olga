package com.f_candy_d.olga.domain.filter;

/**
 * Created by daichi on 9/23/17.
 */

public class DefaultFilterFactory {

    /**
     * Default filter names
     */
    public static final String FILTER_NAME_NOW = "Now";
    public static final String FILTER_NAME_ALL = "All";
    public static final String FILTER_NAME_ACHIEVED = "Achieved";
    public static final String FILTER_NAME_NOT_ACHIEVED = "Not Achieved";

    private DefaultFilterFactory() {}

    public static NoteFilter createNowFilter() {
        NoteFilter filter = createNotAchievedFilter();
        filter.setFilterName(FILTER_NAME_NOW);
        return filter;
    }

    public static NoteFilter createAllFilter() {
        NoteFilter filter = new NoteFilter();
        filter.setFilterName(FILTER_NAME_ALL);
        return filter;
    }

    public static NoteFilter createAchievedFilter() {
        NoteFilter filter = createAllFilter();
        filter.setPickUpAchievementFlag(NoteFilter.FLAG_PICKUP_ONLY_ACHIEVED);
        filter.setFilterName(FILTER_NAME_ACHIEVED);
        return filter;
    }

    public static NoteFilter createNotAchievedFilter() {
        NoteFilter filter = createAllFilter();
        filter.setPickUpAchievementFlag(NoteFilter.FLAG_PICKUP_ONLY_NOT_ACHIEVED);
        filter.setFilterName(FILTER_NAME_NOT_ACHIEVED);
        return filter;
    }
}
