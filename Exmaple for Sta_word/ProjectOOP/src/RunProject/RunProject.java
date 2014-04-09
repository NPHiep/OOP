/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RunProject;

import Sta_word.BaseDict;
import Sta_word.StaticTable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bui
 */
public class RunProject {
    public static BaseDict Base = new BaseDict();
    public static StaticTable CountWord= new StaticTable();
    
    public static void Print(){
        Vector V= CountWord.getAllData();
        for (Object i : V){
            Vector temp = (Vector) i;
            String BasicWord = (String) temp.get(0);
            String Word= (String) temp.get(1);
            int count = (int) temp.get(2);
            System.out.printf("%-30s%-30s%5d\n",BasicWord,Word,count);
        }
    }
    public static void main (String[] args){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("fullword.full"));
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    Base.addAWordIntoDict(line, line, 1); // hiện tại là chưa có từ dẫn xuất nên để mọi từ đều là basic
                }
            } catch (IOException ex) {
                Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // sửa 1 số từ trong danh sách common word 
        try {
            reader = new BufferedReader(new FileReader("commonword.txt"));
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    Base.addAWordIntoDict(line, line, 0); // hiện tại là chưa có từ dẫn xuất nên để mọi từ đều là basic
                }
            } catch (IOException ex) {
                Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       for (int i=0;i<20;i++){
           try {
            reader = new BufferedReader(new FileReader("test.txt"));// load thử 1 file test lặp đi lắp lại 20 lần để tính
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    CountWord.Add(line, Base);
                }
            } catch (IOException ex) {
                Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       }
       Print();
    }
}
