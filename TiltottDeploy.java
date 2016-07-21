import java.rmi.registry.*;

public class TiltottDeploy{
	
	public static void main(String[] args) throws Exception{
		
		try{
			int num = Integer.parseInt(args[0]);
		}catch (Exception e){
			System.out.println("A parameternek szamnak kell lennie!");
			return;
		}
		
		if(args.length == 1 && Integer.parseInt(args[0])>0){
		
			Registry registry = LocateRegistry.createRegistry(12345);
			String nev = "";
			int num = Integer.parseInt(args[0]);
			for( int i = 1; i <= num; ++i ) {
				nev = "tiltott"+i;
				TiltottSzerver tiltott = new TiltottSzerver(nev);
				registry.rebind(nev, tiltott);
			}
		
		}else{
			System.out.println("Hibas parameterezes");
			return;
		}
	}
	
}