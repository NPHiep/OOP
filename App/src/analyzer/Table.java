/* Đây là 1 bảng. Có thể hiểu là 1 database thu nhỏ.
 * 
 */
package analyzer;

import utils.WordItem;

import java.util.*;

/**
 *
 * @author Bui Duc Hanh
 */
public class Table {
    private Map<String,Vector> db;
    
    public Table(){
        this.db = new TreeMap<String,Vector>();
    }

    protected void addData(String K, String Word, int num, String moreWord){
        Vector<Object> V = new Vector<Object>();
        V.addElement(Word);
        V.addElement(num);
        if (moreWord != null){
            List<String> more = getMoreWord(K);
            if (!more.contains(moreWord)) more.add(moreWord);
            V.addElement(more);
        }
        this.db.put(K, V);
    }
    
    public boolean checkAvailable(String K){
        return this.db.containsKey(K);
    }
    
    protected Vector getData(String K){
        return this.db.get(K);
    }
    
    protected String getWord(String K){
        String Word= new String();
        if (this.checkAvailable(K)){
            Vector V = this.db.get(K);
            Word = V.get(0).toString();
        }
        return Word;
    }

    public List<String> getMoreWord(String K){
        if (checkAvailable(K)){
            Vector V = (Vector)this.db.get(K);
            if (V.capacity() > 2) {
                Object obj = V.elementAt(2);
                return (List<String>)V.elementAt(2);
            }
            else return new ArrayList<String>();
        }
        return new ArrayList<String>();
    }
    
    protected int getNum(String K){
        int num =0;
        if (this.checkAvailable(K)){
            Vector V = this.db.get(K);
            String temp = V.get(1).toString();
            num = Integer.parseInt(temp);
        }
        return num;
    }
    public Vector getAllData(){
        Vector R= new Vector();
        for (String i : this.db.keySet()){
            Vector temp= new Vector();
            temp.add(0,i);
            temp.add(1,this.getWord(i));
            temp.add(2,this.getNum(i));
            R.add(temp);
        }
        return R;
    }
    
    public void getListViewData(){
        wordanalyzer.WordAnalyzer.data.clear();
        for (String i : this.db.keySet()){
            WordItem wItem = new WordItem(this.getWord(i), this.getNum(i));
            wordanalyzer.WordAnalyzer.data.add(wItem);
        }
    }
    
    public void clearAllData(){
        this.db.clear();
    }
    
    protected void removeData(String K){
        this.db.remove(K);
    }
}
