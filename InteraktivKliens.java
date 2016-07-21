import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InteraktivKliens {
    
    public static volatile boolean serverExit = false;

	
    InteraktivKliens(String host, int port) {
        try {
            Socket client = new Socket(host, port);
            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
            Scanner sc = new Scanner(client.getInputStream());
            Scanner scIn = new Scanner(System.in);
			System.out.println("Neve: ");
			String nev = scIn.nextLine();
			pw.println(nev);
			
        	(new Thread(new Jatek(sc, pw,scIn, nev))).start();            
            
        } catch (Exception e) {
            System.err.println("Nem sikerult kapcsolodni a szerverhez.");
            e.printStackTrace();
            return;
        }
    }
    
    public static void main(String[] args) {
        new InteraktivKliens("localhost", 32123);
    }
	
	
	public boolean isAWord(String msg){
		return msg.matches("^[a-zA-Z]+$");
	}
	
	
	class Jatek implements Runnable {
		PrintWriter pw;
		Scanner sc;
		Scanner scIn;
		String nev;
		String start = "start";;
		String nyert = "nyert";;
		String exit = "exit";
		
		Jatek(Scanner sc, PrintWriter pw, Scanner scIn, String nev) {
			this.pw = pw;
			this.sc = sc;
			this.scIn = scIn;
			this.nev = nev;
		}
    
		@Override
		public void run() {
			try{
				
				String message = "";
				String prev = "";
				boolean jatekVege = false;
				while( !message.equals(exit)  && !prev.equals(nyert)) {
					prev = sc.nextLine();
					System.out.println(prev);
					message = "";
						if(!prev.equals(nyert)){		
							if(prev.equals(start)){
								System.out.println(nev+": start");
								String tilt = "";
								while(!tilt.equals("ok") && !message.equals(exit)){
									message = scIn.nextLine();
									pw.println(message);
									tilt = sc.nextLine();
									System.out.println(tilt);
									if(!tilt.equals("ok")){
										System.out.println("Probald ujra!");
									}
								}
							}else{
								String tilt = "";
								String lc = prev.substring(prev.length() - 1);
								char lastChar = lc.charAt(0);
								while(!tilt.equals("ok")){
									message = scIn.nextLine();
									while((message.charAt(0) != lastChar || !isAWord(message)) && !message.equals(exit)){
										System.out.println("Probald ujra!");
										message = scIn.nextLine();
										
									}
									pw.println(message);
									tilt = sc.nextLine();
									System.out.println(tilt);
									if(!tilt.equals("ok")){
										System.out.println("Probald ujra!");
									}
								}
								
							}
							
						}else{
							System.out.println(nev+ ": nyert");
						}
						
				}
				pw.close();
				
			} catch(Exception e)  {}
		}
	}

}

