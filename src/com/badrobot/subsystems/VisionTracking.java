/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badrobot.subsystems;

import com.badrobot.subsystems.interfaces.IVisionTracking;
/**
 *
 * @author Steve
 */
public class VisionTracking extends BadSubsystem implements IVisionTracking {

    Camera camera;
    

    protected void initialize() 
    {
        
    }

    public String getConsoleIdentity()
    {
        return "VisionTrackingSubsystem";
    }

    protected void initDefaultCommand() 
    {
    
    }
    
}
