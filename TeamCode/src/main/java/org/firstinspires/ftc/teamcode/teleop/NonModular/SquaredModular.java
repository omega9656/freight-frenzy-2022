package org.firstinspires.ftc.teamcode.teleop.NonModular;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.NonModular.OmegaTeleopModular;

@TeleOp(name = "Squared Modular")
public class SquaredModular extends OmegaTeleopModular {
    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.SQUARED;
    }
}
