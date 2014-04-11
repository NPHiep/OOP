/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RunProject;

import Sta_word.BaseDict;
import Sta_word.StaticTable;
import java.io.*;
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
    private static BufferedReader reader;
    
    public static void Print() throws IOException{
        Vector V= CountWord.getAllData();
        File file = new File("data1.txt");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
        for (Object i : V){
            Vector temp = (Vector) i;
            String BasicWord = (String) temp.get(0);
            String Word= (String) temp.get(1);
            int count = (int) temp.get(2);
            String content = ""+BasicWord+" "+Word+" "+count+"\n";
	    bw.write(content);		
        }
        bw.close();
    }
    public static void main (String[] args){
        Base.ImportData();
               for (int i=0;i<20;i++){
           try {
            reader = new BufferedReader(new FileReader("test.txt"));// load thá»­ 1 file test láº·p Ä‘i láº¯p láº¡i 20 láº§n Ä‘á»ƒ tÃ­nh
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
        try {
            Print();
        } catch (IOException ex) {
            Logger.getLogger(RunProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
