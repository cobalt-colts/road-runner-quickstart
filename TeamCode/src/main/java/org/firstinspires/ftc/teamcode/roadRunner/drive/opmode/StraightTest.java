package org.firstinspires.ftc.teamcode.roadRunner.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.oldPrograms.usedAtState.DriveTrain6547State;
import org.firstinspires.ftc.teamcode.roadRunner.drive.DriveTrain6547Offseason;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
public class StraightTest extends LinearOpMode {
    public static double DISTANCE = 24;

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrain6547Offseason bot = new DriveTrain6547Offseason(this);

        Trajectory trajectory = bot.trajectoryBuilder()
                .forward(DISTANCE)
                .build();
        waitForStart();

        if (isStopRequested()) return;

        bot.followTrajectorySync(trajectory);
    }
}
