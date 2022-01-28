package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DuckMech {
    public DcMotorEx duckMech;
    Power currentPower;

    ElapsedTime time = new ElapsedTime();


    // degrees / sec
    public final double INITIAL_VELO = 15;
    public final double ACCEL = 25;

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
        duckMech.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void spin() {
        run(Power.FORWARDS);
    }

    public void stop() {
        run(Power.STOPPED);
    }

    public void optimalSpin(){
        time.reset();
        while(time.milliseconds() < 2000){
            duckMech.setVelocity(INITIAL_VELO + (time.seconds() * ACCEL), AngleUnit.DEGREES);
        }
        duckMech.setVelocity(0);
    }
}