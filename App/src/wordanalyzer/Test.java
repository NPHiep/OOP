package wordanalyzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by eleven on 5/7/14.
 */
public class Test extends Application{
//    NotificationPane notification;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Test.class.getResource("../utils/Dialog.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
    }
//    public static void main(String[] args) throws IOException {
//        String sample = "As soon as you find that sequence in a decrypted block, you know that you'd found the correct decryption key.";
//        InputStream is = new FileInputStream(Test.class.getResource("data/en-token.bin").getPath());
//        TokenizerModel model = new TokenizerModel(is);
//        Tokenizer tokenizer = new TokenizerME(model);
//        String[] tokens = tokenizer.tokenize(sample);
//        for (String w: tokens){
//            System.out.println(w);
//        }
//        is.close();
////        is = new FileInputStream(Test.class.getResource("data/en-ner-person.bin").getPath());
////        TokenNameFinderModel nameModel = new TokenNameFinderModel(is);
////        NameFinderME nameFinderME = new NameFinderME(nameModel);
////        Span[] spans = nameFinderME.find(tokens);
////        for (Span s: spans){
////            for (int i = s.getStart(); i < s.getEnd(); i++)
////                System.out.println(tokens[i]);
////        }
////        Pattern pattern = Pattern.compile(".*\\bhello\\b([^-/.]+.*)?", Pattern.CASE_INSENSITIVE);
////        if (pattern.matcher("i've say hello-comma to her").matches()) System.out.println(">>");
////        if (pattern.matcher("\"Hello\" is an greeting way in English").matches()) System.out.println(">>>");
////        TokenizerModel model = new TokenizerModel(is);
////        Tokenizer tokenizer = new TokenizerME(model);
////        String[] tokens = tokenizer.tokenize(sample);
////        for (String s: tokens){
////            System.out.println(""+s);
////        }
////        is.close();
//    }
}
