import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GepiKliens {
    
    public static volatile boolean serverExit = false;
    public static String nev;
    private static String szokincs;
	
    GepiKliens(String host, int port) {
        try {
            Socket client = new Socket(host, port);
            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
            Scanner sc = new Scanner(client.getInputStream());            
   
            File fromFile = new File(szokincs);
			String tmpName = nev+"temp.txt";
			File toFile = new File(tmpName);
			if(toFile.exists()){
				toFile.delete();
			}
			Files.copy(fromFile.toPath(), toFile.toPath());
						
        	(new Thread(new Jatek(sc, pw, tmpName, nev))).start();
            
        } catch (Exception e) {
            System.err.println("Nem sikerult kapcsolodni a szerverhez.");
            e.printStackTrace();
            return;
        }
    }
    
    public static void main(String[] args) {
		nev = args[0];
		szokincs = args[1];
        new GepiKliens("localhost", 32123);
    }
	
	class Jatek implements Runnable {
		PrintWriter pw;
		Scanner sc;
		String szokincs;
		String nev;
		String start = "start";
		String nyert = "nyert";
		String exit = "exit";
		
		Jatek(Scanner sc, PrintWriter pw, String szokincs, String nev) {
			this.pw = pw;
			this.sc = sc;
			this.szokincs = szokincs;
			this.nev = nev;
		}
		
		@Override
		public void run() {
			try{
				pw.println(nev);			
				String message = "";
				String prev = "";
				boolean jatekVege = false;
				

				while( !jatekVege  && !prev.equals(nyert)) {
					String tilt = "";
					prev = sc.nextLine();
					
					if(!prev.equals(nyert)){
							
						File inFile = new File(szokincs);
						Scanner scF = new Scanner(inFile);
						File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
						PrintWriter pwF = new PrintWriter(new FileWriter(tempFile));
						if(prev.equals(start)){
								
								boolean nemKuldott = true;
								while(!tilt.equals("ok") && scF.hasNextLine()){
									message = scF.nextLine();
									pw.println(message);
									tilt = sc.nextLine();
									System.out.println(nev +": " + message + " - " + tilt);
									if(tilt.equals("ok")){
										nemKuldott = false;
									}
								}
								while(scF.hasNextLine()){
									pwF.println(scF.nextLine());
									pwF.flush();
								}
								if(nemKuldott){
									message = exit;
									pw.println(message);
								}
								
						}else{
							boolean nemKuldott = true;
							String tmp = "";
							String lc = prev.substring(prev.length() - 1);
							char lastChar = lc.charAt(0);	
							while(scF.hasNextLine()){
								tmp = scF.nextLine();
								if(tmp.charAt(0) == lastChar && nemKuldott){
									message = tmp;
									pw.println(message);
									tilt = sc.nextLine();
									System.out.println(nev + ": " + message + " - " + tilt);
									if(tilt.equals("ok")){
										nemKuldott = false;
									}
								}else{
									pwF.println(tmp);
									pwF.flush();
								}
							}
							if(nemKuldott){
								
								message = exit;
								pw.println(message);
							}
							
						}
						scF.close();
						jatekVege = (prev.equals(exit) || message.equals(exit));
						pwF.close();
							
						if (!inFile.delete()) {
							System.out.println("Could not delete file");
							return;
						}
						if (!tempFile.renameTo(inFile))
							System.out.println("Could not rename file");
					}else{
						System.out.println(nev+ " nyert");
					}
					
				}
				pw.close();
				File delFile = new File(nev+"temp.txt");
				if(!delFile.delete()){
					System.out.println("Could not delete file");
					return;
				}
				
			} catch(Exception e)  {}
		}
	}
		
	
}

