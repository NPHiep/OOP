package ooptest.valuess;

import ooptest.valuess.utils.parsers.CHMParser;
import ooptest.valuess.utils.text.TextTool;
import ooptest.valuess.utils.text.Word;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * Created by eleven on 3/12/14.
 */
public class TestTika {

    public static void main(String[] args) {        
        String fileName = "/home/eleven/Downloads/math.pdf";
        ContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        InputStream input = null;

        if (fileName.endsWith(".pdf")) {
            try {
                input = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        long start = System.currentTimeMillis();
        try {
            if (fileName.endsWith(".pdf"))
                (new PDFParser()).parse(input, handler, metadata, new ParseContext());
            else
                (new CHMParser(fileName)).parse(input, handler, metadata, new ParseContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } finally {
            if (input != null) try {
                input.close();
            } catch (IOException e) {
            }
        }
        
        System.out.println("Time parser: " + (System.currentTimeMillis() - start)/1000+"s");

        start = System.currentTimeMillis();
        TextTool text = new TextTool(handler.toString());
        
        // Parser text to word list
        Word[] words = text.toWordArray();                       
        System.out.println("Time parser to word list: " + (System.currentTimeMillis() - start)/1000+"s");

        // output to file
        try {
            FileOutputStream output = new FileOutputStream("/home/eleven/test.txt");
            for (int i = 0; i < words.length; i++){
                output.write((words[i]+"\n").getBytes());
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
                
        start = System.currentTimeMillis();
        // Find a sentence contains a word
        String[] result = text.getSentence("development");
        System.out.println("Time parser to find sentence: " + (System.currentTimeMillis() - start)/1000+"s");
        System.out.println(result.length+ " sentence(s) found");
        for (int i = 0; i < result.length; i++){
        	System.out.println("-->>"+result[i]);
        }
    }
}