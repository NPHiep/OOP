package ooptest.valuess.utils.text;

/**
 * Created by eleven on 3/28/14.
 */
public class Word {
    private String word;
    private long frequency;    

    public Word(String aWord) {
        word = aWord;        
        frequency = 1;
    }

    public void adjust() {        
        frequency++;        
    }
    
    public int compareTo(String str){
    	return word.compareTo(str);
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
    

}
