package com.f_candy_d.olga.domain.filter;

/**
 * Created by daichi on 9/23/17.
 */

public class DefaultFilterFactory {

    private DefaultFilterFactory() {}

    public static TaskFilter createAllFilter() {
        return new TaskFilter();
    }

    public static TaskFilter createAchievedFilter() {
        TaskFilter filter = createAllFilter();
        filter.setPickUpAchievementFlag(TaskFilter.FLAG_PICKUP_ONLY_ACHIEVED);
        return filter;
    }

    public static TaskFilter createNotAchievedFilter() {
        TaskFilter filter = createAllFilter();
        filter.setPickUpAchievementFlag(TaskFilter.FLAG_PICKUP_ONLY_NOT_ACHIEVED);
        return filter;
    }
}
