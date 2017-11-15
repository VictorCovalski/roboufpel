//Na pasta do arquivo:
//nxjc Light.java

//nxjlink -o Light.nxj Light
//nxjupload [-r] Light.nxj
//ou
//nxj -o Light.nxj Light

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;
import lejos.nxt.*;
import lejos.robotics.objectdetection.*;
import lejos.robotics.*;

public class Light
{
	static public int getColorBW(ColorSensor cs)
	{
		Color c = cs.getColor();
		//int THRESHOLD = 128;
		int THRESHOLD = 100;
		if( (c.getRed() >= THRESHOLD) || (c.getBlue() >= THRESHOLD) || (c.getGreen() >= THRESHOLD))
		{
			return Color.WHITE;
		}
		else
		{
			return Color.BLACK;
		}
	}
	static int invertColor(int color)
	{
		if(color == Color.WHITE)
		{
			return Color.BLACK;
		}
		return Color.WHITE;
	}
	/*static public int getColorBW(ColorSensor cs)
	{
		return cs.getColor().getColor();
	}*/
	static public void movefw(int graus)
	{
		Motor.A.rotate(graus,true);
		Motor.C.rotate(graus,true);
	}
	public static void main (String[] args) 
	{
		Motor.A.setSpeed(10);
		Motor.C.setSpeed(10);
		
		int graus = 0;
		int dist = -10;
		int nextColor;
		ColorSensor cs = new ColorSensor(SensorPort.S4, SensorConstants.TYPE_LIGHT_ACTIVE);
		cs.setFloodlight(false);
		//cs.setFloodlight(Color.WHITE);
		Color cor = new Color(0,0,0);
		
		System.out.println("Enter pra começar.");
		Button.waitForAnyPress();
		int id_coratual = getColorBW(cs);
		
		if(id_coratual == Color.WHITE || id_coratual == Color.YELLOW)
		{
			nextColor = Color.BLACK;
		}
		else
		{
			nextColor = Color.WHITE;
		}
		
		System.out.println("Cor calibracao:" + id_coratual);
		//Button.waitForAnyPress();	
		
		
		movefw(50);
		while(Motor.A.isMoving())
		{
			if(id_coratual == Color.BLACK)
			{
				Motor.A.stop();
				Motor.C.stop();
				break;
			}
		}
		nextColor = invertColor(nextColor);
		System.out.println("transição:  " + nextColor);
		
		System.out.println("Inicio cor:" + id_coratual);
		Button.waitForAnyPress();

		for(int i=0;i<125;)
		{
			movefw(360);
			while(Motor.A.isMoving())
			{
				id_coratual = getColorBW(cs);
				if(id_coratual == nextColor)
				{
					nextColor = invertColor(nextColor);
					i++;
					System.out.println("Transicao:" + i);
					break;
				}
			}
		}
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
