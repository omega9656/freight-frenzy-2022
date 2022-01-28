package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Squared Modular")
public class SquaredModular extends OmegaTeleopModular{
    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.SQUARED;
    }
}
