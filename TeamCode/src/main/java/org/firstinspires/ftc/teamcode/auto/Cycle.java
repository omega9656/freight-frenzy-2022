package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilderKt;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Robot;

import java.nio.channels.spi.AbstractSelectionKey;
import java.util.List;

@Autonomous(name = "Cycle Near")
public class Cycle extends LinearOpMode {
    /* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
     * the following 4 detectable objects
     *  0: Ball,
     *  1: Cube,
     *  2: Duck,
     *  3: Marker (duck location tape marker)
     *
     *  Two additional model assets are available which only contain a subset of the objects:
     *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
     *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
     */
    //private static final String TFOD_MODEL_ASSET = "Skystone.tflite";

    // position of element (1, 2, 3)
    private int pos;
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AcDQfuj/////AAABmXUjqtdnMEsGh6gDa7LYFopzRUf6HRbC0ikH7cae27nG9ziqFCiHTvzrkU3J62YaqQmZrgYj1sKCNNxd7Aka3GoB9C4ciJNEJLFvYi3cP9HzG8iJf3MftEoeuaEV894LYyYKbUNIErDIGRORLkDctd7d+pktHnT57AxufAXj+MXOD4KxeHXlAugxJTFvDDkChUC6LJiFd/4MVUyhVKgOwYaJLzuGZPijETVf/LchUlikMDG1QFK4WCAe1N/ke98+rXej6aVMUqEFzpFka2Be7tn2R6D0lQ0HDs4ezlqH2Fvj5T67iHy7Wy4QI8uavaeZC/to7oSzvu5Ff3HXNO9MEo/yIGsUqUHRs3HZwufIQBZz";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;


    ElapsedTime time;
    Robot robot;
    SampleMecanumDrive drive;


    Pose2d startPose = new Pose2d(10, -62, Math.toRadians(0)); // 270 by itself was fine

    @Override
    public void runOpMode() throws InterruptedException {

        time  = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        robot = new Robot(hardwareMap);
        robot.init(true);
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(startPose);


        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.8, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        while (!isStopRequested() && !opModeIsActive()) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if(updatedRecognitions.size() == 0){
                        pos = 1;
                    }
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {

                        if(recognition.getLabel().equals("Cube")){
                            if(recognition.getLeft() < 200 && recognition.getTop() < 210){
                                pos = 2;
                            } else if(recognition.getLeft() < 450 && recognition.getTop() < 210){
                                pos = 3;
                            } else {
                                pos = 0;
                            }
                        }
                        telemetry.addData("DUCK POSITION IN LEVEL ", pos);

                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        i++;
                    }
                    telemetry.update();
                }
            }
        }

        waitForStart();

        if(opModeIsActive() && !isStopRequested()){
            splineCycle();
        }
    }

    //Pose2d startPose = new Pose2d(10, -60, Math.toRadians(0));
    void splineCycle(){


        Trajectory toHub = drive.trajectoryBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-19, -80))
                .build();

        Trajectory depot = drive.trajectoryBuilder(toHub.end())
                .splineToLinearHeading(new Pose2d(13, -55, Math.toRadians(90)), 0)
                .build();

        Trajectory intake = drive.trajectoryBuilder(depot.end())
                .forward(30)
                .build();

        Trajectory antiIntake = drive.trajectoryBuilder(intake.end())
                .back(40)
                .build();

        Trajectory antiDepot = drive.trajectoryBuilder(antiIntake.end())
                .splineToLinearHeading(new Pose2d(-19, -80, 270), Math.toRadians(90))
                .build();

        /*
        Trajectory toHub = drive.trajectoryBuilder(startPose)
                .lineToConstantHeading(new Vector2d(39, -11.5))
                .build();

        Trajectory depot = drive.trajectoryBuilder(toHub.end())
                .splineToLinearHeading(new Pose2d(62, 15.5, Math.toRadians(90)), 100)
                .build();

        Trajectory intake = drive.trajectoryBuilder(depot.end())
                .forward(30)
                .build();

        Trajectory antiIntake = drive.trajectoryBuilder(intake.end())
                .back(40)
                .build();

        Trajectory antiDepot = drive.trajectoryBuilder(antiIntake.end())
                .splineToLinearHeading(new Pose2d(39, -11.5, 270), Math.toRadians(90))
                .build();*/


        drive.followTrajectory(toHub);
        drive.followTrajectory(depot);
        drive.followTrajectory(intake);
        drive.followTrajectory(antiIntake);
        drive.followTrajectory(antiDepot);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        //tfodParameters.
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
