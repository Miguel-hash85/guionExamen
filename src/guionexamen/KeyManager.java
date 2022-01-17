/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guionexamen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author 2dam
 */
public class KeyManager {
    
    //private static final String ALGORITHM = "AES";
    //private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";
    // cifrado
    //private static byte[] salt = "esta es la salt!".getBytes();
    public static String decrypt (String mensaje) throws Exception {
        PublicKey publicKey;
        KeyFactory keyFactory;       
        byte[]key=fileReader("keypublic.txt");
        byte[] encodedMessage = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            Cipher cipherRSA = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
            publicKey = keyFactory.generatePublic(spec);
            cipherRSA.init(Cipher.DECRYPT_MODE, publicKey);
            encodedMessage = cipherRSA.doFinal(hexStringToByteArray(mensaje));
            
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(KeyFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        //We use the method covertStringToHex to get the hex value of the String
        System.out.println("Hasheo decrypt:"+ hashPassword(new String(encodedMessage)));
        return new String(encodedMessage);
    }
    // descifrar
    public static String encrypt(String password) throws Exception {
        //password=new String(hexStringToByteArray(password));
        KeyFactory factoriaRSA;
        byte[] decodedMessage=null;
        //String path=KeyManager.class.getResource("PrivateKey.txt").getPath();
        PrivateKey privateKey;
        Cipher cipher;
        String desc = null;
        byte[]key = fileReader("keyprivate.txt");
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);
            factoriaRSA = KeyFactory.getInstance("RSA");
            privateKey = factoriaRSA.generatePrivate(spec);
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            System.out.println("Hasheo encrypt:"+ hashPassword(password));
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            //Le decimos que descifre
            decodedMessage = cipher.doFinal(password.getBytes());
            // Texto descifrado
            desc = new String(decodedMessage);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(KeyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hexadecimal(decodedMessage);
    }
    
    private static String convertStringToHex(String password) {
        StringBuilder hex = new StringBuilder();
        for(int i=0; i<password.length(); i++){
            int decimal=(int)i;
            hex.append(Integer.toHexString(decimal));
        }
        return hex.toString();
    }
    
    static String hexadecimal(byte[] resumen) {
        String hex = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1)
                    hex += "0";
            hex += h;
        }
        return hex.toUpperCase();
    }
    
    public static byte[] fileReader(String path) {
        byte ret[] = null;
        File file = new File(path);
        try {
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public static byte[] hexStringToByteArray(String password) {
        int len = password.length();
        byte[] mensajeByte = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            mensajeByte[i / 2] = (byte) ((Character.digit(password.charAt(i), 16) << 4)
                    + Character.digit(password.charAt(i + 1), 16));
        }
        return mensajeByte;
    }
    
    public static String hashPassword(String password){
        MessageDigest messageDigest;
        String base64=null;
        try {
            messageDigest=MessageDigest.getInstance("SHA");
            messageDigest.update(password.getBytes("UTF-8"));
            password=new String(messageDigest.digest());
            base64=Base64.getEncoder().encodeToString(password.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }


    
}
