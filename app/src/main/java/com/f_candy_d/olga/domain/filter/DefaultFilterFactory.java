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

    public static TaskFilter createNowFilter() {
        TaskFilter filter = createNotAchievedFilter();
        filter.setFilterName(FILTER_NAME_NOW);
        return filter;
    }

    public static TaskFilter createAllFilter() {
        TaskFilter filter = new TaskFilter();
        filter.setFilterName(FILTER_NAME_ALL);
        return filter;
    }

    public static TaskFilter createAchievedFilter() {
        TaskFilter filter = createAllFilter();
        filter.setPickUpAchievementFlag(TaskFilter.FLAG_PICKUP_ONLY_ACHIEVED);
        filter.setFilterName(FILTER_NAME_ACHIEVED);
        return filter;
    }

    public static TaskFilter createNotAchievedFilter() {
        TaskFilter filter = createAllFilter();
        filter.setPickUpAchievementFlag(TaskFilter.FLAG_PICKUP_ONLY_NOT_ACHIEVED);
        filter.setFilterName(FILTER_NAME_NOT_ACHIEVED);
        return filter;
    }
}
