package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "SLOW CUBED TEST")
public class CubedModularSlow extends OmegaTeleopSlowModular{
    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.NORMAL;
    }
}
