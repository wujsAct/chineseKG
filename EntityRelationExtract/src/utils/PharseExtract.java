package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PharseExtract {
	public static ArrayList<ArrayList<String>> EntityExtract(String tags,ArrayList<String> tokens){
		ArrayList<ArrayList<String>> retEnts = new ArrayList<ArrayList<String>>();
		
		String pattern = "[14]*0+[14]*0*"; //consider the continuous noun phrase
		boolean isMatch = Pattern.matches(pattern, tags);
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(tags);
		String str = null,mention=null;Integer start = 0,end=0; 
		ArrayList<String> item;
		while (m.find()) {
			str = m.group();
			start = m.start();
			end = m.end();
			mention = String.join("",tokens.subList(start, end));
			item = new ArrayList<String>();
			item.add(mention);item.add(Integer.toString(start));item.add(Integer.toString(end));
			System.out.println("Ent: "+str+"\t"+start+"\t"+end+"\t"+mention);
			retEnts.add(item);
		} 
		return retEnts;
	}
	
	
	public static ArrayList<ArrayList<String>> RelExtract(String tags,ArrayList<String> tokens){
		ArrayList<ArrayList<String>> retRels = new ArrayList<ArrayList<String>>();
		
		String pattern = "3*2+3*2*";  //consider the continuous verb phrase
		boolean isMatch = Pattern.matches(pattern, tags);
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(tags);
		String str = null,mention=null;Integer start = 0,end=0;
		ArrayList<String> item;
		while (m.find()) {
			str = m.group();
			start = m.start();
			end = m.end();
			mention = String.join("",tokens.subList(start, end));
			item = new ArrayList<String>();
			item.add(mention);item.add(Integer.toString(start));item.add(Integer.toString(end));
			System.out.println("Rel:"+str+"\t"+start+"\t"+end+"\t"+mention);
			retRels.add(item);
		} 
		return retRels;
	}
}
