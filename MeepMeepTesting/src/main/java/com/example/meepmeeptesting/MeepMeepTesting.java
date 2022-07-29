package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.heading.TangentInterpolator;
import com.acmerobotics.roadrunner.profile.VelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Vector;

public class MeepMeepTesting {
    public static void main(String[] args) {
        TangentInterpolator ti = new TangentInterpolator(50);

        MeepMeep meepMeep = new MeepMeep(1000);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 60, 58.2, 1.0471975511965976, 13.5)
                .setStartPose(new Pose2d(-10, -60, Math.toRadians(270)))
                .followTrajectorySequence(drive -> // start pose
                        drive.trajectorySequenceBuilder(new Pose2d(10, -60, Math.toRadians(270)))
                                // start position to drop off indicated by barcode
                                .lineToConstantHeading(new Vector2d(-11, -39))
                                // splines to warehouse entrance
                                .splineToLinearHeading(new Pose2d(20, -60, Math.toRadians(0)), Math.toRadians(0))
                                // moves forward into warehouse to pickup
//                                .splineToLinearHeading(new Pose2d(50, -61, Math.toRadians(0)), Math.toRadians(0))
                                .forward(30)
                                // backs up for spline
                                .back(10)
                                .splineToSplineHeading(new Pose2d(-11, -39, Math.toRadians(-90)), Math.toRadians(-270))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}