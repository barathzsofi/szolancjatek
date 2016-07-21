import java.rmi.*;

public interface TiltottSzerverInterface extends Remote {
	public boolean tiltottE(String szo)  throws RemoteException;
}