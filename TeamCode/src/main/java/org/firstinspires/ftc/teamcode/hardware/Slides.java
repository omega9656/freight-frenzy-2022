package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Slides {
    public DcMotorEx slides;
    Position currentPos;

    public static final double maxPower = 0.6;

    // 806 1st
    public enum Position {
        DROP_OFF_HIGH(1530),
        DROP_OFF_MIDDLE(1128),
        DROP_OFF_BOTTOM(806),
        PICKUP(0); //picking up game elements

        public int armPosition;

        Position(int armPosition){
            this.armPosition = armPosition;
        }
    }

    public Slides(DeviceManager deviceManager){
        slides = deviceManager.slides;

        slides.setPower(maxPower);

        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setDirection(DcMotorSimple.Direction.REVERSE);

        // sets default position
        slides.setTargetPosition(0);
    }


    public void run(Position position){
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setTargetPosition(position.armPosition);

        // updates the arm position to where it is currently
        currentPos = position;
    }

    //sets arm to 'drop off high' position
    public void dropOffHigh(){
        run(Position.DROP_OFF_HIGH);
    }
    //sets arm to 'drop off middle' position
    public void dropOffMiddle(){
        run(Position.DROP_OFF_MIDDLE);
    }

    // sets arm to level 1 position
    public void dropOffLow() {
        run(Position.DROP_OFF_BOTTOM);
    }
    //sets arm to 'pick up' position
    public void pickUp(){
        run(Position.PICKUP);
    }

}