package utils;

public class PosTag2id {
	/**
	 * @atuhtor wujs
	 * @we utilize the mapping 
	 * @param tag
	 * @return
	 */
	public static boolean is_noun(String tag){
		return tag.contains("N");
	}
	
	public static boolean is_verb(String tag){
		return tag.contains("V");
	}
	
	public static boolean is_quantifier(String tag){
		return tag.contains("OD") || tag.contains("M") || tag.contains("LC");
	}
	
	public static boolean is_adverb(String tag){
		return tag.contains("AD");
	}
	
	public static boolean is_adjective(String tag){
		return tag.contains("J");
	}
	
	public static String is_which_pos(String tag){
		if (PosTag2id.is_noun(tag)){
			return "0";
		}else if(PosTag2id.is_adjective(tag)){
			return "1";
		}else if(PosTag2id.is_verb(tag)){
			return "2";
		}else if(PosTag2id.is_adverb(tag)){
			return "3";
		}
		else if(PosTag2id.is_quantifier(tag)){
			return "4";
		}
		else{
			return "-";
		}
	}
}
