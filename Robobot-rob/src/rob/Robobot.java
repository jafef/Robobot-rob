package rob;

import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.*;

public class Robobot extends rob {
	boolean move; // É definida como true quando setAhead é chamada e vice-versa
	boolean pard; // É verdade quando robô está perto da parede.

	public void run() {

		// Cores do robo
		setBodyColor(Color.black);
		setGunColor(Color.black);
		setRadarColor(Color.orange);
		setBulletColor(Color.orange);
		setScanColor(Color.orange);

		// Criando movimentos indemendentes.
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		// Verifique se o robô está perto da parede.
		if (getX() <= 70 || getY() <= 70
				|| getBattleFieldWidth() - getX() <= 70
				|| getBattleFieldHeight() - getY() <= 70) {
			this.pard = true;
		} else {
			this.pard = false;
		}

		etTurnRadarRight(360); // scannear até encontrar seu primeiro inimigo
		this.move = true; // chamamos setAhead, então move é verdade

		while (true) {
			
			if (getX() > 50 && getY() > 50
					&& getBattleFieldWidth() - getX() > 50
					&& getBattleFieldHeight() - getY() > 50
					&& this.pard == true) {
				this.pard = false;
			}
			if (getX() <= 50 || getY() <= 50
					|| getBattleFieldWidth() - getX() <= 50
					|| getBattleFieldHeight() - getY() <= 50) {
				if (this.pard == false) {
					reverseDirection();
					pard = true;
				}
			}

			//Procurando o inimigo
			if (getRadarTurnRemaining() == 0.0) {
				setTurnRadarRight(360);
			}

			execute(); // executar todas as ações set.
		}
	}

	public void onHitWall(HitWallEvent e) { //chocar com a parede mudar direção
		reverseDirection();
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		
		if (e.getDistance() < 50 && getEnergy() > 50) {
			fire(3);
		} 
		else {
			fire(1);
		}
		// se o radar não estiver girando, gera evento de giro (scanner)
		if (bearingFromGun == 0) {
			scan();
		}
	}

	// em contato com o robo, se tenha sido por nossa conta, inverte a direção
	public void onHitRobot(HitRobotEvent e) {
		if (e.isMyFault()) {
			reverseDirection();
		}
	}


	// mudar de frente para trás e vice-versa
	public void reverseDirection() {
		if (this.move) {
			setBack(10000);
			this.move = false;
		} else {
			setAhead(10000);
			this.move = true;
		}
	}
}