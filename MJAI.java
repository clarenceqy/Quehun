import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap; 


public class MJAI {
	MJPai mjpai;
	List<String> WanPai;
	List<String> TongPai;
	List<String> SuoPai;
	List<String> ZiPai;
	List<String> MingWanPai;
	List<String> MingTongPai;
	List<String> MingSuoPai;
	List<String> MingZiPai;
	List<List<String>> HandPaiList;
	List<List<String>> FormedPaiList;
	HashMap<String,Double> Allwinrates;
	int donekezi, doneshunzi = 0;
	double winrate = 0.0;
	String msg = "";
	
	MJAI(MJPai mjpai){
		this.mjpai = mjpai;
		WanPai = new ArrayList<>();
		TongPai = new ArrayList<>();
		SuoPai = new ArrayList<>();
		ZiPai = new ArrayList<>();
		MingWanPai = new ArrayList<>();
		MingTongPai = new ArrayList<>();
		MingSuoPai = new ArrayList<>();
		MingZiPai = new ArrayList<>();
		FormedPaiList = new ArrayList<>();
		HandPaiList = new ArrayList<>();
		Allwinrates = new HashMap<>();
		FormedPaiList = new ArrayList<>();
		HandPaiList.add(WanPai);
		HandPaiList.add(TongPai);
		HandPaiList.add(SuoPai);
		HandPaiList.add(ZiPai);
	}
	
	public void updatepai() {
		List<String> HandPai = mjpai.myhandPai;
		WanPai.clear();
		TongPai.clear();
		SuoPai.clear();
		ZiPai.clear();
		MingWanPai.clear();
		MingTongPai.clear();
		MingSuoPai.clear();
		MingZiPai.clear();
		Allwinrates.clear();
		for(String s: HandPai) {
			if(s.charAt(1) == 'm') WanPai.add(s);
			if(s.charAt(1) == 'p') TongPai.add(s);
			if(s.charAt(1) == 's') SuoPai.add(s);
			if(s.charAt(1) == 'z') ZiPai.add(s);
		}
		for(List<String> l: HandPaiList){
			Collections.sort(l);
		}
		donekezi = 0;
		doneshunzi = 0;
		for(String s:mjpai.ming_me) {
			if(s.contains("m")) MingWanPai.add(s);
			else if(s.contains("p")) MingTongPai.add(s);
			else if(s.contains("s")) MingSuoPai.add(s);
			else MingZiPai.add(s);
		}
	}
	
	public void updatewinrate(String type) {
		Allwinrates.put(type, winrate);
	}
	
