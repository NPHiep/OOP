/* Đây là 1 bảng. Có thể hiểu là 1 database thu nhỏ.
 * 
 */
package analyzer;

import utils.WordItem;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author Bui Duc Hanh
 */
public class Table {
    private Map<String,Vector> db;
    
    public Table(){
        this.db = new TreeMap<String,Vector>();
    }
    
    protected void addData(String K, String Word, int num){
        Vector V = new Vector();
        V.add(0,Word);
        V.add(1,num);
        if (this.checkAvailable(K)) this.db.remove(K);
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
        System.out.println(wordanalyzer.WordAnalyzer.data.size());
        for (String i : this.db.keySet()){
//            System.out.println(">>>>")
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
