import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

public class TiltottSzerver extends UnicastRemoteObject implements TiltottSzerverInterface{
	
	private List<String> tiltott;
	private String name;
	
	TiltottSzerver(String name) throws RemoteException, FileNotFoundException{
		this.name = name;
		this.tiltott = new ArrayList<String>();
		tiltottFeltoltese(name);		
	}
	
	public boolean tiltottE(String szo) throws RemoteException{
		if(!tiltott.contains(szo)){
			tiltott.add(szo);
			//System.out.println(name);
			return false;
		}else {
			return tiltott.contains(szo);
		}
	}
	
	
	private void tiltottFeltoltese(String name) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(name+".txt"));
		while (sc.hasNext()) {
            String szoveg = sc.next();
            tiltott.add(szoveg);
        }
	}
	
}