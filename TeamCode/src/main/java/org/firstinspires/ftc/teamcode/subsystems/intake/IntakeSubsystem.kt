package org.firstinspires.ftc.teamcode.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.RobotMap.ExpansionHub.Motors.Intake.INTAKE_MOTOR_ID
import org.firstinspires.ftc.teamcode.RobotMap.ExpansionHub.Motors.Intake.INTAKE_MOTOR_TYPE
import org.firstinspires.ftc.teamcode.alonlib.TelemetryLevel
import org.firstinspires.ftc.teamcode.alonlib.commands.SubsystemBase
import org.firstinspires.ftc.teamcode.alonlib.hardware.motors.HaMotor
import org.firstinspires.ftc.teamcode.alonlib.hardware.motors.Motor
import org.firstinspires.ftc.teamcode.alonlib.units.Percentage
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeConstants.INTAKE_ACTIVE_VOLTAGE_THRESHOLD
import kotlin.math.absoluteValue

class IntakeSubsystem(hardwareMap: HardwareMap, var telemetry: Telemetry, val telemetryLevel: TelemetryLevel) : SubsystemBase() {

	// --- hardware declaration ---
	val intakeMotor = HaMotor(hardwareMap, INTAKE_MOTOR_ID, INTAKE_MOTOR_TYPE).apply {
		zeroPowerBehavior = Motor.ZeroPowerBehavior.BRAKE
		runningDirection = HaMotor.Direction.FORWARD
		runMode = Motor.RunMode.RAW_POWER
	}

	// --- state getters ---

	val isActive get() = intakeMotor.voltage.absoluteValue >= INTAKE_ACTIVE_VOLTAGE_THRESHOLD

	// --- motors control ---

	fun setMotorPower(power: Percentage) {
		intakeMotor.percentOutput = power
	}

	fun stopMotor() {
		intakeMotor.stop()
	}

	// --- telemetry ---
	fun updateTelemetry() {
		when (telemetryLevel) {
			TelemetryLevel.Competition -> {}
			TelemetryLevel.Testing     -> {
				telemetry.addLine("--- intake subsystem ---")
				telemetry.addData("Running Command", super.currentCommand())
				telemetry.addData("is running: ", isActive)
			}
		}
	}

	// --- periodic function ---
	override fun periodic() {
		updateTelemetry()
	}
}
