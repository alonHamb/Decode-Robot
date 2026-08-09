package org.firstinspires.ftc.teamcode.opmodes.autonomous

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.seattlesolvers.solverslib.command.CommandOpMode
import org.firstinspires.ftc.teamcode.RobotContainer
import org.firstinspires.ftc.teamcode.alonlib.TelemetryLevel
import org.firstinspires.ftc.teamcode.alonlib.commands.asCommand
import org.firstinspires.ftc.teamcode.alonlib.units.Alliance

/**
 * TODO: replace the example trajectory below with an actual DECODE autonomous routine (this just
 * demonstrates that RoadRunner trajectory-following is wired up end to end -- drives 24in forward
 * and back). Tune [org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive.PARAMS] first via
 * [org.firstinspires.ftc.teamcode.roadrunner.tuning.TuningOpModes] or this won't track accurately.
 */
@Autonomous(name = "Blue Main Autonomous", group = "Autonomous")
class BlueMainAutonomous : CommandOpMode() {
    lateinit var hub: LynxModule

    override fun initialize() {
        hub = hardwareMap.get(LynxModule::class.java, "Control Hub").apply {
            bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
        }
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry.addLine("Robot initializing")

        val robotContainer = RobotContainer(
            hardwareMap,
            telemetry,
            gamepad1,
            gamepad2,
            Alliance.Blue,
            TelemetryLevel.Testing
        )
        telemetry.update()

        val drive = robotContainer.driveSubsystem
        schedule(
            drive.actionBuilder()
                .lineToX(24.0)
                .lineToX(0.0)
                .build()
                .asCommand(drive)
        )
    }

    override fun run() {
        hub.clearBulkCache()
        super.run()
        telemetry.update()
    }
}
