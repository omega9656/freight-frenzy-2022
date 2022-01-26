package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DuckMech {
    CRServo duckMech;
    ServoPower currentPower;

//    public boolean servoIsOn = false;

    public enum ServoPower {
        FORWARDS(0.5),
        STOPPED(0);

        public double power;

        ServoPower(double power) {
            this.power = power;
        }
    }

    public void run(ServoPower servoPower) {
        duckMech.setPower(servoPower.power);
        currentPower = servoPower;
    }

    public DuckMech(DeviceManager deviceManager) {
        currentPower = ServoPower.STOPPED;

        duckMech = deviceManager.duckMech;
    }

    public void spin() {
        run(ServoPower.FORWARDS);
    }

    public void stop() {
        run(ServoPower.STOPPED);
    }
}