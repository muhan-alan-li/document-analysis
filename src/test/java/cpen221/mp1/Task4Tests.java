package cpen221.mp1;

import cpen221.mp1.similarity.DocumentSimilarity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class Task4Tests {

    private static Document testDocument1;
    private static Document testDocument2;
    private static Document testDocument3;
    private static Document fatman;
    private static Document skinnyturd;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        testDocument1 = new Document("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new Document("The Ant and The Cricket", new URL("http://textfiles.com/stories/antcrick.txt"));
        testDocument3 = new Document("badinput", "resources/badinput.txt");
        fatman = new Document("fatman", "resources/fatman.txt");
        skinnyturd = new Document("skinnyturd", "resources/skinnyturd.txt");
    }

    @Test
    public void testvalswithantcrick() {
        double jsDivergence = DocumentSimilarity.jsDivergence(testDocument1,testDocument2);
        double documentDivergence = DocumentSimilarity.documentDivergence(testDocument1,testDocument2);
        System.out.println("antcrick.txt vs antcrick on the web:");
        System.out.println("jsdivergence: " + jsDivergence);
        System.out.println("document divergence: "+ documentDivergence);
    }

    @Test
    public void testDDAntcrickBadinput() {
        double documentDivergence = DocumentSimilarity.documentDivergence(testDocument1,testDocument3);
        System.out.println(documentDivergence);
    }

    @Test
    public void testfatmanskinnyturd() {
        double jsDivergence = DocumentSimilarity.jsDivergence(fatman,skinnyturd);
        double documentDivergence = DocumentSimilarity.documentDivergence(fatman,skinnyturd);

        Assertions.assertEquals(0.7407, jsDivergence, 0.0005);
        Assertions.assertEquals(46.61, documentDivergence, 0.05);
    }

}
