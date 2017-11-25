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
//import Math.*;

public class Robot
{
	private int corAtual;
	private int speed;
	
	private double prevUSDistance;
	private int MAX_DISTANCE = 150; // In centimeters [limite do sensor é 255 com margem de +-3cm nas especificações]
	private int PERIOD = 200; 		// In milliseconds
	
	//robot sensors
	private FeatureDetector fd; 
	private NXTRegulatedMotor left;
	private NXTRegulatedMotor right;
	private UltrasonicSensor us;
	private ColorSensor cs;
	
	//kalman related variables
	private double distConveyor; //distance traversed according to conveyor sensoring method
	private double distUS; // distance traversed according to ultrasoonic sensoring method
	//private double gKalman = 0.7360626233;
	private double gKalman = 0.6162647384;
	
	public Robot(NXTRegulatedMotor l, NXTRegulatedMotor r, UltrasonicSensor s, ColorSensor cs, int speed)
	{
		this.left = l;
		this.right= r;
		this.cs = cs;
		this.us = s;
		this.fd = new RangeFeatureDetector(us, MAX_DISTANCE, PERIOD);
		setSpeed(speed);
	}
	public double getDistance()
	{
		Feature result = this.fd.scan();
	
		// Importante testar se null para que n ocorra exceção
		if(result != null)
		{
			return result.getRangeReading().getRange();
		}
		return this.prevUSDistance;
		 
	}
	
	private void moveKalman(double distance)
	{
		
		//reset variables
		this.distUS = 0;
		this.distConveyor = 0;
		this.prevUSDistance = getDistance();
		double usDif = this.prevUSDistance;
		
		double x_estimated = 0;
		int corinicial = this.corAtual;
		
		do
		{
			moveDegrees(-360);
			this.prevUSDistance = getDistance();
			this.distUS = this.prevUSDistance - usDif;
			
			if(corinicial != getCurColorBW())
			{
				corinicial = invertColor(corinicial);
				this.distConveyor += 1.7;
			}
			
			LCD.clear();
			x_estimated = (this.gKalman * this.distUS) + ((1 - this.gKalman) * this.distConveyor);
			System.out.println(this.distUS + "\n" + this.distConveyor + "\n" + x_estimated);
			
			if(x_estimated >= distance)
			{
				stop();
				break;
			}
			
			
		}
		while(isMoving());
		
		stop();
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
	public void moveDegrees(int degrees)
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
	public void calibrate()
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
		
		stop();
		waitButton();
	}
	
	public static void main (String[] args)
	{
		Robot r = new Robot(Motor.A,Motor.C, new UltrasonicSensor(SensorPort.S1), new ColorSensor(SensorPort.S4, SensorConstants.TYPE_LIGHT_ACTIVE),40);

		r.calibrate();
		System.out.println("Aperte para iniciar.");
		r.waitButton();

		System.out.println("Anda 100 cm");
		r.moveKalman(100.0);
		r.waitButton();
	}
}
