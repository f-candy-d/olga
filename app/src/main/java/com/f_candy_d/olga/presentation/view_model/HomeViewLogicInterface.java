package com.f_candy_d.olga.presentation.view_model;

import com.f_candy_d.dutils.Group;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.vmvl.ActivityViewLogicInterface;

import java.util.ArrayList;

/**
 * Created by daichi on 9/10/17.
 */

public interface HomeViewLogicInterface extends ActivityViewLogicInterface {

    void showInitialUpcomingTasks(ArrayList<Group<Task>> taskGroups);
}
