package com.mbras.cavavin.cucumber.stepdefs;

import com.mbras.cavavin.CavavinApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = CavavinApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
