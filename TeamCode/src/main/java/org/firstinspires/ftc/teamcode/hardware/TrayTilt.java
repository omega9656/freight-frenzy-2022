package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class TrayTilt {
    Servo trayTilt;
    Position currentMode;

    public enum Position {
        INTAKING(0.0),
        SLIDING(0.1),
        OUTTAKING(0.5);

        public double tiltPosition;

        Position(double tiltPosition) {
            this.tiltPosition = tiltPosition;
        }

    }

    public TrayTilt(DeviceManager deviceManager){
        trayTilt = deviceManager.trayTilt;

        trayTilt.setPosition(0);
    }

    public void run(Position position){
        trayTilt.setPosition(position.tiltPosition);
        currentMode = position;
    }

    public void ready(){
        run(Position.INTAKING);
    }

    public void partialTilt() {
        run(Position.SLIDING);
    }

    public void tilt(){
        run(Position.OUTTAKING);
    }
}