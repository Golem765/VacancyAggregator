package com.javarush.test.level28.lesson15.big01;

import com.javarush.test.level28.lesson15.big01.model.Model;

/**
 * Created by Golem765 on 13.04.2016.
 */
public class Controller
{
    private Model model;

    public Controller(Model model) throws IllegalArgumentException
    {
        if(model == null)
            throw new IllegalArgumentException();
        this.model = model;
    }

    public void onCitySelect(String cityName)
    {
        model.selectCity(cityName);
    }
}
