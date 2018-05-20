/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.net.ssl.SSLServerSocket;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

/**
 *
 * @author GIZEM
 */
public class Server {
    
    static ArrayList<makineOz> machines;
    static ArrayList<IsOz> isler;
    public static void main(String[] args) throws IOException {
        
        ServerSocket listener = new ServerSocket(9090);
        
        machines = new ArrayList<makineOz>();
        isler = new ArrayList<IsOz>();
        
         try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }
    
    
    
    
    private static class Handler extends Thread{

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        ArrayList text = new ArrayList();

        public Handler(Socket socket) {
            this.socket = socket;
        }

        
        
        @Override
        public void run() {
            
            if(socket== null)
                System.out.println("gizemm");
                        try {
                
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                
                makineOz m = new makineOz(in,out);
                IsOz iso = null;
                
            System.out.println("asdasd");
                        while (true) {
                            // burada isler arraylist olacak
                            String gelen = in.readLine();
                                switch(gelen.substring(0,3)){
                                    case "mac":    //yeni makine bağlandı
                                        machines.add(m);
                                        break;
                                    case "nam":   //makina ismi
                                        m.name = gelen.substring(4, gelen.length());
                                        break;
                                        
                                    case "id ":  //makina id
                                        m.id = gelen.substring(4, gelen.length());
                                        break;
                                
                                    case "typ": //makina türü
                                        m.type = gelen.substring(4, gelen.length());
                                        break;
                                    
                                    case "spe":  //makina hızı
                                        m.speed = gelen.substring(4, gelen.length());
                                        break;
                                        
                                    case "sta": //makina durumu
                                        m.state = gelen.substring(4, gelen.length());
                                        break;
                                        
                                    case "log":        //kullanıcı adı ve şifre
                                        String kadi = gelen.substring(4, gelen.length());
                                         System.out.println(kadi);
                                        if(kadi.equals("gizem123456") || kadi.equals("sait2d4dt6"))
                                            out.println("success");
                                        else
                                            out.println("fail");
                                        break;
                                    
                                    case "cid": //client işin id
                                        iso = new IsOz(in, out);
                                        isler.add(iso);
                                        iso.id = gelen.substring(4, gelen.length());
                                        break;
                                    
                                    case "cle": // client iş uzunluğu
                                        iso.lenght = gelen.substring(4, gelen.length());
                                        break;
                                    
                                    case "cty": // client iş türü
                                        iso.type = gelen.substring(4, gelen.length());
                                        isAta();
                                        break;
                                    
                                    case "dne": // makina verilen işi bitirdiyse
                                        m.works.add(gelen.substring(4, gelen.length()));
                                        isAta();
                                        break;
                                    
                                    case "lst": // client serverdan makinaların isimlerini ister
                                        for(int i=0;i<machines.size();i++)
                                            out.println("lst " + machines.get(i).name + " " + "Tür " + machines.get(i).type);
                                        out.println("ldn");
                                         break;
                                    case "bck": // bekleyen işleri clientIs e gönderme
                                        out.println("iss"); //is gönderme başladı
                                        for(int i=0;i<isler.size();i++) //işler tek tek gönderiliyor
                                            out.println("isi " + isler.get(i).id);
                                        out.println("isf"); //iş gönderme bitti
                                        break;
                                    case "mno":
                                        int machineNo = Integer.parseInt(gelen.substring(4, gelen.length()));
                                        out.println("mws");
                                        out.println("mww " + machines.get(machineNo).state);
                                        for(int i=0;i<machines.get(machineNo).works.size();i++)
                                            out.println("mww " + machines.get(machineNo).works.get(i));
                                        out.println("mwf");
                                        break;
                                }
                              
                                
                        }
                        
                            
                            
                
            } catch (Exception e) {
                System.out.println(e);
            }
  
        }
       
    private static void log(String message, String text1){
            System.out.println(text1);
            System.out.println(message);
    }
    private void isAta() throws InterruptedException{
       
        for(int i=0;i<isler.size();i++)
            for(int j=0;j<machines.size();j++){
                 System.out.println(machines.get(j).state);
                if("empty".equals(machines.get(j).state))
                    if(isler.get(i).type == null ? machines.get(j).type == null : isler.get(i).type.equals(machines.get(j).type)){
                        
                        machines.get(j).out.println("idd " + isler.get(i).id);
                        
                        machines.get(j).out.println("wrk " + isler.get(i).lenght);
                        isler.remove(i);
                        System.out.println("ddddddddddd");
                        break;
                    }
            }
    }
    
    }
    
    

}
//innerclass
  class makineOz {
     public String name;
     public String id;
     public String type;
     public String speed;
     public String state;
     public ArrayList<String> works; 
     BufferedReader in;
     PrintWriter out;

    makineOz(BufferedReader in, PrintWriter out) {
        works = new ArrayList<String>();
        this.in = in;
        this.out = out;
        state = "empty";
    }
}

class IsOz {
    
    public String lenght;
    public String id;
    public String type;
    BufferedReader in;
    PrintWriter out;
     
    IsOz(BufferedReader in, PrintWriter out){
    this.in = in;
    this.out = out;
    }
    
}
 



