package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DeviceManager {
    DcMotorEx backRight;
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx frontLeft;

    DcMotorEx slides;
    Servo dump;

    DcMotorEx intake;

    CRServo duckMech;

    HardwareMap hardwareMap;

    /***
     *
     * @param hardwareMap - readies hardwareMap so it can initialize devices in init()
     */
    public DeviceManager(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    /**
     * initializes the robot, get hardware from the expansion hub
     * @param autoRunning - indicates whether auto is running so we don't initialize our drive motors
     */
    void init(boolean autoRunning){
        backRight = hardwareMap.get(DcMotorEx.class, "back_right");
        frontRight = hardwareMap.get(DcMotorEx.class, "front_right");
        backLeft = hardwareMap.get(DcMotorEx.class, "back_left");
        frontLeft = hardwareMap.get(DcMotorEx.class, "front_left");

        slides = hardwareMap.get(DcMotorEx.class, "slides");
        dump = hardwareMap.get(Servo.class, "dump");

        intake = hardwareMap.get(DcMotorEx.class, "intake");

        duckMech = hardwareMap.get(CRServo.class, "duck_mechanism");
    }

}
