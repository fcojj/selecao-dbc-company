package com.github.conferencetrack.controller;

import com.github.conferencetrack.model.ModelFactory;

/**
 * Created by Jonas Rodrigues on 18/08/2016.
 */
public class Controller {

    private ModelFactory modelFactory;

    private ModelFactory getModelFactory() {
        if (modelFactory == null) {
            modelFactory = new ModelFactory();

            //magic here...loading data file and scheduling talks
            modelFactory.manufactureModel();
        }

        return modelFactory;
    }

    public void showTalksScheduling() {
        getModelFactory().showTalksScheduling();
    }
}
