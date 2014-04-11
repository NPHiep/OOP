package ooptest.valuess.utils.text;

import java.util.ArrayList;

/**
 * Created by eleven on 3/28/14.
 */
public class Word implements Comparable {
    private String word;
    private long frequency;
    private ArrayList<String> nearWord;

    public Word(String aWord) {
        word = aWord;
        nearWord = new ArrayList<String>();
        frequency = 1;
    }

    public void addAWord(String aWord) {
        if (word.compareTo(aWord) == 0)
            frequency++;
        else {
            frequency++;
            if (!nearWord.contains(aWord))
                nearWord.add(aWord);
        }
    }

    public String getWord() {
        return word;
    }

    public long getFrequency() {
        return frequency;
    }

    public String toString(){
        return word;
    }

    public String[] getNearWords() {
        return nearWord.toArray(new String[nearWord.size()]);
    }

    public boolean checkNearWord(String aWord) {
        if (word.compareTo(aWord) == 0)
            return true;
        if (nearWord.contains(aWord))
            return true;

        return WordTool.near(this.word, aWord);
    }

    @Override
    public int compareTo(Object o) {
        Word w = (Word) o;
        if (checkNearWord(((Word) o).word))
            return 1;
        else
            return word.compareTo(((Word) o).word);
    }
}
