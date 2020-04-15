
import java.util.Scanner;

public class Driver implements Runnable{
	
	WBdriver wbdrivercaller;
	MJPai mjpaicaller;
	MJAI mjaicaller;
	Scanner scanner;
	int laststep;
	volatile boolean keepRunning = true;
	Driver(){
		wbdrivercaller = new WBdriver();
		wbdrivercaller.Driver_init();
		mjpaicaller = new MJPai();
		
		laststep = 0;
		scanner  = new Scanner(System.in);
	}
//	Driver(){
//		mjpaicaller = new MJPai();
//		
//		laststep = 0;
//		scanner  = new Scanner(System.in);
//	}
	
	public void start() {
		while(isgaming() == false)
			try {
				System.out.println("Ju hasn't started, waiting for another 5s");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.out.println("Local Error: Checking if in game thread sleep failed!");
				e.printStackTrace();
			}
		mjaicaller = new MJAI(mjpaicaller);
		while(isgaming()) {
			start_analyze();
		}
		System.out.println("Finish analyze and start()");
	}
	
	public void start_analyze() {
		
		int currentstep = wbdrivercaller.getstep();
		//System.out.println("Last step: "+ laststep + "Current Step:" + currentstep);
		if(wbdrivercaller.needscan() && currentstep > laststep) {
			laststep = currentstep;
			mjpaicaller.update_All(wbdrivercaller.getremaining(),
								   wbdrivercaller.scanHandPai(), wbdrivercaller.scan_qipai("0"), 
								   wbdrivercaller.scan_qipai("1"), wbdrivercaller.scan_qipai("2"),
								   wbdrivercaller.scan_qipai("3"), wbdrivercaller.scan_mingpai("0"),
								   wbdrivercaller.scan_mingpai("1"), wbdrivercaller.scan_mingpai("2"),
								   wbdrivercaller.scan_mingpai("3"));
			mjaicaller.updatepai();
			mjaicaller.do_All_analyze();
			
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void test() {
		mjaicaller = new MJAI(mjpaicaller);
		int remaining = 52;
		String handpai[] = {"1m","1m","1m","1m","1p","3s","3s","6m","1s","1s","1s","8s","4s","3s"};
		String qipai[] = {"1z"};
		String qipai1[] = {"2z"};
		String qipai2[] = {"2z"};
		String qipai3[] = {"3z"};
		String[] ming_0s = {};
		String[] ming_1s = {};
		String[] ming_2s = {};
		String[] ming_3s = {};
		
		mjpaicaller.update_All(remaining, handpai, qipai, qipai1, qipai2, qipai3, ming_0s, ming_1s, ming_2s, ming_3s);
		mjaicaller.updatepai();
		mjaicaller.do_All_analyze();
	}
	
	public boolean isgaming() {
		return wbdrivercaller.isingaming();
	}

	public static void main(String[] args) {
		
		Driver driver = new Driver();
		Thread t = new Thread(driver);
		driver.wbdrivercaller.connect();
		System.out.println("Starting game.Connecting");
		
		String input = "";
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Waiting for lobby loaded, type s and enter to start");
			input = sc.nextLine();
		}while(!input.equals("s"));
		
		t.start();
		
		do {
			input = sc.next();
			if (input.equals("r")) System.out.println("You typed RRRR");
		}while(!input.equals("q"));
		
        driver.keepRunning = false;
        t.interrupt();

	}
	@Override
	public void run() {
		
		while(keepRunning) {
			start();
			//test();
			laststep = -1;
		}
		wbdrivercaller.disconnect();
		System.out.println("Quiting game.Disonnecting");
	}

}
