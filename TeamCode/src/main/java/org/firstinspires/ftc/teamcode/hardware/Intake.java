package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Intake {
    MotorVelocity currentVelocity;
    public DcMotorEx intake; //declaring Intake

    public Intake(DeviceManager deviceManager) {
        intake = deviceManager.intake;

        intake.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER); // when ran, ran without encoder?
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE); // when power is 0, BRAKE
        intake.setDirection(DcMotorSimple.Direction.REVERSE);


        currentVelocity = MotorVelocity.STOP; // default power is at STOP(0)
    }
    public enum MotorVelocity {
        // set the velocities for the intake at different states
        IN(128),
        OUT(-60),
        STOP(0);

        public double intakeSpeed;

        MotorVelocity(double intakeSpeed) {
            this.intakeSpeed = intakeSpeed;
        }
    }

    public void run(MotorVelocity motorVelocity){
        intake.setVelocity(motorVelocity.intakeSpeed, AngleUnit.DEGREES);
        this.currentVelocity = motorVelocity;
    }

    public void in(){
        run(MotorVelocity.IN); //calling the run method for the IN power
    }
    public void out(){
        run(MotorVelocity.OUT); //calling the run method for the OUT power
    }
    public void stop(){
        run(MotorVelocity.STOP); //calling the run method for the STOP power
    }
}