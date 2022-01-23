package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Cubed")
public class Cubed extends OmegaTeleop{

    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.CUBED;
    }
}
