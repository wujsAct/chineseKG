package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

public class ChineseNLPUtils {

	public static ArrayList<ArrayList<ArrayList<String>>> getChineseTag(
			List<CoreMap> sentences) {

		ArrayList<ArrayList<String>> tokenArray = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> tokenIndexArray = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> posArray = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> neArray = new ArrayList<ArrayList<String>>();
		ArrayList<String> tokens;
		ArrayList<String> tokenIndexs;
		ArrayList<String> poses;
		ArrayList<String> nes;

		Integer sentId = 0;
		ArrayList<ArrayList<String>> depArray = new ArrayList<ArrayList<String>>();
		for (CoreMap sentence : sentences) {
			ArrayList<String> depItems = new ArrayList<String>();
			SemanticGraph deps = sentence
					.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
			List<SemanticGraphEdge> depList = deps.edgeListSorted();
			
			String depSource,depTarget;
			for (SemanticGraphEdge dep : depList) {
				depSource = dep.getSource().word()+"\t"+dep.getSource().index();
				depTarget = dep.getTarget().word()+"\t"+dep.getTarget().index();
				depItems.add(depSource+"\t\t"+dep.getRelation()+"\t\t"+depTarget);	
				System.out.println(dep.getSource()
						+ "\t" + dep.getRelation()
						+ "\t" + dep.getTarget());
			}
			depArray.add(depItems);
			// System.out.println(deps);
			System.out.println("------------------");

			tokens = new ArrayList<String>();
			tokenIndexs = new ArrayList<String>();
			poses = new ArrayList<String>();
			nes = new ArrayList<String>();
			String newString = "";
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);

				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				Integer start = token
						.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
				Integer end = token
						.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
				tokenIndexs.add(start + "_" + end);
				// newString += word+":"+start+" ";
				String ne = token.get(NamedEntityTagAnnotation.class);
				// System.out.println(word + "\t" + start + "\t" +
				// end+"\t"+pos+"\t"+ne);
				tokens.add(word);
				poses.add(pos);
				nes.add(ne);
			}
			tokenArray.add(tokens);
			tokenIndexArray.add(tokenIndexs);
			posArray.add(poses);
			newString = newString.trim();
			// System.out.println(sentId+": "+newString.length()+":"+newString);
			// System.out.println("--------------------------------");
			sentId += 1;
		}

		ArrayList<ArrayList<ArrayList<String>>> retParams = new ArrayList<ArrayList<ArrayList<String>>>();
		retParams.add(tokenArray);
		retParams.add(posArray);
		retParams.add(tokenIndexArray);
		retParams.add(depArray);
		return retParams;

	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, JSONException {
		/**
		 * String text = "《疑情别恋》（英文：Love Exchange），香港电视广播有限公司时装电视剧，以高清技术拍摄。" +
		 * "此剧为2008年节目巡礼剧集之一，由苗侨伟及袁咏仪领衔主演，由田蕊妮及陈国邦联合主演，并由关礼杰及汤盈盈特别演出，监制黄伟声。";
		 ***/

		String text = "李岚清（1932年5月22日－），江苏镇江人；毕业于复旦大学企业管理系；中国共产党和中华人民共和国前主要领导人之一。"
				+ "1952年9月加入中国共产党，是中共第十三届中央候补委员，第十四届中央委员、中央政治局委员，第十五届中央政治局常委；曾任国务院副总理，对外经济贸易部部长";

		/**
		 * String text =
		 * "人类免疫缺乏病毒（human immunodeficiency virus，缩写为HIV）是一种感染人类免疫系统细胞的慢病毒，属反转录病毒的一种。普遍认为，人类免疫缺陷病毒的感染导致艾滋病，"
		 * + "艾滋病是后天性细胞免疫功能出现缺陷而导致严重随机感染及/或继发肿瘤并致命的一种疾病。" +
		 * "艾滋病自1981年在美国被识别并发展为全球大流行至2003年底，已累计导致两千余万人死亡。人类免疫缺陷病毒通常也俗称为「艾滋病病毒」或「艾滋病毒」。"
		 * ;
		 **/

		String dirPath = "D:/Users/DELL/Workspaces/MyEclipse Professional 2014/protobuf/";
		Properties props = new Properties();
		props.load(new FileInputStream(dirPath
				+ "src/StanfordCoreNLP-chinese.properties"));
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Writer datas = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("data/out"), false), "UTF8"));
		for (String line : IOUtils.readLines("data/person.des")) {
			JSONArray jsons = new JSONArray();
			JSONObject jsonObj = new JSONObject(line);
			JSONArray rawDatas = jsonObj.getJSONArray("content");
			
			for (Integer i = 0; i < rawDatas.length(); i++) {
				
				text = rawDatas.get(i).toString();
				Annotation document = new Annotation(text);

				pipeline.annotate(document);
				List<CoreMap> sentences = document
						.get(SentencesAnnotation.class);
				ArrayList<ArrayList<ArrayList<String>>> retParams = new ArrayList<ArrayList<ArrayList<String>>>();
				retParams = ChineseNLPUtils.getChineseTag(sentences);

				ArrayList<ArrayList<String>> tokenArray = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> tokenIndexArray = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> posArray = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> neArray = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> depArray = new ArrayList<ArrayList<String>>();
				tokenArray = retParams.get(0);
				posArray = retParams.get(1);
				depArray = retParams.get(3);
				/**
				 * @2017/7/27 we utilize the pattern to generate the NER
				 */
				Integer senti = 0;

				for (ArrayList<String> posList : posArray) {
					ArrayList<String> sent = tokenArray.get(senti);
					String tagIds = "";
					for (String pos : posList) {
						tagIds += PosTag2id.is_which_pos(pos);
					}
					System.out.println(tagIds);
					ArrayList<ArrayList<String>> ents = PharseExtract
							.EntityExtract(tagIds, sent);
					ArrayList<ArrayList<String>> rels = PharseExtract
							.RelExtract(tagIds, sent);
					JSONArray sentData = new JSONArray();
					sentData.put(sent);
					sentData.put(ents);
					sentData.put(rels);
					System.out.println(senti);
					sentData.put(depArray.get(senti));
					jsons.put(sentData);
					
					System.out.println(sentData);
					System.out.println("--------------");
					senti += 1;
				}
			}
			datas.write(jsons.toString() + "\n");
		}
		datas.close();
	}
}
