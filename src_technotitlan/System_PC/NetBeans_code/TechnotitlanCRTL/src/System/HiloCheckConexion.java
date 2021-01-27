/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;
import com.panamahitek.ArduinoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import time.*;

/**
 *
 * @author Ing Lalux
 */
public class HiloCheckConexion extends Thread{
    int id;
    String timeLastPresencia;
    boolean vivo=true;
		
    HiloCheckConexion(int id){
	this.id=id;
    }
			
    @Override
    public void run(){
        //Checar la presencia del controlador cada 1mins
        while(vivo){
            try {
                //Pedir que Anuncie su presencia
                Menu.Arduino.sendData("OK\n");
            } catch (ArduinoException ex) {
                Logger.getLogger(HiloCheckConexion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SerialPortException ex) {
                Logger.getLogger(HiloCheckConexion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            //Comprobar su presencia
            CheckPresenciaControlador();
            
            
            //Espera para pedir Anuncie su presencia otra vez
            try {
                HiloCheckConexion.sleep((long) Menu.TIME_REPEAT_SEND_OK_MENSAJE);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloCheckConexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    //Actualizar la ultima hora en la que se detecto presencia del Controlaor Centrall
    public void ActualizarLastPresencia(){
        timeLastPresencia = time.AlgoritmsT.getTimeActual();
    }
    

    //Comprobar el tiempo que ha pasado desde que se detecto la ultima presencia del Controlador Central
    public void CheckPresenciaControlador(){
        System.out.println("HiloCheckConexion: Verificando la presencia del Controlador Central");
        System.out.println("LAST PRECENCIA:"+timeLastPresencia+"     ACTUAL:"+time.AlgoritmsT.getTimeActual());
        double diferencia = time.AlgoritmsT.calculeTime(time.AlgoritmsT.getTimeActual(),"-", timeLastPresencia);
        
        //Analizar diferencia, si es mayor a X timepo, entonces el controlador central perdio conexion//
        if(diferencia>Menu.MAX_TIME_WAIT_OK_REPLY){
            //Entonces pedir la Resurekcion del Sistema//
            System.out.println("HiloCheckConexion: Presencia No detectada, realizando la resurekcion del sistema...");
            Menu.addBitacora("ERROR","Presencia Controlador Central No detectada, realizando Resurekcion");
            main.m.Resurekcion();
        }else{
            System.out.println("HiloCheckConexion: Presencia Validada, Diferencia:"+diferencia+", continuando con el sitema");
        }
    }
    
    
    //Metodo para indicar que el Hilo debe detenerse
    public synchronized void Detener(){
        vivo=false;
    }
}
