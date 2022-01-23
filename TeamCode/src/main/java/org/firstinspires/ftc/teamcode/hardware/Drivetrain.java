package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Drivetrain {

    DcMotorEx backRight;
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx frontLeft;

    /***
     * initializes motors with hardware from DeviceManager
     * @param deviceManager - instantiates hardware with motors in code, passed in Robot class
     */
    public Drivetrain(DeviceManager deviceManager){
        backRight = deviceManager.backRight;
        frontRight = deviceManager.frontRight;
        backLeft = deviceManager.backLeft;
        frontLeft = deviceManager.frontLeft;

        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}