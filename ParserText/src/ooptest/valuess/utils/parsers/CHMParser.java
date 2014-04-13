package ooptest.valuess.utils.parsers;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eleven on 3/18/14.
 */

/**
 * CHMParser - parser a chm file to get plain text
 */
public class CHMParser extends AbstractParser{
    
	private static final long serialVersionUID = -7449327136341928055L;
	private static final Set<MediaType> SUPPORTED_TYPES =
            Collections.unmodifiableSet(new HashSet<MediaType>(Arrays.asList(
                    MediaType.application("vnd.ms-htmlhelp"),
                    MediaType.application("chm"),
                    MediaType.application("x-chm"))));
    private String file;
    private String outputDirectory;
    private String filter = "([^\\s]+(\\.(htm|html))$)";

    public CHMParser(String fileName){
        this.file = fileName;
        try {
            File temp = File.createTempFile("tmp"+fileName,"");
            temp.delete();
            temp.mkdir();
            temp.deleteOnExit();
            this.outputDirectory = temp.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.outputDirectory == null){
                this.outputDirectory = "./tmp" + fileName;
            }
        }
    }

    public void setFileInput(String fileName){
        file = fileName;
    }

    public void setTemporaryFolder(String folderPath){
        outputDirectory = folderPath;
    }

    @Override
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }

    @Override
    public void parse(InputStream inputStream, ContentHandler contentHandler, Metadata metadata, ParseContext parseContext) throws IOException, SAXException, TikaException {
        Extractor extractor = new Extractor(file, outputDirectory, false, filter);
        try {
            extractor.extract();
        } catch (Extractor.ExtractionException e) {
            e.printStackTrace();
        }

        File[] htmlFile = extractor.getExtractFile();

        // metadata
        metadata.set(Metadata.CONTENT_TYPE, "application/vnd.ms-htmlhelp");

        // content
        XHTMLContentHandler xhtml = new XHTMLContentHandler(contentHandler, metadata);
        xhtml.startDocument();

        for (File f: htmlFile){
            xhtml.characters(parserHtmlElem(f));
        }

        deleteTemporaryFolder(new File(this.outputDirectory));

        xhtml.endDocument();
    }

    private void deleteTemporaryFolder(File f) {
        if (f.isDirectory()){
            for (File cur: f.listFiles()){
                deleteTemporaryFolder(cur);
            }
        }
        f.delete();
    }

    private String parserHtmlElem(File file) throws TikaException {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();

        try {
            (new HtmlParser()).parse(input, handler, metadata, new ParseContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return handler.toString();
    }
}
