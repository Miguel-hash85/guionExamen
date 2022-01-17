/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guionexamen;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class KeyGen {

    /**
     * @param args the command line arguments
     */
    //private static final byte[] salt = "esta es la salt!".getBytes();
    public static void main(String[] args) {
        
        try {
            
            //System.out.println("Generando clave Blowfish...");
            KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
            generador.initialize(1024);
            KeyPair clave = generador.genKeyPair();
            //System.out.println("Formato: "+claveBlowfish.getFormat());
            //String publicKey = "mvLBiZsiTbGwrfJB";
            //String privateKey = "llllllllllllllll";
            fileWriter("keypublic.txt", clave.getPublic().getEncoded());
            fileWriter("keyprivate.txt",clave.getPrivate().getEncoded());
            String data = "abcd*1234";
            String cypherPass=KeyManager.encrypt(data);
            System.out.println(cypherPass);
            System.out.println(KeyManager.decrypt(cypherPass));
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGen.class.getName()).log(Level.SEVERE, null, ex);
        }
          catch (Exception ex) {
            Logger.getLogger(KeyGen.class.getName()).log(Level.SEVERE, null, ex);
        }
	
        
    }
    public static void fileWriter(String path, byte[] text) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(text);
        } catch (IOException e) {
                e.printStackTrace();
        }
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
    public static byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] ret = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, ret, 0, array1.length);
        System.arraycopy(array2, 0, ret, array1.length, array2.length);
        return ret;
    }
    
}
