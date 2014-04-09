/*
 * Đây là bảng dùng để lưu tất cả các từ
 * Một phần tử trong bảng sẽ có 3 trường Word (từ thực tế) - KEY, BasicWord(từ gốc) và loại từ
 * Loại từ gồm 3 loại là BASIC, EXTEND và COMMON.
 * 
 */
package Sta_word;

/**
 *
 * @author Bùi Đức Hạnh
 */
public class BaseDict extends Table{
    final int BASIC =1;
    final int EXTEND=2;
    final int COMMON=0;
    public BaseDict (){
        super();
    }
    
    public void addAWordIntoDict(String Word, String BasicWord, int type){
        addData(Word, BasicWord, type);
    }
    
    public String getBasicWordFromDict(String Word){
        return getWord(Word);
    }
    
    public int getTypeofWord(String Word){
        return getNum(Word);
    }
    
}
