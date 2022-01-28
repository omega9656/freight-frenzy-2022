package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class TrayTilt {
    public Servo trayTilt;
    Position currentMode;

//    public static double degreesToServoPos(double angle) {
//        return angle / 180.0;
//    }

    public enum Position {
//        INTAKING(0.0),
//        SLIDING(0.1),
//        OUTTAKING(0.5);
        INTAKING(0.33),
        SLIDING(.45),
        OUTTAKING(0.75);

        public double tiltPosition;

        Position(double tiltPosition) {
            this.tiltPosition = tiltPosition;
        }

    }

    public TrayTilt(DeviceManager deviceManager){
        trayTilt = deviceManager.trayTilt;
        trayTilt.setDirection(Servo.Direction.REVERSE);
        trayTilt.setPosition(Position.INTAKING.tiltPosition);
    }

    public void run(Position position){
        trayTilt.setPosition(position.tiltPosition);
        currentMode = position;
    }

    public void ready(){
        run(Position.INTAKING);
    }

    public void parallel() {
        run(Position.SLIDING);
    }

    public void tilt(){
        run(Position.OUTTAKING);
    }
}