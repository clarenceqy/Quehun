import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MJPai {
	
	List<String> myhandPai;
	List<String> discard_me;
	List<String> ming_me;
	List<String> discard_other;
	List<String> ming_other;
	List<String> additionalzhengting;
	HashMap<String,Integer> remaining_tiles;
	int remaining = 1;
	int ming_0,ming_1 = 0;
	
	MJPai(){
		myhandPai = new ArrayList<>();
		discard_me = new ArrayList<>();
		ming_me = new ArrayList<>();
		discard_other = new ArrayList<>();
		ming_other = new ArrayList<>();
		additionalzhengting = new ArrayList<>();
		remaining_tiles = new HashMap<>();
	}
	
	
	public void update_myPai(String handpai[]) {
		myhandPai.clear();
		for(String s: handpai) {
			myhandPai.add(s);
		}
		
	}
	
	public void update_myQiPai(String qipai[]) {
		discard_me.clear();
		for(String s: qipai) {
			discard_me.add(s);
		}
	}
	
	public void update_otherQiPai(String qipai1[], String qipai2[], String qipai3[]) {
		discard_other.clear();
		for(String s: qipai1) {
			discard_other.add(s);
		}
		for(String s: qipai2) {
			discard_other.add(s);
		}
		for(String s: qipai3) {
			discard_other.add(s);
		}
	}
	
	public void update_myming(String[] ming_0s) {
		if(ming_0 == ming_0s.length) return;
		ming_me.clear();
		for(String s: ming_0s) {
			ming_me.add(s);
		}
		ming_0 = ming_0s.length;
	}
	
	public void update_otherming(String[] ming_1s,String[] ming_2s,String[] ming_3s) {
		int total = ming_1s.length + ming_2s.length + ming_3s.length;
		if(ming_1 == total) return;
		ming_other.clear();
		for(String s: ming_1s) {
			ming_other.add(s);
		}
		for(String s: ming_2s) {
			ming_other.add(s);
		}
		for(String s: ming_3s) {
			ming_other.add(s);
		}
		ming_1 = total;
	}
	
	public void populatemap() {
		String type[] = {"m","p","s"};
		for(int i = 1; i <= 9; i++) {
			for(int j = 0; j < 3;j++) {
				remaining_tiles.put(i+type[j], 4);
			}
		}
		for(int i = 1; i <=7; i++) {
			remaining_tiles.put(i+"z", 4);
		}
	}
	
	public void update_All(int remaining,String handpai[],String qipai[],String qipai1[], String qipai2[], String qipai3[], String[] ming_0s,String[] ming_1s,String[] ming_2s,String[] ming_3s) {
		update_myPai(handpai);
		update_myQiPai(qipai); 
		update_otherQiPai(qipai1,qipai2,qipai3);
		update_myming(ming_0s);
		update_otherming(ming_1s,ming_2s,ming_3s);
		this.remaining = remaining;
		remaining_tiles.clear();
		populatemap();
		for(String s: handpai) {remaining_tiles.put(s, remaining_tiles.get(s) - 1);}
		for(String s: qipai) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		for(String s: qipai1) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		for(String s: qipai2) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		for(String s: qipai3) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		for(String s: ming_0s) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		for(String s: ming_1s) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		for(String s: ming_2s) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		for(String s: ming_3s) remaining_tiles.put(s, remaining_tiles.get(s) - 1);
		//System.out.print(remaining_tiles.get("1z"));
	}
	
	public double prob_pai(List<String> pais) {
		int ispai = 0;
		for(String s: pais) {
			ispai += remaining_tiles.get(s);
		}
		return ispai/remaining;
	}
	
	public void print_myhandpai() {
		for(String s: myhandPai) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	public void print_myqipai() {
		for(String s: discard_me) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
}
