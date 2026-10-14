package org.firstinspires.ftc.teamcode.subsystems.drive

import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.RobotMap.ControlHub.Motors.Drive.BACK_LEFT_MOTOR_ID
import org.firstinspires.ftc.teamcode.RobotMap.ControlHub.Motors.Drive.BACK_RIGHT_MOTOR_ID
import org.firstinspires.ftc.teamcode.RobotMap.ControlHub.Motors.Drive.DRIVE_MOTOR_TYPE
import org.firstinspires.ftc.teamcode.RobotMap.ControlHub.Motors.Drive.FRONT_LEFT_MOTOR_ID
import org.firstinspires.ftc.teamcode.RobotMap.ControlHub.Motors.Drive.FRONT_RIGHT_MOTOR_ID
import org.firstinspires.ftc.teamcode.alonlib.TelemetryLevel
import org.firstinspires.ftc.teamcode.alonlib.commands.SubsystemBase
import org.firstinspires.ftc.teamcode.alonlib.drives.mecanumDrive.HaMecanumDrive
import org.firstinspires.ftc.teamcode.alonlib.hardware.motors.HaMotor
import org.firstinspires.ftc.teamcode.alonlib.math.geometry.Pose2d
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

	// --- hardware declarations ---

	val frontLeftMotor = HaMotor(hardwareMap, FRONT_LEFT_MOTOR_ID, DRIVE_MOTOR_TYPE)
	val frontRightMotor = HaMotor(hardwareMap, FRONT_RIGHT_MOTOR_ID, DRIVE_MOTOR_TYPE)
	val backLeftMotor = HaMotor(hardwareMap, BACK_LEFT_MOTOR_ID, DRIVE_MOTOR_TYPE)
	val backRightMotor = HaMotor(hardwareMap, BACK_RIGHT_MOTOR_ID, DRIVE_MOTOR_TYPE)

	// --- functional properties ---
	val localizer = LocalizerSubsystem(hardwareMap, telemetry, telemetryLevel)
	val mecanumDrive = HaMecanumDrive(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor)

	val roadrunnerMecanumDrive = MecanumDrive(hardwareMap,localizer.pinPoint,localizer.latestBotPose2d.toRoadRunner())

	// --- operation functions ---

	fun resetPose(pose: Pose2d) {
		localizer.setBotPose2d(pose)
	}

	fun resetImu() {
		localizer.
	}

	/** Builds a RoadRunner trajectory starting from the current pose estimate. */
	fun actionBuilder(): TrajectoryActionBuilder = roadrunnerMecanumDrive.actionBuilder(mecanumDrive.localizer.pose)

	// --- Telemetry ---
	fun updateTelemetry() {
		when (telemetryLevel) {
			TelemetryLevel.Competition -> {}
			TelemetryLevel.Testing     -> {
				telemetry.addLine("--- Drive subsystem ---")
				telemetry.addData("Running Command", super.currentCommand())
				val pose = roadrunnerMecanumDrive.localizer.pose
				telemetry.addData("Robot pose (RoadRunner)", "x: ${roadrunnerMecanumDrive.localizer.pose.position.x}, y: ${roadrunnerMecanumDrive.localizer.pose.position.y}, heading (deg): ${roadrunnerMecanumDrive.localizer.pose.heading}")
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
