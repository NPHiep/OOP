/*
 * Đây là bảng dùng để thống kê các từ xuất hiện.
 * 1 phần tử thuộc bảng này gồm 3 phần 
 * BasicWord(từ gốc)-KEY, 
 * MaskWord(từ để hiện thị khi in)
 * số lượng
 */
package oop8.analyzer;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Bui
 */
public class StaticTable extends Table {

    public StaticTable() {
        super();
    }

    public void add(String word, BaseDict base) {
        if (base.checkAvailable(word)) {
            String BasicWord = base.getBasicWordFromDict(word);
            this.increaseCountWord(BasicWord, word);
        }
    }

    public void addi(String word, int count, BaseDict base) {
        if (base.checkAvailable(word)) {
            String BasicWord = base.getBasicWordFromDict(word);
            this.increaseCountWords(BasicWord, count, word);
        }
    }

    private void increaseCountWords(String basicWord, int count, String maskedWord) {
        if (!this.checkAvailable(basicWord)) {
            addData(basicWord, maskedWord, count, maskedWord);
        } else {
            int i = getNum(basicWord);
            String maskWord = getWord(basicWord);
            i = i + count;
            if (maskWord.length() > maskedWord.length()) {
                addData(basicWord, maskedWord, i, maskedWord);
            } else {
                addData(basicWord, maskWord, i, maskedWord);
            }
        }
    }

    private void increaseCountWord(String basicWord, String maskedWord) {
        if (!this.checkAvailable(basicWord)) {
            addData(basicWord, maskedWord, 1, maskedWord);
        } else {
            int i = getNum(basicWord);
            String maskWord = getWord(basicWord);
            i++;
            if (maskWord.length() > maskedWord.length()) {
                addData(basicWord, maskedWord, i, maskedWord);
            } else {
                addData(basicWord, maskWord, i, maskedWord);
            }
        }
    }

    public ArrayList<String> getUnsualWord(BaseDict Base) {
        ArrayList<String> tmp = new ArrayList<String>();
        Vector temp = this.getAllData();
        for (int i = 0; i < temp.size(); i++) {
            Vector t = (Vector) temp.get(i);
            String word = t.get(1).toString();
            if (Base.getTypeOfWord(word) == 1) tmp.add(word);
        }
        return tmp;
    }

}
