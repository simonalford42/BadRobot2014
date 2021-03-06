/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badrobot.commands;
import com.badrobot.OI;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *Drives the robot forward in a straight line for a time or to a distance from a barrier
 *can also be used in tele-op mode to keep a constant course.
 * @author Jacob
 */
public class DriveStraightForward extends BadCommand
{
    public long driveTime;
    public double driveDistance;
    public long startTime;
    public long currentTime;
    private double initialAngle;
    //constant for turn correction
    private static final double Kp = .05;
    private static double driveSpeed;
    
    boolean startedTime;
    
    /**
     * Parameterless constructor for DriveStraightForward command.
     */
    public DriveStraightForward()
    {
        requires((Subsystem) driveTrain);
        driveTime = -1;
        driveDistance = -1;
    }
    
    /**
     * Constructor for time mode of DriveStraightForward command.
     * @param t Time to drive forward
     */
    public DriveStraightForward(double t, boolean shiftUp)
    {
        requires((Subsystem) driveTrain);
        driveTime = (long) t*1000000;
        driveDistance = -1;
        if (shiftUp) {
            driveTrain.shiftUp();
        }
        else {
            driveTrain.shiftDown();
        }
    }
    
    /**
     * Constructor for distance mode of DriveStraightForward command.
     * @param d Distance to drive forward
     * @param t Can be anything, is not used in constructor-- Is there better way to do this?
     */
    public DriveStraightForward(double d, double t)
    {
        requires((Subsystem) driveTrain);
        driveDistance = d;
        driveTime = -1;
        driveTrain.shiftDown();
    }
    
    /**
     * Gets initial time and angle and gives their values to variables.
     */
    protected void initialize() {
        startTime = Utility.getFPGATime();
        //initialAngle = driveTrain.getGyro().getAngle();
        driveSpeed = 1.0;
    }
    
    /**
     * Class identifier for use in logging to the console.
     * @return Class name
     */
    public String getConsoleIdentity() {
        return "DriveStraightForward";
    }

    /**
     * Runs continuously once command is called.
     */
    protected void execute() {
        //if in time mode, update the time variable and drive the robot forward while correcting its angle
        if (SmartDashboard.getNumber("AutonomousDriveStraightTime") > 0)
        {
            //if (driveTrain.getLeftEncoder().get() > 0) {
                //SmartDashboard.putNumber("Encoder Click Count: ", driveTrain.getLeftEncoder().get());
            //}
            if (!startedTime) {
                startTime = Utility.getFPGATime();
                driveTrain.shiftDown();
                startedTime = true;
            }
            currentTime = Utility.getFPGATime();
            //driveTrain.getTrain().drive(driveSpeed, -(driveTrain.getGyro().getAngle()-initialAngle)*Kp);
            driveTrain.tankDrive(1, 1);
        }
        //if in distance mode, drive the robot forward while correcting its angle
        else if (driveDistance > 0)
        {
            
            driveTrain.getTrain().drive(driveSpeed, -(driveTrain.getGyro().getAngle()-initialAngle)*Kp);
        }
        //if in tele-op mode, drive the robot forward while correcting its angle
        //for now this is at a constant speed (driveSpeed), change later for operator input determined speed?
        else
        {
            driveTrain.getTrain().drive(driveSpeed, -(driveTrain.getGyro().getAngle()-initialAngle)*Kp);
        }
    }
    
    /**
     * Stops the command from running when true is returned.
     * @return True ends the command
     */
    protected boolean isFinished() {
        //if in time mode, and the time has exceeded the defined parameter, end the command
        if (SmartDashboard.getNumber("AutonomousDriveStraightTime") > 0 && 
                currentTime > (startTime + SmartDashboard.getNumber("AutonomousDriveStraightTime")*1000000))
        {
            return true;
        }
        //if in distance mode, and the robot has gotten closer to the wall than the initial parameter defined, end the command
        else if(driveDistance > 0 && driveTrain.getDistanceToWall() <= driveDistance)
        {
            return true;
        }
        //if in teleop mode and the rb button has become unpressed, end the command
        else if(driveDistance < 0 && driveTime < 0 && !OI.primaryController.isRBButtonPressed())
        {
            return true;
        }
        return false;
    }

    protected void end() { 
        driveTrain.tankDrive(0, 0);
    }

    protected void interrupted() {
        
    }
    
}
