package cpen221.mp1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class Task1Tests {

    private static Document testDocument1;
    private static Document testDocument2;
    private static Document empty;
    private static Document dot;
    private static Document oneword;
    private static Document tenword;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        testDocument1 = new Document("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new Document("The Ant and The Cricket",
            new URL("http://textfiles.com/stories/antcrick.txt"));
        empty = new Document("empty", "resources/empty.txt");
        dot = new Document("dot", "resources/dot.txt");
        oneword = new Document("oneword", "resources/oneword.txt");
        tenword = new Document("tenword", "resources/tenword.txt");
    }

    @Test
    public void testAvgSentenceLength() {
        Assertions.assertEquals(10.027, testDocument1.averageSentenceLength(), 0.005);
    }

    @Test
    public void testAvgSentenceComplexity() {
        Assertions.assertEquals(1.702, testDocument2.averageSentenceComplexity(), 0.005);
    }

    @Test
    public void testSentences() {
        Assertions.assertEquals(37, testDocument1.numSentences());
        Assertions.assertEquals(
            "\"We can't do that,\" they said, \"We must store away food for the winter.",
            testDocument2.getSentence(5));
    }

    @Test
    public void testavgWordLength() {
        Assertions.assertEquals(0, empty.averageWordLength());
        Assertions.assertEquals(0, dot.averageWordLength());
        Assertions.assertEquals(4, oneword.averageWordLength());
    }

    @Test
    public void testdivby0() {
        Assertions.assertEquals(0, empty.uniqueWordRatio());
        Assertions.assertEquals(0, dot.uniqueWordRatio());

    }

    @Test
    public void testUniqueWord() {
        Assertions.assertEquals(1, oneword.uniqueWordRatio());
        Assertions.assertEquals(0.1, tenword.uniqueWordRatio());
        Assertions.assertEquals(0, empty.uniqueWordRatio());
    }

    @Test
    public void testHapax() {
        Assertions.assertEquals(1, oneword.hapaxLegomanaRatio());
        Assertions.assertEquals(0, tenword.hapaxLegomanaRatio());
        Assertions.assertEquals(0, empty.hapaxLegomanaRatio());
    }
}
