package utils;

import java.util.ArrayList;
import java.util.List;

public class SplitMessage {

    public static List<String> splitGPTMessage(String msg) {
        if (msg.contains("```")) {
            return splitGPTMessageWithCode(msg);
        } else {
            return splitGPTMessageWithoutCode(msg);
        }
    }

    private static String[] formatGPTMessageWithCode(String msg) {
        String[] arrWords = msg.replaceAll("`{3}", "~```").split("~");
        for (int i = 0; i < arrWords.length; i++) {
            if (i % 2 != 0) {
                arrWords[i] += "\n```";
            } else {
                arrWords[i] = arrWords[i].replaceAll("```", "");
            }
        }
        return arrWords;
    }

    private static List<String> splitGPTMessageWithCode(String msg) {

        String[] arrWords = formatGPTMessageWithCode(msg);
        List<String> arrPhrases = new ArrayList<>();

        StringBuilder stringBuffer = new StringBuilder();
        int cnt = 0;
        int index = 0;
        int length = arrWords.length;

        while (index != length) {
            if (cnt + arrWords[index].length() <= 1900) {
                cnt += arrWords[index].length() + 1;
                stringBuffer.append(arrWords[index]);
                index++;
            } else {
                arrPhrases.add(stringBuffer.toString());
                stringBuffer = new StringBuilder();
                cnt = 0;
            }
        }
        if (stringBuffer.length() > 0) {
            arrPhrases.add(stringBuffer.toString());
        }
        return arrPhrases;
    }

    private static List<String> splitGPTMessageWithoutCode(String msg) {

        String[] arrWords = msg.split("\n");
        List<String> arrPhrases = new ArrayList<>();

        StringBuilder stringBuffer = new StringBuilder();
        int cnt = 0;
        int index = 0;
        int length = arrWords.length;

        while (index != length) {
            if (cnt + arrWords[index].length() <= 1900) {
                cnt += arrWords[index].length() + 1;
                stringBuffer.append(arrWords[index]);
                index++;
            } else {
                arrPhrases.add(stringBuffer.toString());
                stringBuffer = new StringBuilder();
                cnt = 0;
            }
        }
        if (stringBuffer.length() > 0) {
            arrPhrases.add(stringBuffer.toString());
        }
        return arrPhrases;
    }
}