package oop8.utils.text;

/**
 * Created by eleven on 3/28/14.
 */

/**
 * Data construct to store word and its frequency
 */
public class Word {
    private String word;
    private int frequency;    

    public Word(String aWord) {
        word = aWord;        
        frequency = 1;
    }

    public void adjust() {        
        frequency++;        
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public String toString(){
        return word;
    }
}
