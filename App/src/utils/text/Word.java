package utils.text;

/**
 * Created by eleven on 3/28/14.
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
    
    public int compareTo(String str){
    	return word.compareTo(str);
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
