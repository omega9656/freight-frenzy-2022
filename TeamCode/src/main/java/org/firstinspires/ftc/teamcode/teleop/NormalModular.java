package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Modular Normal Drive")
public class NormalModular extends OmegaTeleopModular {

    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.NORMAL;
    }
}
