package cpen221.mp1.cryptanalysis;

import cpen221.mp1.Document;

import java.util.ArrayList;
import java.util.List;

public abstract class Cryptography {

    /**
     * Encrypt a document by replacing the i-th character, c_i, with
     * c_i + A \times \sin{(i \times 2\pi / P + \pi/4)}
     * where A is the amplitude and P is the period for the sine wave.
     * When encrypting text with multiple sentences, exactly one space
     * is used to separate sentences.
     *
     * @param doc       the document to encrypt
     * @param length    the number of characters to encrypt.
     *                  If {@code length} is less than the number of
     *                  characters in the document then only that many
     *                  characters are encrypted.
     *                  If {@code length} is greater than the number
     *                  of characters in the document then additional
     *                  ' ' are added at the end and encrypted.
     * @param period    is the period of the sine wave used for encryption
     *                  and {@code period} must be a factor of
     *                  {@code length} other than 1 and {@code length} itself.
     * @param amplitude is the amplitude of the encrypting sine wave
     *                  and can be 64, 128, 256 or 512.
     * @return the encrypted array, with exactly one encrypted space
     * separating sentences.
     */
    public static int[] encrypt(Document doc, int length, int period, int amplitude) {
        //initialize char list
        List<Character> chars = new ArrayList<>();
        for(int i=1; i<=doc.numSentences(); i++){
            String str = doc.getSentence(i);
            char[] ca = str.toCharArray();
            for(char c : ca){
                chars.add(c);
            }
        }
        int size = chars.size();
        int[] encrypted = new int[size];

        double frequency = 2 * Math.PI / period;
        double phase = Math.PI / 4;

        if (length < size) {
            for (int i = 0; i < length; i++) {
                encrypted[i] = (int) (chars.get(i) + amplitude * Math.sin(i * frequency + phase));
            }

            for (int i = length; i < size; i++) {
                encrypted[i] = chars.get(i);
            }
        }
        else{
            for(int i=0; i < size; i++){
                encrypted[i] = (int) (chars.get(i) + amplitude * Math.sin(i * frequency + phase));
            }
        }
        return encrypted;
    }


    /**
     * Decrypt a text that has been encrypted using {@code Cryptography#encrypt}.
     *
     * @param codedText the data to decrypt.
     * @return the decrypted text.
     */
    public static String decrypt(int[] codedText) {
        // TODO: implement this method
        return null;
    }

}