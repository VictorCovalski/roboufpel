//Na pasta do arquivo:
//nxjc DistMove.java

//nxjlink -o DistMove.nxj DistMove 
//nxjupload [-r] DistMove.nxj
//ou 
//nxj -o DistMove.nxj DistMove


import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;
import lejos.nxt.*;
import lejos.robotics.objectdetection.*;

public class DistMove
{

	public static void main (String[] args) 
	{

		/* Configs para acessar sensor ultrasonic */
		int MAX_DISTANCE = 250; // In centimeters [limite do sensor é 255 com margem de +-3cm nas especificações]
		int PERIOD = 50; // In milliseconds
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		FeatureDetector fd = new RangeFeatureDetector(us, MAX_DISTANCE, PERIOD);
		Feature result;
		int i=0;
		float distancia = 0;
		float distanciaant = 0;
		float distobjetivo = 100;
		/*---*/
		
		System.out.println("Enter pra começar.");
		Button.waitForAnyPress();
		
		while(true)
		{
			/* Escaneia a distancia */
			result = fd.scan();
			
			// Importante testar se null para que n ocorra exceção
			if(result != null)
			{
				// Se entrou no if é uma distancia valida
				distancia = result.getRangeReading().getRange();
				distanciaant = distancia;
				
				System.out.println("Range: " + distancia);

				// Se a distancia é válida e, maior que x, então anda y
				if (distancia< 100.0)
				{
					// Motores movimentando a 1/4 ciclo
					// true para que ambos motores movimentem junto

					Motor.A.rotate(-45,true);
					Motor.C.rotate(-45,true);
					i++;
				}
				else
				{
					System.out.println(i + " Chegou: " + distancia);
					break;
				}

			}
			else
			{
				System.out.println(i + " Range: NULL");
				if (distanciaant < 100.0)
				{
					Motor.A.rotate(-15,true);
					Motor.C.rotate(-15,true);
					i++;
				}
				
			}
			//Delay.msDelay(100);
		}

  			

  			/* ---- Conhecimento extra:

           LCD.drawString("Aperta o botão pro bixo andar.", 0, 0);
           Button.waitForAnyPress();

           // Enquanto
           while(Button.readButtons()>0);


           LCD.clear();
           Motor.A.rotateTo(360, true);
           
           while(Motor.A.isMoving())
           {
             Delay.msDelay(200);
             LCD.drawInt(Motor.A.getTachoCount(),0,0);
              if(Button.readButtons()>0) Motor.A.stop();
           }
           LCD.drawInt(Motor.A.getTachoCount(),0,1);
 
           Button.waitForAnyPress();     
           */
	}	

}

// Objetivo: Andar na reta e contar os passos

/* 
Aparentemente o sensor ultrasom não pode ficar muito proximo do chao,
na configuracao atual ele esta a 4 cm do chão e esta detectando, no máximo,
95 cm (+-). Mas, se levantarmos ele em 3 cm (sensor a 7cm do chao) ele ganha + 60 cm,
ficando com algo em torno de 150 cm
*/
