package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DuckMech {
    DcMotorEx duckMech;
    Power currentPower;

//    public boolean servoIsOn = false;

    public enum Power {
        FORWARDS(0.5),
        STOPPED(0);

        public double power;

        Power(double power) {
            this.power = power;
        }
    }

    public void run(Power power) {
        duckMech.setPower(power.power);
        currentPower = power;
    }

    public DuckMech(DeviceManager deviceManager) {
        currentPower = Power.STOPPED;
        duckMech = deviceManager.duckMech;
    }

    public void spin() {
        run(Power.FORWARDS);
    }

    public void stop() {
        run(Power.STOPPED);
    }
}