/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author PhiHiep
 */
public class WordItem{
    private final SimpleStringProperty Word;
    private final SimpleIntegerProperty Count;
 
    public WordItem(String word, int count) {
        this.Word = new SimpleStringProperty(word);
        this.Count = new SimpleIntegerProperty(count);
    }

    public String getWord() {
        return Word.get();
    }
    public void setWord(String word) {
        Word.set(word);
    }
        
    public int getCount() {
        return Count.get();
    }
    public void setCount(int count) {
        Count.set(count);
    }

}
