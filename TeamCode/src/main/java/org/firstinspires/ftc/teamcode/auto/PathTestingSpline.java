package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
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

import java.util.List;

@Autonomous(name = "Path Test Spline")
public class PathTestingSpline extends LinearOpMode {
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

    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
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

    Pose2d startPose = new Pose2d(0, 0, 0);

    @Override
    public void runOpMode() throws InterruptedException {

        initTfod();
        initVuforia();

        time  = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        robot = new Robot(hardwareMap);
        robot.init(true);
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(startPose);

        waitForStart();

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
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
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
        }

        if(opModeIsActive() && !isStopRequested()){
            splinePaths();
        }
    }

    public void executeAutoPath(){
        Trajectory offWall = drive.trajectoryBuilder(startPose)
                .back(2)
                .build();

        Trajectory strafeHub = drive.trajectoryBuilder(offWall.end())
                .strafeLeft(19)
                .build();

        Trajectory forwardHub = drive.trajectoryBuilder(strafeHub.end())
                .back(18)
                .build();

        Trajectory frontWall = drive.trajectoryBuilder(forwardHub.end())
                .forward(17.5)
                .build();

        Trajectory backToDuck = drive.trajectoryBuilder(frontWall.end().plus(new Pose2d(0, 0, Math.toRadians(-90))))
                .forward(44)
                .build();

        Trajectory parkWarehouseHalf = drive.trajectoryBuilder(backToDuck.end())
                .back(50)
                .build();

        Trajectory strafeWall = drive.trajectoryBuilder(parkWarehouseHalf.end())
                .strafeLeft(8)
                .build();

        Trajectory park = drive.trajectoryBuilder(strafeWall.end())
                .back(45)
                .build();

        drive.followTrajectory(offWall);

        robot.trayTilt.parallel();
        robot.slides.dropOffHigh();

        drive.followTrajectory(strafeHub);
        drive.followTrajectory(forwardHub);

        time.reset();
        while(time.milliseconds() < 1000){
            robot.trayTilt.tilt();
        }
        robot.trayTilt.parallel();
        robot.slides.pickUp();
//        while(robot.slides.slides.getCurrentPosition()-20 > robot.slides.slides.getTargetPosition()){ }

        drive.followTrajectory(frontWall);
        robot.trayTilt.ready();
        drive.turn(Math.toRadians(-90));
        drive.followTrajectory(backToDuck);
        robot.duckMech.optimalSpin();
        drive.followTrajectory(parkWarehouseHalf);
        drive.followTrajectory(strafeWall);
        drive.followTrajectory(park);


    }

    public void splinePaths(){
        Trajectory t1 = drive.trajectoryBuilder(startPose)
                .lineToConstantHeading(new Vector2d(22, 18))
                .build();

        drive.followTrajectory(t1);
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
