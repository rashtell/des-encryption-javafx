package com.rashtell;

/**
 *
 */

/**
 * @author rAsHtElL
 *
 */

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class Main extends Application implements EventHandler {

    private static Cipher ecipher;
    private static Cipher dcipher;

    private static SecretKey key;

    TextArea input;
    TextArea output;

    public String execute(String information, String mode){
        try {
            //generate secret key using DES algorithm
            key = KeyGenerator.getInstance("DES").generateKey();

            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");

            //Initialize the ciphers with the given key
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);

//            String info = "Thi is a classified message !";
           if (mode.equals("encrypt")) {
               String encrypted = ecrypt(information);
               return encrypted;
           }else if (mode.equals("decrypt")){
               String decrypted = decrypt(information);
                return decrypted;
           }else {
               return "";
           }

//            System.out.println(encrypted);
//            System.out.println(decrypted);


        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm: "+e.getMessage());
        }catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding: "+e.getMessage());
        }catch (InvalidKeyException e) {
            System.out.println("Invalid Key: "+e.getMessage());
        }

        return "";
    }

    private static String ecrypt(String info) {
        try {
            // encode the string into a sequence of bytes using the named charset

            // storing the result into a new byte array

            byte[] utf8 = info.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);

            // encode to base64
            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String decrypt(String encrypted) {
        try {
            // decode with base64 to get bytes
            byte[] dec = BASE64DecoderStream.decode(encrypted.getBytes());

            byte[] utf8 = dcipher.doFinal(dec);

            // create new string based on the specified charset
            return new String(utf8, "UTF8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Rashcrypt");

        input = new TextArea();
        input.setWrapText(true);
        input.setPrefRowCount(15);

        output = new TextArea();
        output.setWrapText(true);
        output.setPrefRowCount(15);
        output.setEditable(false);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5,5,5,5));

        Button encryptBut = new Button("Encrypt");
        encryptBut.setOnMouseClicked(event -> {
            String textInput = input.getText();
            output.setText(execute(textInput, "encrypt"));
        });

        Button  decryptBut = new Button("Decrypt");
        decryptBut.setOnMouseClicked(event -> {
            String textInput = input.getText();
            output.setText(execute(textInput, "decrypt"));
        });

        Insets insets = new Insets(5,5,5,5);

        hBox.setSpacing(10);
        hBox.getChildren().addAll(encryptBut, decryptBut);

        VBox root = new VBox();
        root.setSpacing(20);
        root.setPadding(insets);
        root.getChildren().addAll(input, output, hBox);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(Event event) {

    }

    public static void main(String[] args) {
        launch(args);
    }

}
