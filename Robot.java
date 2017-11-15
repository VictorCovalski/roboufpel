//Na pasta do arquivo:
//nxjc Robot.java

//nxjlink -o Robot.nxj Robot
//nxjupload [-r] Robot.nxj
//ou
//nxj -o Robot.nxj Robot


// Objetivo: Andar na reta e contar os passos
//esteira tem 31 centimetros de perímetro
// branco = 60mm
// preto = 1 cm
// 18 pretos e 18 brancos
// esteira tem 36 estados
/*
Aparentemente o sensor ultrasom não pode ficar muito proximo do chao,
na configuracao atual ele esta a 4 cm do chão e esta detectando, no máximo,
95 cm (+-). Mas, se levantarmos ele em 3 cm (sensor a 7cm do chao) ele ganha + 60 cm,
ficando com algo em torno de 150 cm
*/

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;
import lejos.nxt.*;
import lejos.robotics.objectdetection.*;
import lejos.robotics.*;

public class Robot
{
	private Color last_color;
	private int distanceTravelled;
	private Motor left;
	private Motor right;
	private ColorSensor cs;

	public Robot(Motor l, Motor r ,ColorSensor cs, int speed)
	{
		this.distanceTravelled = 0;
		this.degreesTurned = 0;
		this.left = l;
		this.right= r;
		this.cs = cs;
		setSpeed(speed);
	}

	public void setSpeed(int speed) //360 = 1 rpm
	{
		this.left.setSpeed(speed);
		this.right.setSpeed(speed);
	}
	public void wait()
	{
		Button.waitForAnyPress();
	}
	private int getCurColorBW()
	{
		last_color = this.cs.getColor();
		//int THRESHOLD = 128;
		int THRESHOLD = 100;
		if( (last.getRed() >= THRESHOLD) || (last.getBlue() >= THRESHOLD) || (last.getGreen() >= THRESHOLD))
		{
			last_color = Color.WHITE;
			return Color.WHITE;
		}
		else
		{
			last_color = Color.BLACK;
			return Color.BLACK;
		}
	}
	private int invertColor(int color)
	{
		if(color == Color.WHITE)
		{
			return Color.BLACK;
		}
		return Color.WHITE;
	}
	public void move(int degrees) //positive values will move robot forward
	{
		this.left.rotate(graus,true);
		this.right.rotate(graus,true);
	}
	private boolean isMoving()
	{
		return (this.left.isMoving() || this.right.isMoving())
	}
	public void stop()
	{
		this.left.stop();
		this.right.stop();
	}
	private void calibrate()
	{
		movefw(50);
		while(isMoving())
		{
			if(getCurcolorBW() == Color.BLACK)
			{
				stop();
				break;
			}
		}
	}
	private moveConveyor(double distance)
	{
		calibrate();
		int transitions = 0.861 * distance;
		for(int i=0;i<transitions;)
		{
			movefw(360);
			while(isMoving())
			{
				this.last_color = getColorBW(cs);
				if(this.last_color == invertColor(this.last_color))
				{
					i++;

					stop()
					//System.out.println("Transicao:" + i);
					break;
				}
			}
		}
	}


	public static void main (String[] args)
	{
		Robot r = new Robot(Motor.A,Motor.C,ColorSensor(SensorPort.S4, SensorConstants.TYPE_LIGHT_ACTIVE);

		r.setSpeed(10);



		Motor.A.stop();
		Motor.C.stop();
		Button.waitForAnyPress();
		/*
		for(int i=0;i<2;i++)
		{
			while(id_coratual != nextColor)
			{
				movefw(50);
				id_coratual = getColorBW(cs);
				//System.out.println("Cor observada: " + id_coratual);
			}
			nextColor = invertColor(nextColor);
			System.out.println("transição:  " + i + nextColor);
		}
		System.out.println("final");
		Button.waitForAnyPress();
		*/
	}
}
