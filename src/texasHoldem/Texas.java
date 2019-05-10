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
import java.util.Vector;

public class Texas {

	public static int maxCardSize = 5;
	
	public enum cardType {
		None, High_Card, Pair, Two_Pairs, Three_of_a_Kind, Straight, Flush, Full_House, Four_of_a_Kind, Straight_Flush;
	}
	
	public Vector<Object> judge(String str) {

		boolean straightFlag = false;
		
		String[] strSplit = str.split(" ");
		Integer[] numarr = new Integer[strSplit.length];
		Character[] suitArr = new Character[strSplit.length];
		
		//	make numarr
		int index = 0;
		for (String string : strSplit) {
			 char rawChar = string.toCharArray()[0];
			 Integer cookedChar = ConvertLetterTONum(rawChar);
			 numarr[index] = cookedChar;
			index++;
		}
		
		// make suitArr
		index = 0;
		for (String string : strSplit) {
			 char rawChar = string.toCharArray()[1];
			 suitArr[index] = rawChar;
			index++;
		}
		
		// find straight
		char baseChar = suitArr[0];
		for (int i = 1; i < suitArr.length; i++) {
			if (suitArr[i] != baseChar) {
				break;
			}
			// 5 cards are the same suit
			if (i == suitArr.length-1) {
				straightFlag = true;
			}
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
		boolean endFlag = false;
		int lastCard = 0;
		ArrayList<Integer> compareArray = new ArrayList<>();
		ArrayList<Integer> secondArray = new ArrayList<>();
		cardType type = cardType.None;
		
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			if (index == 1) {
				maxNum = entry.getValue();
				lastCard = entry.getKey();
			}
			if (entry.getValue() == maxNum) {
				compareArray.add(entry.getKey());
			}else{
				secondArray.add(entry.getKey());
			}
			
			if (maxNum == 4) {
				type = cardType.Four_of_a_Kind;
			}
			if (entry.getValue() == 2 && maxNum == 3) {
				type = cardType.Full_House;
			}
			if (entry.getValue() == 1 && maxNum == 3) {
				type = cardType.Three_of_a_Kind;
			}
			if (entry.getValue() == 1 && maxNum == 2) {
				if (index == 2 && endFlag == false) {
					type = cardType.Pair;
					endFlag = true;
				}
				if (index == 3 && endFlag == false) {
					type = cardType.Two_Pairs;
					endFlag = true;
				}
			}
			if (entry.getValue() == 1 && maxNum == 1) {
				if (index > 1 && entry.getKey() != lastCard+1) {
					type = cardType.High_Card;					
				}
				if (index != 1 && entry.getKey() == lastCard+1) {
					lastCard = entry.getKey();
				}
				if (index == maxCardSize && entry.getKey() == lastCard) {
					if (straightFlag) {
						type = cardType.Straight_Flush;												
					}else {
						type = cardType.Flush;
					}
				}
			}
			
			index++;
			
		}
		
		if (type != cardType.Straight_Flush && straightFlag) {
			type = cardType.Straight;
		}
		
		Vector<Object> result = new Vector<Object>(2);
		result.addElement(type);
		result.addElement(compareArray);
		result.addElement(secondArray);
		
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

	@SuppressWarnings("unchecked")
	public String compare(String black, String white) {

		String result = "";
		
		Vector<Object> blackResult = judge(black);
		Vector<Object> whiteResult = judge(white);
		
		cardType blackCardType = cardType.None;
		ArrayList<Integer> blackCompareArrayList = new ArrayList<Integer>();
		ArrayList<Integer> blackSecondArrayList = new ArrayList<Integer>();
		cardType whiteCardType = cardType.None;
		ArrayList<Integer> whiteCompareArrayList = new ArrayList<Integer>();	
		ArrayList<Integer> whiteSecondArrayList = new ArrayList<Integer>();
		
    	blackCardType = (cardType) blackResult.elementAt(0);
    	blackCompareArrayList = (ArrayList<Integer>) blackResult.elementAt(1);
    	blackSecondArrayList = (ArrayList<Integer>) blackResult.elementAt(2);
    	whiteCardType = (cardType) whiteResult.elementAt(0);
    	whiteCompareArrayList = (ArrayList<Integer>) whiteResult.elementAt(1);
    	whiteSecondArrayList = (ArrayList<Integer>) whiteResult.elementAt(2);
		
		if (blackCardType == cardType.None || whiteCardType == cardType.None || blackCompareArrayList.size() == 0 || whiteCompareArrayList.size() == 0) {
			return "error!";
		}
		
		Integer[] blackArr = new Integer[blackCompareArrayList.size()];
		blackCompareArrayList.toArray(blackArr);
		Arrays.sort(blackArr);
		
		Integer[] blackSecondArr = new Integer[blackSecondArrayList.size()];
		blackSecondArrayList.toArray(blackSecondArr);
		Arrays.sort(blackSecondArr);
		
		Integer[] whiteArr = new Integer[whiteCompareArrayList.size()];
		whiteCompareArrayList.toArray(whiteArr);
		Arrays.sort(whiteArr);
		
		Integer[] whiteSecondArr = new Integer[whiteSecondArrayList.size()];
		whiteSecondArrayList.toArray(whiteSecondArr);
		Arrays.sort(whiteSecondArr);
		
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
					result += "Black wins - ";
					result += blackCardType.toString().replace('_', ' ');
					result += ": ";
					result += convertNumToCard(blackArr[iterNum]);
					endFlag = true;
					break;
				}
				else if (blackArr[iterNum] < whiteArr[iterNum]) {
					result += "White wins - ";
					result += whiteCardType.toString().replace('_', ' ');
					result += ": ";
					result += convertNumToCard(whiteArr[iterNum]);
					endFlag = true;
					break;
				}
			}
			if (!endFlag) {
				int iterNum2 = blackSecondArr.length;
				while(iterNum2-- > 0){
					if (blackSecondArr[iterNum2] > whiteSecondArr[iterNum2]) {
						result += "Black wins - ";
						result += blackCardType.toString().replace('_', ' ');
						result += ": ";
						result += convertNumToCard(blackSecondArr[iterNum2]);
						endFlag = true;
						break;
					}
					else if (blackSecondArr[iterNum2] < whiteSecondArr[iterNum2]) {
						result += "White wins - ";
						result += whiteCardType.toString().replace('_', ' ');
						result += ": ";
						result += convertNumToCard(whiteSecondArr[iterNum2]);
						endFlag = true;
						break;
					}
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
