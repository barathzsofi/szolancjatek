public class SzolancSzimulacio{
	
	public static void main(String[] args) throws Exception{
		
		String[] td = {"2"};
		
		TiltottDeploy.main(td);
		
		new Thread() {
            @Override
            public void run() {
                Server.main(new String[0]);
            }
        }.start();
		
		String[] j1 = {"jatekos1","szokincs1.txt"};
		String[] j2 = {"jatekos2","szokincs1.txt"};
		String[] j3 = {"jatekos3","szokincs1.txt"};
		String[] j4 = {"jatekos4","szokincs2.txt"};
		
		
		Runnable jatek =
			() -> {
					
					try{
						GepiKliens.main(j1);
						Thread.sleep(1000);
						GepiKliens.main(j2);
						Thread.sleep(1000);
						GepiKliens.main(j3);
						Thread.sleep(1000);
						GepiKliens.main(j4);
						Thread.sleep(1000);
					}catch(Exception e){
						System.out.println(e);
					}
					
			};
		new Thread(jatek).start();		
	}
	
}