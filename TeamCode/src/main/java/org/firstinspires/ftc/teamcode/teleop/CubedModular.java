package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Cubed Modular")
public class CubedModular extends OmegaTeleopModular{
    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.CUBED;
    }
}
