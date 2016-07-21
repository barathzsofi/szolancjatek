public class SzolancJatek{
	
	public static void main(String[] args) throws Exception{
		
		String[] td = {"1"};
		szolanc.TiltottDeploy.main(td);
		
		new Thread() {
            @Override
            public void run() {
                Server.main(new String[0]);
            }
        }.start();
		
		String[] j1 = {"Robot","szokincs1.txt"};
		
		Runnable jatek =
			() -> {
					GepiKliens.main(j1);
					InteraktivKliens.main(new String[0]);
			};
		new Thread(jatek).start();

	}
	
}