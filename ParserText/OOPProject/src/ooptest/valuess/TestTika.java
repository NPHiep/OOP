package ooptest.valuess;

import ooptest.valuess.utils.parsers.CHMParser;
import ooptest.valuess.utils.text.TextTool;
import ooptest.valuess.utils.text.Word;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eleven on 3/12/14.
 */
public class TestTika {

    public static void main(String[] args) {
        System.out.println(new Boolean("true"));
        String fileName = "/home/eleven/Downloads/test.chm";
        ContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();


        try {
            (new CHMParser(fileName)).parse(null, handler, metadata, new ParseContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }

        TextTool text = new TextTool(handler.toString());

        ArrayList<Word> wordArray = text.toWordArray();

        String word = "Graphical";
        String sentence = text.getSentence(word);

        System.out.println("[debug] sentence contains \""+ word+ "\" : "+sentence);
    }
}
