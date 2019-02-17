package com.wsdc.g_a_0;

import com.wsdc.g_a_0.annotation.PluginProcessor;

import org.junit.Test;

import java.net.MalformedURLException;

public class TestProcess {
    @Test
    public void process0(){
        PluginProcessor processor = new PluginProcessor();
        try {
            processor.process0();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
