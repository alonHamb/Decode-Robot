package org.firstinspires.ftc.teamcode.subsystems.drive

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.hardware.HardwareMap
import com.seattlesolvers.solverslib.command.SubsystemBase
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.alonlib.TelemetryLevel
import org.firstinspires.ftc.teamcode.alonlib.units.toRoadRunner

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.subsystems.vision.LocalizerSubsystem

/**
 * Drives the robot via RoadRunner's [MecanumDrive] (see [mecanumDrive]), which owns the
 * authoritative pose estimate used for field-centric driving and trajectory following.
 *
 * [LocalizerSubsystem] (via [localizer]) reads the same physical Pinpoint directly for its
 * goal-distance/vision telemetry -- that reading is NOT offset-corrected the way RoadRunner's
 * pose is, so after [resetImu]/[resetPose] the two can disagree by whatever the correction was.
 * That's fine for approximate telemetry; anything trajectory-critical should read
 * [mecanumDrive]'s pose, not [localizer]'s.
 */
@Config
class DriveSubsystem(
    val hardwareMap: HardwareMap,
    val telemetry: Telemetry,
    val telemetryLevel: TelemetryLevel
) : SubsystemBase() {
    // --- functional properties ---
    val localizer = LocalizerSubsystem(hardwareMap, telemetry, telemetryLevel)
    val mecanumDrive = MecanumDrive(hardwareMap, localizer.pinPoint, localizer.latestBotPose2d.toRoadRunner())

    // --- operation functions ---
    fun fieldCentricDrive(xSpeed: Double, ySpeed: Double, turnSpeed: Double) {
        val heading = mecanumDrive.localizer.pose.heading
        val robotRelative = Rotation2d.fromDouble(-heading.log()).times(Vector2d(xSpeed, ySpeed))
        mecanumDrive.setDrivePowers(PoseVelocity2d(robotRelative, turnSpeed))
        mecanumDrive.updatePoseEstimate()
    }

    fun robotCentricDrive(xSpeed: Double, ySpeed: Double, turnSpeed: Double) {
        mecanumDrive.setDrivePowers(PoseVelocity2d(Vector2d(xSpeed, ySpeed), turnSpeed))
        mecanumDrive.updatePoseEstimate()
    }

    /** Resets RoadRunner's pose estimate to [pose] (does not touch [localizer]'s raw reading). */
    fun resetPose(pose: Pose2d) {
        mecanumDrive.localizer.pose = pose
    }

    fun resetImu() {
        val current = mecanumDrive.localizer.pose
        resetPose(Pose2d(current.position.x, current.position.y, 0.0))
    }

    /** Builds a RoadRunner trajectory starting from the current pose estimate. */
    fun actionBuilder(): TrajectoryActionBuilder = mecanumDrive.actionBuilder(mecanumDrive.localizer.pose)

    // --- Telemetry ---
    fun updateTelemetry() {
        when (telemetryLevel) {
            TelemetryLevel.Competition -> {}
            TelemetryLevel.Testing     -> {
                telemetry.addLine("--- Drive subsystem ---")
                telemetry.addData("Running Command", super.currentCommand)
                val pose = mecanumDrive.localizer.pose
                telemetry.addData(
                    "Robot pose (RoadRunner)",
                    "x: ${pose.position.x}, y: ${pose.position.y}, heading (deg): ${Math.toDegrees(pose.heading.toDouble())}"
                )
                telemetry.addData("distance to red goal", localizer.distanceToRedTarget)
                telemetry.addData("distance to blue goal", localizer.distanceToBlueTarget)
            }
        }
    }

    // --- periodic function ---
    override fun periodic() {
        updateTelemetry()
    }
}
