package utils.text;

import analyzer.BaseDict;
import analyzer.StaticTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by eleven on 3/20/14.
 */
/**
 * TextTool use for parser text -> word Array and get Sentence that contains a
 * specified word
 */
public class TextTool {

    private String content = "";
    private String original;

    public TextTool(String content) {
        this.original = content;
        boolean startSentence = true;
        int i = 0;
        StringBuilder builder = new StringBuilder();
        while (i < content.length()) {
            char c = content.charAt(i);
            // if a word start by letter -> get word
            if (Character.isLetterOrDigit(c)) {
                int start = i;
                boolean drop = false;
                if (Character.isDigit(c) || (Character.isUpperCase(c) && !startSentence)) {
                    drop = true;
                }
                do {
                    i++;
                    if (i >= content.length()) {
                        break;
                    }
                    c = content.charAt(i);
                    // if word contains '.' and followed by a not LetterOrDigit char, break
                    if (c == '.' || c == '-' || c == '\'' || c == '_') {
                        if (i == (content.length() - 1) || !Character.isLetterOrDigit(content.charAt(i + 1))) {
                            break;
                        }
                    }
                    // if word contains '_' or digit, drop word
                    if (c == '_' || c == '.' || Character.isDigit(c)) {
                        drop = true;
                    }
                } while (Character.isLetterOrDigit(c) || c == '.' || c == '\'' || c == '-' || c == '_');

                // if word and with a not letter, drop word
                if (!Character.isLetter(content.charAt(i - 1))) {
                    drop = true;
                }

                if (drop) {
                    for (int k = start; k < i; k++) {
                        builder.append(' ');
                    }
                } else {
                    builder.append(content.substring(start, i));
                }
            } else {
                if (startSentence) {
                    startSentence = false;
                }

                if (c == '.' || c == '?' || c == '!') {
                    startSentence = true;
                    builder.append('|');
                } else if (c == '\n' && (i == content.length() - 1 || Character.isUpperCase(content.charAt(i + 1)))) {
                    startSentence = true;
                    builder.append('|');
                } else {
                    builder.append(' ');
                }

                i++;
            }
        }

        this.content = builder.toString().toLowerCase();
    }

    public String getOriginal() {
        return original;
    }
    /*
     sau khi file -> list word
     hiện tại đang add vào string list
     */

    public void addToDataBase() {
        StringBuilder builder = new StringBuilder();
        String tmp = content.replace("|", " ");
        if (tmp.length() > 0) {
            builder.append(tmp.charAt(0));
        }
        for (int i = 1; i < tmp.length(); i++) {
            if (tmp.charAt(i - 1) == ' ' && tmp.charAt(i) == ' ') {
            } else {
                builder.append(tmp.charAt(i));
            }
        }

        String[] str = builder.toString().split(" ");  // split string into String array

        ArrayList<String> strList = new ArrayList<String>(Arrays.asList(str));
        Collections.sort(strList);						// sort list first        

        ArrayList<Word> wordList = new ArrayList<Word>();       // wordlist contains list words in ascending order
        String last = "";
        

        for (int i = 0; i < strList.size(); i++) {
            String word = strList.get(i);
            if (word.length() < 2) {
                continue;  // throw word which of length is 1 (contains case common words)
            }
            if (Character.isLetter(word.charAt(0))) {
                if (last.compareTo(word) == 0) {
                    wordList.get(wordList.size() - 1).adjust();
                } else {
                    last = word;
                    wordList.add(new Word(word));
                 //  wordanalyzer.WordAnalyzer.keyList.add(word);
                    if (wordList.size() >= 2) {
                        Word tmpWord = wordList.get(wordList.size() - 2);
                       // if(tmpWord.getWord() == "as"){
                            System.out.println(tmpWord.getWord()+ tmpWord.getFrequency());
                        //}
                        wordanalyzer.WordAnalyzer.staticTable.Addi(tmpWord.getWord(), tmpWord.getFrequency(),wordanalyzer.WordAnalyzer.baseData);

                    }
                }
            }
        }

    }

    public String[] getSentence(String word) {
        int index = 0;
        ArrayList<String> sentences = new ArrayList<String>();
        word = word.toLowerCase();

        while (index + word.length() < content.length()) {
            index = content.indexOf(word, index);
            if (index == -1) {
                break;
            }
            // No suitable, continue find
            if ((index != 0 && Character.isLetter(content.charAt(index - 1))) || ((index + word.length() <= content.length() - 1) && Character.isLetter(content.charAt(index + word.length())))) {
                index++;
                continue;
            }

            // suitable, mark sentence
            int start, end;
            int countWord = 1;
            boolean inWord = false;
            for (start = index - 1; start >= 0; start--) {
                char c = content.charAt(start);
                if (c == '|') {
                    break;
                } else if (c == ' ') {
                    if (inWord) {
                        inWord = false;
                    }
                } else {
                    if (!inWord) {
                        countWord++;
                        inWord = true;
                    }
                }
            }
            start++;
            for (end = index + word.length(); end < content.length(); end++) {
                char c = content.charAt(end);
                if (c == '|') {
                    break;
                } else if (c == ' ') {
                    if (inWord) {
                        inWord = false;
                    }
                } else {
                    if (!inWord) {
                        countWord++;
                        inWord = true;
                    }
                }
            }
            String sentence = trimString(original.substring(start, end));
            // drop sentence which have only 1 word
            if (countWord >= 2) {
                sentences.add(sentence);
            }
            index = end + 1;
        }

        return sentences.toArray(new String[sentences.size()]);
    }

    private String trimString(String str) {
        int start, end;

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < str.length(); index++) {
            char c = str.charAt(index);
            if (c == '\n') {
                if (index == 0 || Character.isLetterOrDigit(str.charAt(index - 1))) {
                    builder.append(' ');
                }
            } else {
                builder.append(c);
            }
        }

        str = builder.toString();
        System.out.println("Before:" + str);
        for (start = 0; start < str.length(); start++) {
            if (Character.isLetterOrDigit(str.charAt(start))) {
                break;
            }
        }
        for (end = str.length() - 1; start < end; end--) {
            if (Character.isLetterOrDigit(str.charAt(end))) {
                break;
            }
        }

        return str.substring(start, end + 1);
    }

}