	public boolean isShunzi(String p1,String p2, String p3) {
		if(p1.charAt(1) == 'z') return false;
		double p1num = (double)Integer.parseInt(String.valueOf(p1.charAt(0)));
		double p2num = (double)Integer.parseInt(String.valueOf(p2.charAt(0)));
		double p3num = (double)Integer.parseInt(String.valueOf(p3.charAt(0)));
		return (p1num+p3num) / 2 == p2num && p1num != p2num;
	}
	public boolean isKezi(String p1,String p2, String p3) {
		return p1.charAt(0) == p2.charAt(0) && p2.charAt(0) == p3.charAt(0);
	}
	public boolean isYaojiu(String pai) {
		return pai.charAt(0) == '1' || pai.charAt(0) == '9' || pai.charAt(1) == 'z'; 
	}
	public double probformKezi(List<String> l,int painum,String type) {
		int needforkezi = 3;
		double probforkezi = 0.0;
		for(String s:l) {
			if(Integer.parseInt(String.valueOf(s.charAt(0))) == painum) {
				needforkezi--;
			}
		}
		probforkezi = probforpai(painum+type,needforkezi);
		if (needforkezi == 0) {
			return 1.0;
		}
		return probforkezi;
	}
	public double probformShunzi(List<String> l,int painum,String type) {
		double probforshunzi = 1.0;
		if(!l.contains(painum+type)) probforshunzi = probforshunzi * probforpai(painum+type,1);
		if(!l.contains((painum+1)+type)) probforshunzi = probforshunzi * probforpai((painum+1)+type,1);
		if(!l.contains((painum+2)+type)) probforshunzi = probforshunzi * probforpai((painum+2)+type,1);
		return probforshunzi;
	}
	public List<List<String>> preprocess_duanyaojiu() {
		//FormedPaiList.clear();
		List<List<String>> modHandPaiList = new ArrayList<>();
		for(List<String> l: HandPaiList) {
			List<String> modHandPai = new ArrayList<>();
			if(l.size() < 3) {
				for(String s: l) {
					modHandPai.add(s);
				}
			}
			else {
				int index = 0;
				boolean isshunzi,iskezi = false;
				while(index < l.size() - 2) {
					isshunzi = isShunzi(l.get(index),l.get(index+1),l.get(index+2));
					iskezi = isKezi(l.get(index),l.get(index+1),l.get(index+2));
					if(isshunzi && !isYaojiu(l.get(index))) {
						List<String> combo = new ArrayList<>();
						combo.add(l.get(index));
						combo.add(l.get(index+1));
						combo.add(l.get(index+2));
						FormedPaiList.add(combo);
						index += 2;
						doneshunzi++;
					}
					if(iskezi && !isYaojiu(l.get(index))) {
						List<String> combo = new ArrayList<>();
						combo.add(l.get(index));
						combo.add(l.get(index+1));
						combo.add(l.get(index+2));
						FormedPaiList.add(combo);
						index += 2;
						donekezi++;
					}
					if((!isshunzi && !iskezi )|| isYaojiu(l.get(index))) {
						modHandPai.add(l.get(index));
					}
					index++;
				}
				while(index < l.size()) {
					modHandPai.add(l.get(index));
					index++;
				}
			}
			modHandPaiList.add(modHandPai);
		}
		return modHandPaiList;
	}
	public List<List<String>> preprocess(List<List<String>> HandPaiList) {
		//FormedPaiList.clear();
		List<List<String>> modHandPaiList = new ArrayList<>();
		for(List<String> l: HandPaiList) {
			List<String> modHandPai = new ArrayList<>();
			if(l.size() < 3) {
				for(String s: l) {
					modHandPai.add(s);
				}
			}
			else {
				int index = 0;
				boolean isshunzi,iskezi = false;
				while(index < l.size() - 2) {
					isshunzi = isShunzi(l.get(index),l.get(index+1),l.get(index+2));
					iskezi = isKezi(l.get(index),l.get(index+1),l.get(index+2));
					if(isshunzi) {
						List<String> combo = new ArrayList<>();
						combo.add(l.get(index));
						combo.add(l.get(index+1));
						combo.add(l.get(index+2));
						FormedPaiList.add(combo);
						index += 2;
						doneshunzi++;
					}
					if(iskezi) {
						List<String> combo = new ArrayList<>();
						combo.add(l.get(index));
						combo.add(l.get(index+1));
						combo.add(l.get(index+2));
						FormedPaiList.add(combo);
						index += 2;
						donekezi++;
					}
					if(!isshunzi && !iskezi) {
						modHandPai.add(l.get(index));
					}
					index++;
				}
				while(index < l.size()) {
					modHandPai.add(l.get(index));
					index++;
				}
			}
			modHandPaiList.add(modHandPai);
		}
		return modHandPaiList;
	}
	public List<List<String>> preprocess_Duiduihu() {
		List<List<String>> modHandPaiList = new ArrayList<>();
		for(List<String> l: HandPaiList) {
			List<String> modHandPai = new ArrayList<>();
			if(l.size() < 3) {
				for(String s: l) {
					modHandPai.add(s);
				}
			}
			else {
				int index = 0;
				boolean isshunzi,iskezi = false;
				while(index < l.size() - 2) {
					iskezi = isKezi(l.get(index),l.get(index+1),l.get(index+2));
					if(iskezi) {
						List<String> combo = new ArrayList<>();
						combo.add(l.get(index));
						combo.add(l.get(index+1));
						combo.add(l.get(index+2));
						FormedPaiList.add(combo);
						index += 2;
						donekezi++;
					}
					else {
						modHandPai.add(l.get(index));
					}
					index++;
				}
				while(index < l.size()) {
					modHandPai.add(l.get(index));
					index++;
				}
			}
			modHandPaiList.add(modHandPai);
		}
		return modHandPaiList;
	}
	
	public List<List<String>> preprocess_feng_yi() {
		//FormedPaiList.clear();
		List<List<String>> modHandPaiList = new ArrayList<>();
		for(List<String> l: HandPaiList) {
			List<String> modHandPai = new ArrayList<>();
			if(l.size() < 3) {
				for(String s: l) {
					modHandPai.add(s);
				}
			}
			else {
				int index = 0;
				boolean isshunzi,iskezi = false;
				while(index < l.size() - 2) {
					isshunzi = isShunzi(l.get(index),l.get(index+1),l.get(index+2));
					iskezi = isKezi(l.get(index),l.get(index+1),l.get(index+2));
					if(isshunzi) {
						List<String> combo = new ArrayList<>();
						combo.add(l.get(index));
						combo.add(l.get(index+1));
						combo.add(l.get(index+2));
						FormedPaiList.add(combo);
						index += 2;
						doneshunzi++;
					}
					if(iskezi) {
						List<String> combo = new ArrayList<>();
						combo.add(l.get(index));
						combo.add(l.get(index+1));
						combo.add(l.get(index+2));
						FormedPaiList.add(combo);
						index += 2;
						donekezi++;
					}
					if(!isshunzi && !iskezi) {
						modHandPai.add(l.get(index));
					}
					index++;
				}
				while(index < l.size()) {
					modHandPai.add(l.get(index));
					index++;
				}
			}
			modHandPaiList.add(modHandPai);
		}
		return modHandPaiList;
	}
	
	
	public void printmodpai() {
		List<List<String>> modPaiList = preprocess(HandPaiList);
		for(List<String> l: modPaiList) {
			for(String s:l) {
				System.out.print(s +" ");
			}
			System.out.println();
		}
	}
	public double probforpai(String pai,int neednum) {
		int hasnum = mjpai.remaining_tiles.get(pai);
		if(hasnum >= neednum) {
			return combination(neednum,hasnum) / combination(neednum,mjpai.remaining);
		}
		return 0;
	}
	
