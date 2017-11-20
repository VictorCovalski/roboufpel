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

//esteira B
// 18 estados
// 1,72 cm / estado
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;
import lejos.nxt.*;
import lejos.robotics.objectdetection.*;
import lejos.robotics.*;
public class Robot
{
	private int corAtual;
	private int speed;
	private int distanceTravelled;
	private NXTRegulatedMotor left;
	private NXTRegulatedMotor right;
	private ColorSensor cs;

	public Robot(NXTRegulatedMotor l, NXTRegulatedMotor r ,ColorSensor cs, int speed)
	{
		this.left = l;
		this.right= r;
		this.cs = cs;		
		setSpeed(speed);
	}

	public void setSpeed(int speed) //360 = 1 rpm
	{
		this.speed = speed;
		this.left.setSpeed(speed);
		this.right.setSpeed(speed);
	}
	public void waitButton()
	{
		Button.waitForAnyPress();
	}
	private int getCurColorBW()
	{
		Color cor = this.cs.getColor();
		//int THRESHOLD = 128;
		int THRESHOLD = 100;
		if( (cor.getRed() >= THRESHOLD) || (cor.getBlue() >= THRESHOLD) || (cor.getGreen() >= THRESHOLD))
		{
			this.corAtual = Color.WHITE;
			return Color.WHITE;
		}
		else
		{
			this.corAtual = Color.BLACK;
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
	public void moveDegrees(int degrees) //positive values will move robot forward
	{
		this.left.rotate(degrees,true);
		this.right.rotate(degrees,true);
	}
	private boolean isMoving()
	{
		return (this.left.isMoving());
	}
	public void stop()
	{
		this.left.stop();
		this.right.stop();
	}
	private void calibrate()
	{
		int speed = this.speed;
		setSpeed(10);
		moveDegrees(150);
		int corinicial = getCurColorBW();
		while(this.left.isMoving())
		{
			if(getCurColorBW() != corinicial)
			{
				stop();
				setSpeed(speed);
				break;
			}
		}
	}
	
	private void moveOneColor()
	{
		int corInicial = this.corAtual;
		moveDegrees(90);
		while(this.left.isMoving())
		{
			if(getCurColorBW() != corInicial)
			{
				stop();
				return;
			}
		}
	}
	private void moveConveyor(double distance)
	{
		calibrate();
		waitButton();
		
		int corinicial = this.corAtual;
		//Esteira no início do branco
		while(distance > 1)
		{
			moveDegrees(720);
			while(isMoving())
			{
				if(corinicial != getCurColorBW())
				{
					corinicial = invertColor(corinicial);
					distance-=1.7;
				}
			}
		}

		stop();
		
		if(distance > 0.85)
		{
			moveOneColor();
		}
		/*switch(this.corAtual)
		{
			case Color.BLACK:
			{
				if(distance > 0.5)
				{
					moveOneColor();
				}
			}
			case Color.WHITE:
			{
				if(distance > 0.3)
				{
					moveOneColor();
				}
			}
		}*/
		
		stop();
		waitButton();
	} 
	/*public void moveConveyor2(double distance)
	{
			double statesDouble = distance/1.72;
			int noStates = math.Round(statesDouble);
			System.out.println(noStates + "estados");
			calibrate();
			waitButton();
			int corInicial = this.corAtual;
			
			for(int i=0;i<noStates;)
			{
				moveDegrees(200);
				while(this.left.isMoving())
				{
						if(corInicial != getColorBW())
						{
							i++;
							corInicial = invertColor(corInicial);
						}
				}
					
			}
	}*/
	//1 - 34.5 -> 138
	//2 - 29   -> 133
	//3 - 28.5
	//4 - 29 >> 133
	//5 - 29 -> 130.1
	//6 - 29 -> 132.3
	//7 - 28.6-> 132.5
	//8 - 28.6 -> 132.5
	//9 - 28	-> 131.7
	//10 - 29.7 -> 132.4
	
	//11 - 36.8 -> 140.5
	//12 - 28 -> 131.5
	//13 - 27.9 -> 131.7
	//14 - 27 -> 130.8
	//15 - 26.2 -> 130
	//16 - 27.9 -> 132.9
	//17 - 28.9 -> 131.8
	//18 - 28.8 -> 131.7
	//19 - 32.3 -> 136.4
	//20 - 27.6 -> 132.8
	//21 - 28.5 -> 131.3
	//22 - 24.4 -> 127.5
	//23 - 29.1 -> 131.6
	//24 - 26.9 -> 129.6
	//25 - 33	-> 136.9
	//26 - 28.5 -> 132
	//27 - 28.1 -> 131.9
	//28 - 26   -> 129.5
	//29 - 26.6 -> 
	public static void main (String[] args)
	{
		Robot r = new Robot(Motor.A,Motor.C,new ColorSensor(SensorPort.S4, SensorConstants.TYPE_LIGHT_ACTIVE),60);

		System.out.println("Anda 25 cm");
		r.moveConveyor(100.0);
		//System.out.println("Anda 25 cm");
		//r.moveConveyor(25.0);
	}
}
