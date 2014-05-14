/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oop8.utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author PhiHiep
 */
public class HighScoreItem {
    
    private final SimpleStringProperty Name;
    private final SimpleIntegerProperty Score;
 
    public HighScoreItem(String word, int count) {
        this.Name = new SimpleStringProperty(word);
        this.Score = new SimpleIntegerProperty(count);
    }
 
    public String getName() {
        return Name.get();
    }
    public void setName(String word) {
        Name.set(word);
    }
        
    public int getScore() {
        return Score.get();
    }
    public void setScore(int count) {
        Score.set(count);
    }    
}

