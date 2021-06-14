
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Elliot
 */
public class MyThread implements Runnable{
    static Thread h1;
   
    public static void main(String[] args) {
        h1.start();
    }
    @Override
    public void run() {
         Thread ct=Thread.currentThread();
        while(ct==h1){
         try {
             System.out.println("si");
           }catch (JSONException ex) {
             System.out.println("Error:"+ex.getMessage());
           }
         
           try {
            Thread.sleep(250);
           } catch (InterruptedException e) {
           
           } 
         } 
    }
}
