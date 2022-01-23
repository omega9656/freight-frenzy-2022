package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Squared")
public class Squared extends OmegaTeleop{
    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.CUBED;
    }
}
