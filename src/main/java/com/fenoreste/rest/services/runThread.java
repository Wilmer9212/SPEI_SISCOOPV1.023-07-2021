package com.fenoreste.rest.services;

import java.io.File;


 public class runThread implements Runnable {
    String ruta;
     
    runThread(String rutaPDF){
        ruta=rutaPDF;
    }
    //Punto de entrada del hilo
    //Los hilos comienzan a ejecutarse aquí
    public void run(){     
        File file=new File(ruta);
        try {
            for (int contar=0; contar<20; contar++){
                Thread.sleep(1000);
                if(contar==19){
                    file.delete();
                }
            }
        }catch (InterruptedException exc){
            System.out.println("Interrumpido.");
        }
        
    }
}