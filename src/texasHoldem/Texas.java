package texasHoldem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Texas {

	public enum cardType {
		None, High_Card, Pair, Two_Pairs, Three_of_a_Kind, Straight, Flush, Full_House, Four_of_a_Kind, Straight_Flush;
	}
	
//	public static void main(String[] args) {
//		Texas texas = new Texas();
//		texas.judge("2S 8S AS QS 3S");
//	}
	
	public Map<cardType, ArrayList<Integer>> judge(String str) {

		String[] strSplit = str.split(" ");
		Integer[] numarr = new Integer[strSplit.length];
		
		int index = 0;
		for (String string : strSplit) {
			 char rawChar = string.toCharArray()[0];
			 Integer cookedChar = ConvertLetterTONum(rawChar);
			 numarr[index] = cookedChar;
			index++;
		}
		
		//map集合，key为字符，value为个数
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
		
		for(Integer c:numarr){			
			if (!map.containsKey(c)) {
				map.put(c, 1);
            } else {
            	Integer count = map.get(c) + 1;
                map.put(c, count);
            }
		}
		
		map = sortMapByValue(map);	

		index = 1;
		int maxNum = 0;
		int maxKey = 0;
		ArrayList<Integer> compareArray = new ArrayList<>();
		cardType type = cardType.None;
		
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			if (index == 1) {
				maxNum = entry.getValue();
				maxKey = entry.getKey();
			}
			if (entry.getValue() == 4 &&maxNum == 4) {
				type = cardType.Four_of_a_Kind;
				compareArray.add(maxKey);
			}
			if (entry.getValue() == 2 && maxNum == 3) {
				type = cardType.Full_House;
				compareArray.add(maxKey);
			}
			if (entry.getValue() == 1 && maxNum == 3) {
				type = cardType.Three_of_a_Kind;
				compareArray.add(entry.getKey());
			}
			if (entry.getValue() == 1 && maxNum == 1) {
				type = cardType.High_Card;
				compareArray.add(entry.getKey());
			}
			
			index++;
			
//			System.out.println("Key: " + entry.getKey() + "\tValue: " + entry.getValue());
		}
		
		Map<cardType, ArrayList<Integer>> result = new TreeMap<cardType, ArrayList<Integer>>();
		result.put(type, compareArray);
		
		return result;
	}

	private Integer ConvertLetterTONum(char rawChar) {

		switch (rawChar) {
		case 'T':
			return 10;
		case 'J':
			return 11;
		case 'Q':
			return 12;
		case 'K':
			return 13;
		case 'A':
			return 14;

		default:
			return Integer.parseInt(String.valueOf(rawChar));
		}
	}
	
	private String convertNumToCard(int num) {
		
		switch (num) {
		case 10:
			return "10";
		case 11:
			return "Jack";
		case 12:
			return "Queen";
		case 13:
			return "King";
		case 14:
			return "Ace";
		default:
			return String.valueOf(num);
		}
	}

	public String compare(String black, String white) {

		String result = "";
		
		Map<cardType, ArrayList<Integer>> blackResult = judge(black);
		Map<cardType, ArrayList<Integer>> whiteResult = judge(white);
		
		cardType blackCardType = cardType.None;
		ArrayList<Integer> blackCompareArrayList = new ArrayList<Integer>();
		cardType whiteCardType = cardType.None;
		ArrayList<Integer> whiteCompareArrayList = new ArrayList<Integer>();
		
		for (Map.Entry<cardType, ArrayList<Integer>> entry : blackResult.entrySet()) {
        	blackCardType = entry.getKey();
        	blackCompareArrayList = entry.getValue();
        }		
		for (Map.Entry<cardType, ArrayList<Integer>> entry : whiteResult.entrySet()) {
        	whiteCardType = entry.getKey();
        	whiteCompareArrayList = entry.getValue();
        }
		
		if (blackCardType == cardType.None || whiteCardType == cardType.None || blackCompareArrayList.size() == 0 || whiteCompareArrayList.size() == 0) {
			return "error!";
		}
		
		Integer[] blackArr = new Integer[blackCompareArrayList.size()];
		blackCompareArrayList.toArray(blackArr);
		Arrays.sort(blackArr);
		Integer[] whiteArr = new Integer[whiteCompareArrayList.size()];
		whiteCompareArrayList.toArray(whiteArr);
		Arrays.sort(whiteArr);
		
		if (blackCardType.compareTo(whiteCardType) < 0) {
			result += "White wins - ";
			result += whiteCardType.toString().replace('_', ' ');
		}
		else if (blackCardType.compareTo(whiteCardType) > 0) {
			result += "Black wins - ";
			result += blackCardType.toString().replace('_', ' ');
		}
		else {
			int iterNum = blackArr.length;
			boolean endFlag = false;
			while(iterNum-- > 0){
				if (blackArr[iterNum] > whiteArr[iterNum]) {
					result += "Black wins - high card: ";
					result += convertNumToCard(blackArr[iterNum]);
					endFlag = true;
					break;
				}
				else if (blackArr[iterNum] < whiteArr[iterNum]) {
					result += "White wins - high card: ";
					result += convertNumToCard(whiteArr[iterNum]);
					endFlag = true;
					break;
				}
			}
			if (!endFlag) {
				result += "Tie";
			}
		}
		
		return result;
	}

	public Map<Integer, Integer> sortMapByValue(Map<Integer, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        
        Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        List<Entry<Integer,Integer>> entryList = new ArrayList<Map.Entry<Integer, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        for (Map.Entry<Integer, Integer> entry : entryList) {
        	sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
	
	public static void mapPrint(Map<Integer, Integer> map) {
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			System.out.println("Key: " + entry.getKey() + "\tValue: " + entry.getValue());
		}
	}
	
	class MapValueComparator implements Comparator<Map.Entry<Integer, Integer>> {

	    @Override
	    public int compare(Entry<Integer, Integer> me1, Entry<Integer, Integer> me2) {

	        return me2.getValue().compareTo(me1.getValue());
	    }
	}
	
	class MyIntegerComparator implements Comparator<Integer> {
		 
	    public int compare(Integer n1, Integer n2) {
	        return n2 - n1;
	    }
	 
	}
}
