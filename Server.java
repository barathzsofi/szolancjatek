import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.lang.*;


public class Server {

    private final int port;
    private ServerSocket server;
	private boolean kovParose = false;
    private static int num = 0;
	public int timeout = 30000;
	public boolean vanJatekos = false;
	
	
    private Map<String, PrintWriter> clients = new HashMap<String, PrintWriter>();
    private Map<String, Integer> szamozas = new HashMap<String, Integer>();
	private Registry registry = LocateRegistry.getRegistry("localhost",12345);
	private int regNum = registry.list().length;
	
    public int getPort() {
        return port;
    }
    
    Server(int port) throws IOException {
        this.port = port;
        server = new ServerSocket(port);
        System.out.println("A server elindult.");
		server.setSoTimeout(timeout);
    }
	

    
    public void handleClients() {
		boolean l = true;
        while( l ) {
				try {
					new ClientHandler(server.accept()).start();
					//System.out.println(server.getSoTimeout());
				}catch( SocketTimeoutException e ) {
					//System.out.println("Van meg jatekos!");
					if(clients.isEmpty()){
						System.out.println("Socket timed out!");
						l=false;
					}

				} catch( IOException e ) {
					System.err.println("Hiba a kliensek fogadasakor.");
					e.printStackTrace();
				}

        }
    }
    
    private synchronized boolean addClient(String name, PrintWriter pw)throws Exception{
        clients.put(name, pw);
		szamozas.put(name,num);
		if(kovParose){
			send(name,"start",num);
			++num;
			kovParose = false;
		}else{
			kovParose = true;
		}

        return true;
    }
	
    
    private synchronized void removeClient(String name) {
        clients.remove(name);
		szamozas.remove(name);
    }
	
    
    private synchronized void send(String name, String message, int sz) {
        for (String n : clients.keySet()) {
            if( !n.equals(name) && szamozas.get(n)==sz) {
                clients.get(n).println(message);
            }
        }
    }
	
	
	private synchronized void sendBack(String name, String message, int sz) {
        for (String n : clients.keySet()) {
            if( n.equals(name) && szamozas.get(n)==sz) {
                clients.get(n).println(message);
            }
        }
    }
	
	
	private synchronized String getRivalsName(String name){
		String rival = "";
		int sz = szamozas.get(name);
		for(String n: clients.keySet()){
			if( !n.equals(name) && szamozas.get(n)==sz) {
                rival = n;
            }
		}
		return rival;
	}
	
	
    
    class ClientHandler extends Thread {
        
        PrintWriter pw;
        BufferedReader br;
        String name;
		String rival;
		boolean elso = true;
		int szamuk = -1;
		
		
        
        ClientHandler(Socket s) throws IOException {
            pw = new PrintWriter(s.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}

        
        @Override
        public void run() {
			PrintWriter os;
			
			
            try {

                name = br.readLine();

				addClient(name,pw);
				
				if(!kovParose){	
					elso = false;	
				}
				String fileName = name+".txt";
				File f = new File(fileName);
				os = new PrintWriter(f);
				
            } catch( Exception e ) {
                System.err.println("Inicializalasi problema egy kliensnel. Nev: " + name);
                return;
            }
			
            
            boolean clientError = false;
            String message = "";
			String exit = "exit";
			String nyert = "nyert";
			boolean nyertes = false;
			int kp = -1;
            try {
			
				int tiltottI = (szamozas.get(name)%regNum)+1;
				System.out.println("Csatlakozott: " + name + "(tsz:"+tiltottI+")");
				TiltottSzerverInterface tiltottSzerver = (TiltottSzerverInterface) registry.lookup("tiltott"+tiltottI);
				
				
				int rc = 0;
                while( !message.equals(exit) ) {
					kp = szamozas.get(name);
                    message = br.readLine();
					String uzenet = "";
                    if( message == null ) break;
					while (!uzenet.equals("ok")){
						if( !message.equals("exit")) {
							
							
							if(tiltottSzerver.tiltottE(message)){
								uzenet = "nok";
								sendBack(name, uzenet,kp);
								message = br.readLine();
								
							}else{
								uzenet = "ok";
								sendBack(name, uzenet,kp);
								send(name, message,kp);
								os.println(name+" "+message);
								if(rc==0){
									rival = getRivalsName(name);
									++rc;
								}
							}
							
						}else{
							uzenet = "ok";
							send(name,nyert,kp);
							nyertes = true;
						}
					}
					szamuk = szamozas.get(name);
                }
            } catch( IOException e ) {
                System.err.println("Kommunikacios problema egy kliensnel. Nev: " + name);
                clientError = true;
            } catch( Exception e ) {
                System.err.println("Problema egy kliensnel. Nev: " + name);
                clientError = true;
            } finally {
                removeClient(name);
                pw.close();
                if( clientError )
                    send(name, nyert,kp);
            }
			os.close();
			
			String ifErr = "";
			try{
				if(!szamozas.containsValue(szamuk)){
					
					String j1;
					String j2;
					
					if(elso){
						j1 = name;
						j2 = rival;
					}else{
						j2 = name;
						j1 = rival;
					}
				
					Date d = new Date();
					SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd kk_mm_SSS");
					String ido = form.format(d);
					
					String fileName = j1 + "_" + j2 + "_" + ido + ".txt";
					ifErr = fileName;
					File f = new File(fileName);
					PrintWriter osFinal = new PrintWriter(f);
					
					File fTmp1 = new File(j1 + ".txt");
					File fTmp2 = new File(j2 + ".txt");				
					Scanner sc1 = new Scanner(fTmp1);
					Scanner sc2 = new Scanner(fTmp2);

					String line = "";
					while(sc1.hasNext() || sc2.hasNext()){
						if(sc1.hasNext()){
							line = sc1.nextLine();
							osFinal.println(line);
						}
						if(sc2.hasNext()){
							line = sc2.nextLine();
							osFinal.println(line);
						}
					}
					
					osFinal.close();
					sc1.close();
					sc2.close();
		
				}
			}catch(Exception e){
				System.out.println("Hiba file letrehozasa soran "+ ifErr);				
			}
			
        }
		
    }
    
    public static void main(String[] args) {
        try {
            Server server = new Server(32123);
            server.handleClients();
        } catch( IOException e ) {
            System.err.println("Hiba a server inditasanal.");
            e.printStackTrace();
        }
    }

}