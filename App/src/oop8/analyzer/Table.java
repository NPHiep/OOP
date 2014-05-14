/* Đây là 1 bảng. Có thể hiểu là 1 database thu nhỏ.
 * 
 */
package oop8.analyzer;

import oop8.utils.WordItem;

import java.util.*;

/**
 *
 * @author Bui Duc Hanh
 */

/**
 * Table: Data Structure to store word and more information
 * use Map to manage data follow model: key - value
 */
public class Table {
    private Map<String,Vector> db;
    
    public Table(){
        this.db = new TreeMap<String,Vector>();
    }

    /**
     * add data to table with key k,
     * more information consits of: a word, a extra word and a num as word's frequency
     * @param k
     * @param word
     * @param num
     * @param moreWord
     */
    protected void addData(String k, String word, int num, String moreWord){
        Vector<Object> v = new Vector<Object>();
        v.addElement(word);
        v.addElement(num);
        if (moreWord != null){
            List<String> more = getMoreWord(k);
            if (!more.contains(moreWord)) more.add(moreWord);
            v.addElement(more);
        }
        this.db.put(k, v);
    }

    /**
     * check if db contained keyword k
     * @param k
     * @return if db contained keyword k or not
     */
    public boolean checkAvailable(String k){
        return this.db.containsKey(k);
    }

    /** get word from key word: k. Word is the first element in Vector contain more information about value in key-value
     * @param k
     * @return word which of keyword is k
     */
    protected String getWord(String k){
        String word= new String();
        if (this.checkAvailable(k)){
            Vector v = this.db.get(k);
            word = v.get(0).toString();
        }
        return word;
    }

    /**
     * get more words related to keyword (use to store and get derive word
     * @param k
     * @return list of more words
     */
    public List<String> getMoreWord(String k){
        if (checkAvailable(k)){
            Vector v = (Vector)this.db.get(k);
            if (v.capacity() > 2) {
                Object obj = v.elementAt(2);
                return (List<String>)v.elementAt(2);
            }
            else return new ArrayList<String>();
        }
        return new ArrayList<String>();
    }

    /**
     * get frequency of a represent-word of a group of word
     * @param k
     * @return number of words
     */
    protected int getNum(String k){
        int num =0;
        if (this.checkAvailable(k)){
            Vector v = this.db.get(k);
            String temp = v.get(1).toString();
            num = Integer.parseInt(temp);
        }
        return num;
    }

    /**
     * get Vector contain more information of keyword
     * @return
     */
    public Vector getAllData(){
        Vector r = new Vector();
        for (String i : this.db.keySet()){
            Vector temp= new Vector();
            temp.add(0,i);
            temp.add(1,this.getWord(i));
            temp.add(2,this.getNum(i));
            r.add(temp);
        }
        return r;
    }
    
    public void getListViewData(){
        for (String i : this.db.keySet()){
            WordItem wItem = new WordItem(this.getWord(i), this.getNum(i));
            oop8.wordanalyzer.WordAnalyzer.data.add(wItem);
        }
    }
    
    public void clearAllData(){
        this.db.clear();
    }
}
