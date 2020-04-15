import java.io.File;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


public class WBdriver {
	
	private ChromeDriver driver;
	
	public void Driver_init() {
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    	ChromeDriverService service = new ChromeDriverService.Builder()
    	                            .usingDriverExecutable(new File("/Users/yichaoqin/Desktop/ChromeDriver/chromedriver"))
    	                            .usingAnyFreePort()
    	                            .build();
    	ChromeOptions options = new ChromeOptions();
    	options.merge(capabilities);   
    	options.addArguments("--log-level=3");
    	driver = new ChromeDriver(service, options);
    	driver.setLogLevel(Level.OFF);
    	java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
	}
	
	public void connect() {
		//driver.get("https://www.majsoul.com/1/");
		driver.get("https://game.mahjongsoul.com/");
		
	}
	
	public void disconnect() {
		driver.quit();
	}
	
	
	public boolean LobbyLoaded() {
		try {
			driver.executeScript("var res = view.DesktopMgr.Inst == null; return res");
		}catch(Exception e) {return false;}
		return true;
	}
	public boolean isingaming() {
		if((boolean) driver.executeScript("var res = view.DesktopMgr.Inst == null; return res")) return false;
		return (boolean) driver.executeScript("var res = view.DesktopMgr.Inst.gameing; return res");
	}
	
	public boolean needscan() {
		String javaScript = "var res = view.DesktopMgr.Inst.players[0].can_discard; return res;";
		return (boolean) driver.executeScript(javaScript);
	}
	
	public int getstep() {
		return ((Long)driver.executeScript("var res = view.DesktopMgr.Inst.current_step; return res")).intValue();
	}
	
	public int getremaining() {
		return ((Long)driver.executeScript("var res = view.DesktopMgr.Inst.left_tile_count; return res")).intValue();
	}
	
	public String[] scanHandPai() {
		int hand_length = ((Long)driver.executeScript("var len = view.DesktopMgr.Inst.players[0].hand.length; return len")).intValue();
		String handpai[] = new String[hand_length];
		String javaScript = "";
		String out = "";
		for(int i = 0; i < hand_length; i++) {
			javaScript = "var res = view.DesktopMgr.Inst.players[0].hand["+Integer.toString(i)+"].val.toString();"
					  + "return res";
			out = (String) driver.executeScript(javaScript);
			if(out.equals("0m")) out = "5m";
			if(out.equals("0p")) out = "5p";
			if(out.equals("0s")) out = "5s";
			handpai[i] = out;
		}
        System.out.println("Pai-Scanning Finished");
        return handpai;
	}
	
	public String[] scan_qipai(String n) {
		int qipai_length = ((Long)driver.executeScript("var len = view.DesktopMgr.Inst.players["+n+"].container_qipai.pais.length; return len;")).intValue();
		String javaScript = "";
		javaScript = "var res = view.DesktopMgr.Inst.players[" + n + "].container_qipai.last_pai == null;"
				  + "return res";
		
		String myqipai[];
		
		if((boolean) driver.executeScript(javaScript) == false) {
			myqipai = new String[qipai_length+1];
			String out = "";
			javaScript = "var res = view.DesktopMgr.Inst.players["+n+"].container_qipai.last_pai.val.index;"
					  + "return res;";
			out = Integer.toString(((Long) driver.executeScript(javaScript)).intValue());
			javaScript = "var res = view.DesktopMgr.Inst.players["+n+"].container_qipai.last_pai.val.type;"
					  + "return res;";
			int type = ((Long) driver.executeScript(javaScript)).intValue();
			if(out.equals("0")) out = "5";
			out += type == 0 ? "p" : type == 1 ? "m" : type == 2 ? "s" : "z";
			myqipai[qipai_length] = out;
		}else {
			myqipai = new String[qipai_length];
		}
		String out = "";
		for(int i = 0; i < qipai_length; i++) {
			javaScript = "var res = view.DesktopMgr.Inst.players["+n+"].container_qipai.pais["+Integer.toString(i)+"].val.index;"
					  + "return res;";
			out = Integer.toString(((Long) driver.executeScript(javaScript)).intValue());
			javaScript = "var res = view.DesktopMgr.Inst.players["+n+"].container_qipai.pais["+Integer.toString(i)+"].val.type;"
					  + "return res;";
			int type = ((Long) driver.executeScript(javaScript)).intValue();
			if(out.equals("0")) out = "5";
			out += type == 0 ? "p" : type == 1 ? "m" : type == 2 ? "s" : "z";
			myqipai[i] = out;
		}
        //System.out.println("My QiPai-Scanning Finished");
        return myqipai;
	}
	
	public String[] scan_mingpai(String n) {
		int mingpai_length = ((Long)driver.executeScript("var len = view.DesktopMgr.Inst.players["+n+"].container_ming.pais.length; return len")).intValue();
		String mymingpai[] = new String[mingpai_length];
		String javaScript = "";
		String out = "";
		for(int i = 0; i < mingpai_length; i++) {
			javaScript = "var res = view.DesktopMgr.Inst.players["+n+"].container_ming.pais["+Integer.toString(i)+"].val.index;"
					  + "return res;";
			out = Integer.toString(((Long) driver.executeScript(javaScript)).intValue());
			javaScript = "var res = view.DesktopMgr.Inst.players["+n+"].container_ming.pais["+Integer.toString(i)+"].val.type;"
					  + "return res;";
			int type = ((Long) driver.executeScript(javaScript)).intValue();
			if(out.equals("0")) out = "5";
			out += type == 0 ? "p" : type == 1 ? "m" : type == 2 ? "s" : "z";
			mymingpai[i] = out;
		}
        return mymingpai;
	}
	
	public int get_count_ming(String n) {
		String javaScript = "var len = view.DesktopMgr.Inst.players["+n+"].container_ming.pais.length;"
				  		  + "return len;";
		return ((Long)driver.executeScript(javaScript)).intValue();
	}
	
	

}
