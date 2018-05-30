package frc.team4096.robot.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.SpeedControllerGroup
import edu.wpi.first.wpilibj.VictorSP
import edu.wpi.first.wpilibj.drive.DifferentialDrive

import frc.team4096.robot.util.DriveConsts
import frc.team4096.robot.util.MiscIDs

object DriveSubsystem: Subsystem() {
	// Hardware
	var left1 = VictorSP(DriveConsts.PWM_L1)
	var left2 = VictorSP(DriveConsts.PWM_L2)
	var leftGroup = SpeedControllerGroup(left1, left2)

	var right1 = VictorSP(DriveConsts.PWM_R1)
	var right2 = VictorSP(DriveConsts.PWM_R2)
	var rightGroup = SpeedControllerGroup(right1, right2)

	var diffDrive = DifferentialDrive(leftGroup, rightGroup)

	var shifterSolenoid = DoubleSolenoid(
			MiscIDs.CAN_PCM,
			DriveConsts.PCM_SHIFTER_1,
			DriveConsts.PCM_SHIFTER_2
	)

	// Hardware states
	var gear = updateGearState()
	var isQuickTurn = false

	// Software states
	var state = DriveState.OPEN_LOOP
	var mode = DriveMode.CURVE
	var wasCorrecting = false

	init {
		// Set high gear
		setGearState(GearState.HIGH)
	}

	override fun initDefaultCommand() {}

	fun setGearState(state: GearState) {
		shifterSolenoid.set(state.solenoidState)
	}

	// Used to fix internal gear state in case someone manually shifted
	private fun updateGearState() {
		gear = (GearState)shifterSolenoid.get()
	}
}

enum class DriveMode {
	TANK,
	ARCADE,
	CURVE
}

enum class DriveState {
	OPEN_LOOP,
	POSITION_CONTROL,
	PATH_FOLLOWING
}

enum class GearState(val solenoidState: DoubleSolenoid.Value) {
	HIGH(DoubleSolenoid.Value.kForward),
	LOW(DoubleSolenoid.Value.kReverse),
	NEUTRAL(DoubleSolenoid.Value.kOff)
}
