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
import java.util.ArrayList;

/**
 * Created by eleven on 3/12/14.
 */
public class TestTika {

    public static void main(String[] args) {
        System.out.println(new Boolean("true"));
        String fileName = "D:\\Study\\Data\\Ebook\\information theory infernce and learning algorithms david jc mackey_2.pdf";
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

        TextTool text = new TextTool(handler.toString());

        // Parser text to word list
        ArrayList<Word> wordArray = text.toWordArray();
        Word[] words = wordArray.toArray(new Word[wordArray.size()]);

        // output to file
        try {
            FileOutputStream output = new FileOutputStream("D:\\Eleven\\test.txt");
            for (int i = 0; i < words.length; i++){
                output.write((words[i]+"\n").getBytes());
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find a sentence contains a word
        String result = text.getSentence("hello");
    }
}