	public double probfor2pai(String pai1,String pai2,int neednum) {
		int hasnum1 = mjpai.remaining_tiles.get(pai1);
		int hasnum2 = mjpai.remaining_tiles.get(pai2);
		if(hasnum1 >= neednum && hasnum2 >= neednum) {
			return combination(neednum,hasnum1) *  combination(neednum,hasnum2) / combination(2,mjpai.remaining);
		}
		return 0;
	}
	
	
	public double combination(int c,int n) {
		int x  = 1,y =1;
		int base = n-c;
		for(int i = n; i > base;i--) {
			x = x * i;
		}
		for(int i = c; i > 1;i--) {
			y = y * i;
		}
		return ((double)x)/y;
	}
	
	public void do_All_analyze() {
		String discard = "";
		List<List<String>> modlist;
		HashMap <String,Double>chancemap = new HashMap();
		HashMap <String,String>discardmap = new HashMap();
		HashMap <String,String>msgmap = new HashMap();
		List<String> namelist = new ArrayList();
		if(mjpai.ming_0 == 0) {
			//LiChi
			modlist = preprocess(HandPaiList);
			discard = analyze_lichi(modlist);
			updatewinrate("Lichi");
			FormedPaiList.clear();
			namelist.add("Lichi");
			chancemap.put("Lichi",winrate);
			discardmap.put("Lichi", discard);
			//System.out.println("Lichi: "+ Allwinrates.get("Lichi") +"	"+discard);
			
			//Yibeikou
			discard = analyze_yibeikou();
			updatewinrate("Yibeikou");
			FormedPaiList.clear();
			namelist.add("Yibeikou");
			chancemap.put("Yibeikou",winrate);
			discardmap.put("Yibeikou", discard);
			msgmap.put("Yibeikou", msg);
			//System.out.println("Yibeikou: "+ Allwinrates.get("Yibeikou") +"	"+discard);
			
			//Qiduizi
			discard = analyze_qiduizi();
			updatewinrate("Qiduizi");
			FormedPaiList.clear();
			namelist.add("Qiduizi");
			chancemap.put("Qiduizi",winrate);
			discardmap.put("Qiduizi", discard);
			//System.out.println("Qiduizi: "+ Allwinrates.get("Qiduizi") +"	"+discard);
		}
		
		
		//Duanyaojiu
		modlist = preprocess_duanyaojiu();
		discard = analyze_duanyaojiu();
		updatewinrate("Duanyaojiu");
		FormedPaiList.clear();
		namelist.add("Duanyaojiu");
		chancemap.put("Duanyaojiu",winrate);
		discardmap.put("Duanyaojiu", discard);
		//System.out.println("Duanyaojiu: "+ Allwinrates.get("Duanyaojiu") +"	"+discard);
		
		//Duiduihu
		discard = analyze_duiduihu();
		updatewinrate("Duiduihu");
		FormedPaiList.clear();
		namelist.add("Duiduihu");
		chancemap.put("Duiduihu",winrate);
		discardmap.put("Duiduihu", discard);
		//System.out.println("Duiduihu: "+ Allwinrates.get("Duiduihu") +"	"+discard);
		
		//Sansetongke
		discard = analyze_sansetongke();
		updatewinrate("Sansetongke");
		FormedPaiList.clear();
		namelist.add("Sansetongke");
		chancemap.put("Sansetongke",winrate);
		discardmap.put("Sansetongke", discard);
		msgmap.put("Sansetongke", msg);
		//System.out.println("Sansetongke: "+ Allwinrates.get("Sansetongke") +"	"+discard);
		
		//Sansetongshun
		discard = analyze_sansetongshun();
		updatewinrate("Sansetongshun");
		FormedPaiList.clear();
		namelist.add("Sansetongshun");
		chancemap.put("Sansetongshun",winrate);
		discardmap.put("Sansetongshun", discard);
		msgmap.put("Sansetongshun", msg);
		//System.out.println("Sansetongshun: "+ Allwinrates.get("Sansetongshun") +"	"+discard);
		
		
		//Yipai
		discard = analyze_yipai();
		updatewinrate("Yipai");
		FormedPaiList.clear();
		namelist.add("Yipai");
		chancemap.put("Yipai",winrate);
		discardmap.put("Yipai", discard);
		msgmap.put("Yipai", msg);
		//System.out.println("Yipai: "+ Allwinrates.get("Yipai") +"	"+discard);
		Collections.sort(namelist, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				double chance1 = chancemap.get(o1);
				double chance2 = chancemap.get(o2);
				if(chance1 < chance2) return 1;
				else if(chance1 == chance2) return 0;
				return -1;
			}
			
		});
		for(String s: namelist) {
			System.out.println(discardmap.get(s)+" "+s+": "+chancemap.get(s)+"	"+msgmap.getOrDefault(s, ""));
		}
		System.out.println();
	}
	
	public String analyze_duanyaojiu() {
		List<List<String>> modPaiList = preprocess_duanyaojiu();
		double totalprob = 1;
		String discard = "";
		int times = 4-FormedPaiList.size()-mjpai.ming_0/3;
		String discardquetou = "";
		double quetourate  = 1.0;
		List<Double> winrates = new ArrayList<>();
		List<Double> quetourates = new ArrayList<>();
		for(List<String> l: modPaiList) {
			for(String s: l) {
				String pai = s;
				int painum = Integer.parseInt(String.valueOf(pai.charAt(0)));
				String type = String.valueOf(pai.charAt(1));
				int needkezi = 3;
				double probforkezi = 0.0;
				double probforshunzi = 0.0;
				if(painum != 1 && painum != 9 && !type.equals("z")) {
					for(int i = 0; i <l.size();i++) {
						if(l.get(i).equals(pai)) {
							needkezi--;
							//break;
						}
					}
					probforkezi = probforpai(pai,needkezi);
					if(painum == 2 && !type.equals("z")) {
						double probnum3 = l.contains(3+type) ? 1 : probforpai(3+type,1);
						double probnum4 = l.contains(4+type) ? 1 : probforpai(4+type,1);
						double prob34 = (probnum3 != 1 && probnum4 != 1) ? probfor2pai(3+type,4+type,1): probnum3*probnum4;
						probforshunzi = prob34;
					}
					else if(painum == 8 && !type.equals("z")) {
						double probnum6 = l.contains(6+type) ? 1 : probforpai(6+type,1);
						double probnum7 = l.contains(7+type) ? 1 : probforpai(7+type,1);
						double prob67 = (probnum6 != 1 && probnum7 != 1) ? probfor2pai(6+type,7+type,1): probnum6*probnum7;
						probforshunzi = prob67;
					}
					else if(painum == 3 && !type.equals("z")) {
						double probnum2 = l.contains(2+type) ? 1 : probforpai(2+type,1);
						double probnum4 = l.contains(4+type) ? 1 : probforpai(4+type,1);
						double probnum5 = l.contains(5+type) ? 1 : probforpai(5+type,1);
						double prob24 = (probnum2 != 1 && probnum4 != 1) ? probfor2pai(2+type,4+type,1): probnum2*probnum4;
						double prob45 = (probnum4 != 1 && probnum5 != 1) ? probfor2pai(4+type,5+type,1): probnum4*probnum5;
						probforshunzi = prob24+prob45;
					}
					else if(painum == 7 && !type.equals("z")) {
						double probnum5 = l.contains(5+type) ? 1 : probforpai(5+type,1);
						double probnum6 = l.contains(6+type) ? 1 : probforpai(6+type,1);
						double probnum8 = l.contains(8+type) ? 1 : probforpai(8+type,1);
						double prob56 = (probnum5 != 1 && probnum6 != 1) ? probfor2pai(5+type,6+type,1): probnum5*probnum6;
						double prob68 = (probnum6 != 1 && probnum8 != 1) ? probfor2pai(6+type,8+type,1): probnum6*probnum8;
						probforshunzi = prob56+prob68;
					}
					else if(painum > 3 && !type.equals("z")){
						double probnummin1 = l.contains((painum-1)+type) ? 1 : probforpai((painum-1)+type,1);
						double probnummin2 = l.contains((painum-2)+type) ? 1 : probforpai((painum-2)+type,1);
						double probnumplus1 = l.contains((painum+1)+type) ? 1 : probforpai((painum+1)+type,1);
						double probnumplus2 = l.contains((painum+2)+type) ? 1 : probforpai((painum+2)+type,1);
						double prob1 = (probnummin1 != 1 && probnummin2 != 1) ? probfor2pai((painum-1)+type,(painum-2)+type,1): probnummin1*probnummin2;
						double prob2 = (probnummin2 != 1 && probnumplus1 != 1) ? probfor2pai((painum-1)+type,(painum+1)+type,1): probnummin1*probnumplus1;
						double prob3 = (probnumplus1 != 1 && probnumplus2 != 1) ? probfor2pai((painum+1)+type,(painum+2)+type,1): probnumplus1*probnumplus2;
						probforshunzi = prob1+prob2+prob3;
					}
				}
				if(probforshunzi+probforkezi<totalprob) {
					totalprob = probforshunzi+probforkezi;
					discard = pai;
				}
				double currquetourate = isYaojiu(pai) ? 0: needkezi == 1 ? 1:probforpai(pai,1);
				quetourates.add(currquetourate);
				if(currquetourate < quetourate) {
					quetourate = currquetourate;
					discardquetou = pai;
				}
				winrates.add(probforshunzi+probforkezi);
			}
		}
		Collections.sort(winrates);
		Collections.sort(quetourates);
		double chance = 1.0;
		if(times == 0) {
			if(quetourates.size() == 0) this.winrate = 1.0;
			else this.winrate = quetourates.get(quetourates.size()-1);
			return discardquetou;
		}
		for(int i = 0; i < times; i++) {
			chance = chance * winrates.get(winrates.size()-1-i);
		}
		
		this.winrate = chance;
		return discard;
	}
	
	public String analyze_lichi(List<List<String>> modPaiList) {
		double totalprob = 1;
		String discard = "";
		int times = 4-FormedPaiList.size()-mjpai.ming_0/3;
		String discardquetou = "";
		double quetourate  = 1.0;
		List<Double> winrates = new ArrayList<>();
		List<Double> quetourates = new ArrayList<>();
		for(List<String> l:modPaiList) {
			for(String s:l) {
				String pai = s;
				int painum = Integer.parseInt(String.valueOf(pai.charAt(0)));
				String type = String.valueOf(pai.charAt(1));
				int needkezi = 3;
				for(int i = 0; i <l.size();i++) {
					if(l.get(i).equals(pai)) {
						needkezi--;
						//break;
					}
				}
				double probforkezi = probforpai(pai,needkezi);
				double probforshunzi = 0;
				
				if(painum == 1 && !type.equals("z")) {
					if(l.contains(2+type)) probforshunzi = probforpai(3+type,1);
					else if(l.contains(3+type)) probforshunzi = probforpai(2+type,1);
					else probforshunzi = probfor2pai(2+type,3+type,1);
				}
				else if(painum == 9 && !type.equals("z")) {
					if(l.contains(8+type)) probforshunzi = probforpai(7+type,1);
					else if(l.contains(7+type)) probforshunzi = probforpai(8+type,1);
					else probforshunzi = probfor2pai(7+type,8+type,1);
				}
				else if(painum == 2 && !type.equals("z")) {
					double probnum1 = l.contains(1+type) ? 1 : probforpai(1+type,1);
					double probnum3 = l.contains(3+type) ? 1 : probforpai(3+type,1);
					double probnum4 = l.contains(4+type) ? 1 : probforpai(4+type,1);
					double prob13 = (probnum1 != 1 && probnum3 != 1) ? probfor2pai(1+type,3+type,1): probnum1*probnum3;
					double prob34 = (probnum3 != 1 && probnum4 != 1) ? probfor2pai(3+type,4+type,1): probnum3*probnum4;
					probforshunzi = prob13+prob34;
				}
				else if(painum == 8 && !type.equals("z")) {
					double probnum6 = l.contains(6+type) ? 1 : probforpai(6+type,1);
					double probnum7 = l.contains(7+type) ? 1 : probforpai(7+type,1);
					double probnum9 = l.contains(9+type) ? 1 : probforpai(9+type,1);
					double prob67 = (probnum6 != 1 && probnum7 != 1) ? probfor2pai(6+type,7+type,1): probnum6*probnum7;
					double prob79 = (probnum7 != 1 && probnum9 != 1) ? probfor2pai(7+type,9+type,1): probnum7*probnum9;
					probforshunzi = prob67+prob79;
				}		
				else if(painum > 2 && !type.equals("z")){
					double probnummin1 = l.contains((painum-1)+type) ? 1 : probforpai((painum-1)+type,1);
					double probnummin2 = l.contains((painum-2)+type) ? 1 : probforpai((painum-2)+type,1);
					double probnumplus1 = l.contains((painum+1)+type) ? 1 : probforpai((painum+1)+type,1);
					double probnumplus2 = l.contains((painum+2)+type) ? 1 : probforpai((painum+2)+type,1);
					double prob1 = (probnummin1 != 1 && probnummin2 != 1) ? probfor2pai((painum-1)+type,(painum-2)+type,1): probnummin1*probnummin2;
					double prob2 = (probnummin2 != 1 && probnumplus1 != 1) ? probfor2pai((painum-1)+type,(painum+1)+type,1): probnummin1*probnumplus1;
					double prob3 = (probnumplus1 != 1 && probnumplus2 != 1) ? probfor2pai((painum+1)+type,(painum+2)+type,1): probnumplus1*probnumplus2;
					probforshunzi = prob1+prob2+prob3;
				}
				if(probforshunzi+probforkezi<totalprob) {
					totalprob = probforshunzi+probforkezi;
					discard = pai;
				}
				double currquetourate = needkezi == 1 ? 1:probforpai(pai,1);
				quetourates.add(currquetourate);
				if(currquetourate < quetourate) {
					quetourate = currquetourate;
					discardquetou = pai;
				}
				winrates.add(probforshunzi+probforkezi);
			}
		}
		Collections.sort(winrates);
		Collections.sort(quetourates);
		double chance = 1.0;
		
		if(times == 0) {
			if(quetourates.size() == 0) this.winrate = 1.0;
			else this.winrate = quetourates.get(quetourates.size()-1);
			//if(winrate == 0) {System.out.println("Formed pai:"+FormedPaiList.size()+"  Quetousize:"+quetourates.size());}
			return discardquetou;
		}
		for(int i = 0; i < times; i++) {
			chance = chance * winrates.get(winrates.size()-1-i);
		}
		
		this.winrate = chance;
		//if(winrate == 0) {System.out.println("Formed pai:"+FormedPaiList.size()+"  winratessize:"+winrates.size()+"   Biggest:"+ winrates.get(winrates.size()-1));}
		return discard;
	}
	
	public String analyze_yipai() {
		String discard = "";
		List<List<String>> modlist = new ArrayList();
		if(mjpai.ming_me.contains("1z") || mjpai.ming_me.contains("5z") || mjpai.ming_me.contains("6z") || mjpai.ming_me.contains("7z")) {
			modlist = preprocess(HandPaiList);
			return analyze_lichi(modlist);
		}
		else {
			int painum = 0;
			double maxprob = 0.0;
			double winrates = 0.0;
			for(int i = 0; i< 4;i++) {
				winrates = i>0 ? probformKezi(ZiPai,i+4,"z") : probformKezi(ZiPai,i+1,"z");
				//System.out.println(winrates);
				if(winrates > maxprob) {
					maxprob = winrates;
					painum = i > 0 ? i+4 :i+1;
				}
			}
			msg = "Need yipai:" + painum + "z";
			FormedPaiList.add(new ArrayList());
			List<String> modzi = new ArrayList();
			for(String s: ZiPai) {
				if(Integer.parseInt(String.valueOf(s.charAt(0))) != painum) modzi.add(s);
			}
			modlist.add(WanPai);
			modlist.add(TongPai);
			modlist.add(SuoPai);
			modlist.add(modzi);
			modlist = preprocess(modlist);
			discard = analyze_lichi(modlist);
			this.winrate = maxprob * this.winrate;
			//if(winrate == 0)System.out.println(maxprob +" "+winrates+" "+painum);
		}
		return discard;
	}
	
	public String analyze_sansetongshun() {
		String discard = "";
		double[] winrates = new double[7];
		double probw = 0.0;
		double probp = 0.0;
		double probs = 0.0;
		for(int i = 1; i <= 7;i++) {
			probw = probformShunzi(MingWanPai,i,"m") == 1 ? 1 :probformShunzi(WanPai,i,"m");
			probp = probformShunzi(MingTongPai,i,"p") == 1 ? 1 :probformShunzi(TongPai,i,"p");
			probs = probformShunzi(MingSuoPai,i,"s") == 1 ? 1 :probformShunzi(SuoPai,i,"s");
			winrates[i-1] = probw * probp *probs;
			//System.out.println(" "+probw+"  "+probp+"  "+probs);
		}
		double max = 0.0;
		int index = 0;
		for(int i = 0; i < 7;i++) {
			if(winrates[i] > max) {
				max = winrates[i];
				index = i;
			}
		}
		msg = "Need combo:" + (index+1) + (index+2) + (index+3);
		List<String> modwan = new ArrayList<>();
		List<String> modtong = new ArrayList<>();
		List<String> modsuo = new ArrayList<>();
		List<String> donewan = new ArrayList<>();
		List<String> donetong = new ArrayList<>();
		List<String> donesuo = new ArrayList<>();
		List<List<String>> modPaiList = new ArrayList<>();
		for(String s: WanPai) {
			int start = Integer.parseInt(String.valueOf(s.charAt(0)));
			if((start == index+1 || start == index+2 || start == index+3) && !donewan.contains(s)) donewan.add(s);
			else modwan.add(s);
		}
		for(String s: TongPai) {
			int start = Integer.parseInt(String.valueOf(s.charAt(0)));
			if((start == index+1 || start == index+2 || start == index+3) && !donetong.contains(s)) donetong.add(s);
			else modtong.add(s);
		}
		for(String s: SuoPai) {
			int start = Integer.parseInt(String.valueOf(s.charAt(0)));
			if((start == index+1 || start == index+2 || start == index+3) && !donesuo.contains(s)) donesuo.add(s);
			else modsuo.add(s);
		}
		modPaiList.add(modwan);
		modPaiList.add(modtong);
		modPaiList.add(modsuo);
		modPaiList.add(ZiPai);
		FormedPaiList.add(donewan);
		FormedPaiList.add(donetong);
		FormedPaiList.add(donesuo);
		List<List<String>> temp = preprocess(modPaiList);
		discard = analyze_lichi(temp);
		
		this.winrate = this.winrate * max;
		return discard;
	}
	public String analyze_sansetongke() {
		
		//FormedPaiList.clear();
		String discard = "";
		double[] winrates = new double[9];
		double probw = 0.0;
		double probp = 0.0;
		double probs = 0.0;
		for(int i = 1; i <= 9;i++) {
			probw = probformKezi(MingWanPai,i,"m") == 1 ? 1 :probformKezi(WanPai,i,"m");
			probp = probformKezi(MingTongPai,i,"p") == 1 ? 1 :probformKezi(TongPai,i,"p");
			probs = probformKezi(MingSuoPai,i,"s") == 1 ? 1 :probformKezi(SuoPai,i,"s");
			winrates[i-1] = probw * probp *probs;
			//System.out.println(i+" "+probw+"  "+probp+"  "+probs);
		}
		double max = 0.0;
		int index = 0;
		for(int i = 0; i < 9;i++) {
			if(winrates[i] > max) {
				max = winrates[i];
				index = i;
			}
		}
		msg = "Need combo:" + (index+1);
		List<String> modwan = new ArrayList<>();
		List<String> modtong = new ArrayList<>();
		List<String> modsuo = new ArrayList<>();
		List<String> donewan = new ArrayList<>();
		List<String> donetong = new ArrayList<>();
		List<String> donesuo = new ArrayList<>();
		List<List<String>> modPaiList = new ArrayList<>();
		for(String s: WanPai) {
			if(Integer.parseInt(String.valueOf(s.charAt(0))) != index+1) modwan.add(s);
			else donewan.add(s);
		}
		for(String s: TongPai) {
			if(Integer.parseInt(String.valueOf(s.charAt(0))) != index+1) modtong.add(s);
			else donetong.add(s);
		}
		for(String s: SuoPai) {
			if(Integer.parseInt(String.valueOf(s.charAt(0))) != index+1) modsuo.add(s);
			else donesuo.add(s);
		}
		modPaiList.add(modwan);
		modPaiList.add(modtong);
		modPaiList.add(modsuo);
		modPaiList.add(ZiPai);
		FormedPaiList.add(donewan);
		FormedPaiList.add(donetong);
		FormedPaiList.add(donesuo);
		List<List<String>> temp = preprocess(modPaiList);
		discard = analyze_lichi(temp);
		//System.out.println(max);
		this.winrate = this.winrate * max;
		return discard;
	}
	public String analyze_qiduizi() {
		double totalprob = 1;
		String discard = "";
		List<Double> winrates = new ArrayList<>();
		List<String> visited = new ArrayList<>();
		double minprob = 1.0;
		for(List<String> l : HandPaiList) {
			double prob = 0.0;
			for(String s:l) {
				if(visited.contains(s)) continue;
				int needforduizi = 2;
				for(int i = 0; i < l.size();i++) {
					if (l.get(i).equals(s)) needforduizi--;
				}
				visited.add(s);
				if(needforduizi == 1) prob = probforpai(s,1);
				else if (needforduizi == 0) prob = 1.0;
				else if (needforduizi < 0) {prob = 0.0;winrates.add(1.0);}
				winrates.add(prob);
				if(prob < minprob) {
					minprob = prob;
					discard = s;
				}
			}
		}
		Collections.sort(winrates);
		double chance = 1.0;
		for(int i = 0 ;i < 7;i++) {
			chance = chance * winrates.get(winrates.size()-1-i);
		}
		this.winrate = chance;
		return discard;
	}
	public String analyze_yibeikou() {
		double totalprob = 1;
		String discard = "";
		double winrates[] = new double[21];
		int index = 0;
		int need1 = 2;
		int need2 = 2;
		int need3 = 2;
		double prob1 = 1.0;
		double prob2 = 1.0;
		double prob3 = 1.0;
		for(int i = 1 ; i<= 7;i++) {
			for(String s: WanPai) {
				if (s.equals(i+"m")) need1--; 
				if (s.equals((i+1)+"m")) need2--;
				if (s.equals((i+2)+"m")) need3--;
			}
			prob1 = need1 <= 0 ? 1.0 : probforpai(i+"m",need1);
			prob2 = need2 <= 0 ? 1.0 : probforpai((i+1)+"m",need2);
			prob3 = need3 <= 0 ? 1.0 : probforpai((i+2)+"m",need3);
			winrates[index++] = prob1 * prob2 * prob3;
			need1 = 2; need2 = 2; need3 = 2;
			
			for(String s: TongPai) {
				if (s.equals(i+"p")) need1--; 
				if (s.equals((i+1)+"p")) need2--;
				if (s.equals((i+2)+"p")) need3--;
			}
			prob1 = need1 <= 0 ? 1.0 : probforpai(i+"p",need1);
			prob2 = need2 <= 0 ? 1.0 : probforpai((i+1)+"p",need2);
			prob3 = need3 <= 0 ? 1.0 : probforpai((i+2)+"p",need3);
			winrates[index++] = prob1 * prob2 * prob3;
			need1 = 2; need2 = 2; need3 = 2;
			
			for(String s: SuoPai) {
				if (s.equals(i+"s")) need1--; 
				if (s.equals((i+1)+"s")) need2--;
				if (s.equals((i+2)+"s")) need3--;
			}
			prob1 = need1 <= 0 ? 1.0 : probforpai(i+"s",need1);
			prob2 = need2 <= 0 ? 1.0 : probforpai((i+1)+"s",need2);
			prob3 = need3 <= 0 ? 1.0 : probforpai((i+2)+"s",need3);
			winrates[index++] = prob1 * prob2 * prob3;
		}
		need1 = 2; need2 = 2; need3 = 2;
		double maxprob = 0.0;
		String type = "";
		int painum = 0;
		for(int i = 0; i < winrates.length; i++) {
			if(winrates[i] > maxprob) {
				maxprob = winrates[i];
				int temp = i % 3;
				type = temp == 0 ? "m" : temp == 1 ? "p" : "s";
				painum = i/3 + 1;
			}
		}
		msg = "Need combo:" +painum+type+(painum+1)+type+(painum+2)+type;
		
		List<String> shunzi1 = new ArrayList();
		List<String> shunzi2 = new ArrayList();
		List<String> modwan = new ArrayList();
		List<String> modtong = new ArrayList();
		List<String> modsuo = new ArrayList();
		List<List<String>> modList = new ArrayList<>();
		for(String s:WanPai) {
			if(type.equals("m")) {
				if(s.equals(painum+type)) {
					if(need1 <= 0) modwan.add(s);
					else need1--;;
				}
				if(s.equals((painum+1)+type)) {
					if(need2 <= 0) modwan.add(s);
					else need2--;;
				}
				if(s.equals((painum+1)+type)) {
					if(need3 <= 0) modwan.add(s);
					else need3--;;
				}
			}
			else modwan.add(s);
		}
		need1 = 2; need2 = 2; need3 = 2;
		for(String s:TongPai) {
			if(type.equals("p")) {
				if(s.equals(painum+type)) {
					if(need1 <= 0) modtong.add(s);
					else need1--;;
				}
				if(s.equals((painum+1)+type)) {
					if(need2 <= 0) modtong.add(s);
					else need2--;;
				}
				if(s.equals((painum+1)+type)) {
					if(need3 <= 0) modtong.add(s);
					else need3--;;
				}
			}
			else modtong.add(s);
		}
		need1 = 2; need2 = 2; need3 = 2;
		for(String s:SuoPai) {
			if(type.equals("s")) {
				if(s.equals(painum+type)) {
					if(need1 <= 0) modsuo.add(s);
					else need1--;;
				}
				if(s.equals((painum+1)+type)) {
					if(need2 <= 0) modsuo.add(s);
					else need2--;;
				}
				if(s.equals((painum+1)+type)) {
					if(need3 <= 0) modsuo.add(s);
					else need3--;;
				}
			}
			else modsuo.add(s);
		}
		FormedPaiList.add(shunzi1);FormedPaiList.add(shunzi2);
		modList.add(modwan);
		modList.add(modtong);
		modList.add(modsuo);
		modList.add(ZiPai);
		List<List<String>> temp = preprocess(modList);
		discard = analyze_lichi(temp);
		this.winrate = maxprob * this.winrate;
		return discard;
	}
	public String analyze_duiduihu() {
		String discard = "";
		List<List<String>> modList = preprocess_Duiduihu();
		List<String> visited = new ArrayList();
		String discardquetou = "";
		double quetourate  = 1.0;
		List<Double> winrates = new ArrayList<>();
		List<Double> quetourates = new ArrayList<>();
		int times = 4-FormedPaiList.size()-mjpai.ming_0/3;
		double minrate = 1.0;
		for(List<String> l: modList) {
			for(String s: l) {
				int painum = Integer.parseInt(String.valueOf(s.charAt(0)));
				String type = String.valueOf(s.charAt(1));
				double prob = probformKezi(l,painum,type);
				if(prob < minrate) {minrate = prob; discard = s;}
				if(!visited.contains(s)) {visited.add(s); winrates.add(prob);}
				double currquetourate = probforpai(s,1);
				quetourates.add(currquetourate);
				if(currquetourate < quetourate) {
					quetourate = currquetourate;
					discardquetou = s;
				}
			}
		}
		double chance = 1.0;
		if(times == 0) {
			Collections.sort(quetourates);
			this.winrate = quetourates.get(quetourates.size()-1);
			return discardquetou;
		}else {
			Collections.sort(winrates);
			for(int i = 0; i < times; i++) {
				chance = chance * winrates.get(winrates.size()-1-i);
			}
			this.winrate = chance; 
		}	
		return discard;
	}
}
