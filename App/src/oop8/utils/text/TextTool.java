package oop8.utils.text;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import oop8.wordanalyzer.WordAnalyzer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by eleven on 3/20/14.
 */
/**
 * TextTool use for parser text -> word Array and get Sentence that contains a
 * specified word
 */
public class TextTool {

    public static final int NAME_LOCATION = 0;
    public static final int NAME_ORGANIZATION = 1;
    public static final int NAME_PERSON = 2;
    private String[] sents = null;
    private Tokenizer tokenizer;
    private NameFinderME locationFinder;
    private NameFinderME organizationFinder;
    private NameFinderME personFinder;

    /**
     * init text tool
     * @param content: plain text which is parsed from pdf or chm file
     */
    public TextTool(String content)
    {
        int index = -1;
        StringBuilder builder = new StringBuilder();

        // remove \n characters and replace by suitable ways
        while (true){
            int old = index;
            index = content.indexOf("\n", index + 1);
            if (index < 0){
                builder.append(content.substring(old+1));
                break;
            }
            else {
                // detect '\n' as a connect between 2 part of a word
                if (index > 2 && content.charAt(index-1) == '-' && Character.isLetter(content.charAt(index-2))){
                    builder.append(content.substring(old+1, index-1));
                } else {
                    // if not , simply replace by ' ' char
                    builder.append(content.substring(old+1, index)+" ");
                }
            }
        }

        InputStream is = null;

        // init Model to analyze sentences, tokenize and remove NameFinder (use Apache opennlp lib)
        try {
            // model to tokenize sentence
            is = new FileInputStream(WordAnalyzer.class.getResource("data/en-token.bin").getPath());
            TokenizerModel tokenModel = new TokenizerModel(is);
            tokenizer = new TokenizerME(tokenModel);
            is.close();

            //model to get location NameFinder
            is = new FileInputStream(WordAnalyzer.class.getResource("data/en-ner-location.bin").getPath());
            TokenNameFinderModel nameModel = new TokenNameFinderModel(is);
            locationFinder = new NameFinderME(nameModel);
            is.close();

            //model to get organization NameFinder
            is = new FileInputStream(WordAnalyzer.class.getResource("data/en-ner-organization.bin").getPath());
            nameModel = new TokenNameFinderModel(is);
            organizationFinder = new NameFinderME(nameModel);
            is.close();

            //model to get person NameFinder
            is = new FileInputStream(WordAnalyzer.class.getResource("data/en-ner-person.bin").getPath());
            nameModel = new TokenNameFinderModel(is);
            personFinder = new NameFinderME(nameModel);
            is.close();

            //model to analyze content to sentences
            is = new FileInputStream(WordAnalyzer.class.getResource("data/en-sent.bin").getPath());
            SentenceModel sentenceModel = new SentenceModel(is);
            SentenceDetector detector = new SentenceDetectorME(sentenceModel);
            //parse to list sentences
            sents = detector.sentDetect(builder.toString());
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     sau khi file -> list word
     hiện tại đang add vào string list
     */

    /**
     * analyze list sentence to get list word (by tokenization) and add to database
     */

    public void addToDataBase() {
        InputStream is = null;
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> strList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("[a-zA-Z'-]*[a-zA-Z]");
            //tokenize
            long start = System.currentTimeMillis();
            for (String sent: sents){
                // tokenize each sentences
                String[] temp = tokenizer.tokenize(sent);
                for (String s: temp){
                    if (pattern.matcher(s).matches()){
                        // with word start with a UpperCase, add it to list to check NameFinder
                        if (Character.isUpperCase(s.charAt(0)))
                            nameList.add(s);
                        strList.add(s);
                    }
                }
            }
            System.out.println("Tokenize in "+(System.currentTimeMillis()-start));

        // remove Name from strList
        start = System.currentTimeMillis();
        String[] name = nameList.toArray(new String[nameList.size()]);

        try {
            strList.removeAll(getNameFinder(NAME_LOCATION, name));
            strList.removeAll(getNameFinder(NAME_ORGANIZATION, name));
            strList.removeAll(getNameFinder(NAME_PERSON, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Get Name Finder in "+(System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        Collections.sort(strList);						// sort list first
        System.out.println("Sorted in: "+ (System.currentTimeMillis() - start ));

        ArrayList<Word> wordList = new ArrayList<Word>();       // wordlist contains list words in ascending order
        String last = "";

        start = System.currentTimeMillis();
        for (int i = 0; i < strList.size(); i++) {
            String word = strList.get(i);
            if (Character.isLetter(word.charAt(0))) {
                if (last.compareTo(word) == 0) {
                    wordList.get(wordList.size() - 1).adjust();
                } else {
                    last = word;
                    wordList.add(new Word(word.toLowerCase()));
                    if (wordList.size() >= 2) {
                        Word tmpWord = wordList.get(wordList.size() - 2);
                        WordAnalyzer.staticTable.addi(tmpWord.getWord(), tmpWord.getFrequency(), WordAnalyzer.baseData);
                    }
                }
            }
        }
        System.out.println("Add to dict in: "+ (System.currentTimeMillis()-start ));

    }

    /**
     * get list NameFinder in a array of words.
     * @param type : location, organization, person
     * @param source : array of words to check
     * @return list NameFinder by it's type
     * @throws IOException
     */
    private List<String> getNameFinder(int type, String[] source) throws IOException {
        NameFinderME nameFinder;
        switch (type) {
            case NAME_LOCATION:
                nameFinder = locationFinder;
                break;
            case NAME_ORGANIZATION:
                nameFinder = organizationFinder;
                break;
            case NAME_PERSON:
                nameFinder = personFinder;
                break;
            default:
                return null;
        }
        Span[] span = nameFinder.find(source);
        System.out.println(source.length+"~~~"+span.length);
        ArrayList<String> array = new ArrayList<String>();
        for (Span s: span){
            for (int i = s.getStart(); i < s.getEnd(); i++)
                if (!array.contains(source[i])) array.add(source[i]);
        }
        return array;
    }

    /**
     * get array of sentences contains word in sentence
     * @param word
     * @return  array of suitable sentences
     */
    public String[] getSentence(String word)  {
        int index = 0;
        ArrayList<String> sentences = new ArrayList<String>();
        word = word.toLowerCase();

        InputStream is = null;
        Pattern pattern = Pattern.compile(".*\\b"+word+"\\b([^-/.]+.*)?", Pattern.CASE_INSENSITIVE);
        Pattern subPattern = Pattern.compile(".*[^a-zA-Z0-9'-\\.,()\\[\\]{}\"].*");
        TokenizerME tokenizer = null;
        try{
            is = new FileInputStream(WordAnalyzer.class.getResource(("data/en-token.bin")).getPath());
            TokenizerModel model = new TokenizerModel(is);
            tokenizer = new TokenizerME(model);
        } catch (IOException e){}
        for (String s: sents){
            if (s.length() <= 300){ // too long is dangerous
            if (pattern.matcher(s).matches()){
                String[] words = tokenizer.tokenize(s);
                boolean checkAbility = true;
                for (String w: words){
                    if (subPattern.matcher(w).matches()){
                        checkAbility = false;
                    } else if (w.matches("[a-zA-Z'-]+")){
                        checkAbility = WordAnalyzer.baseData.checkAvailable(w.toLowerCase());
                    }
                    if (!checkAbility) break;
                }
                if (checkAbility)
                    sentences.add(s);
            }
            }
        }

        return sentences.toArray(new String[sentences.size()]);
    }

}