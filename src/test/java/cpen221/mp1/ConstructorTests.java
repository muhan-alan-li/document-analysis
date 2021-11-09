package cpen221.mp1;

import cpen221.mp1.exceptions.NoSuitableSentenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConstructorTests {

    private static Document testDocument1;
    private static Document testDocument2;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        testDocument1 = new Document("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new Document("The Ant and The Cricket", new URL("http://textfiles.com/stories/antcrick.txt"));
    }

    @Test
    public void testWrongPath() {
        Document testDocument = new Document("shouldFail", "resources/fake.txt");
        //Terminal should print "Problem reading file/URL!"
    }
    @Test
    public void testWrongURL() {
        try {
            Document testDocument = new Document("The Ant and The Cricket", new URL("https://johncena/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Terminal should print "Problem reading file/URL!"
    }

    @Test
    public void docIdTest(){
        Assertions.assertEquals("The Ant and The Cricket", testDocument1.getDocId());
        Assertions.assertEquals("The Ant and The Cricket", testDocument2.getDocId());
    }

    @Test
    public void testEmptyDoc(){
        Document emptyDoc = new Document("empty", "resources/empty.txt");
        List<String> expected = new ArrayList<>();
        try{
            emptyDoc.getSentence(1);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Index out of bounds at 0, ArrayList is empty");
        }
    }


}
