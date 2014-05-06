/*
 * Đây là bảng dùng để lưu tất cả các từ
 * Một phần tử trong bảng sẽ có 3 trường Word (từ thực tế) - KEY, BasicWord(từ gốc) và loại từ
 * Loại từ gồm 3 loại là BASIC, EXTEND và COMMON.
 * 
 */
package analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bùi Đức Hạnh
 */
public class BaseDict extends Table {

    final int BASIC = 1;
    final int EXTEND = 2;
    final int COMMON = 0;

    public BaseDict() {
        super();
    }

    public void ImportData() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("data.txt"));
            
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts[parts.length - 2].compareTo("") != 0) {
                        String BasicWord = parts[1];
                        int type = Integer.parseInt(parts[2]);
                        this.addAWordIntoDict(parts[0], BasicWord, type);
                    } else {
                        this.addAWordIntoDict(parts[0], parts[0], 1);
                    }
                }
            } catch (IOException ex) {

            }
            
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(BaseDict.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {

        } finally {

        
    }
}

public void addAWordIntoDict(String Word, String BasicWord, int type) {
        addData(Word, BasicWord, type);
    }

    public String getBasicWordFromDict(String Word) {
        return getWord(Word);
    }

    public int getTypeofWord(String Word) {
        return getNum(Word);
    }

}
