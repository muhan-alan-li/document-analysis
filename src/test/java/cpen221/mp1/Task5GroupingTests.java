package cpen221.mp1;

import cpen221.mp1.similarity.DocPair;
import cpen221.mp1.similarity.GroupingDocuments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Task5GroupingTests {

    private static Document testDocument1;
    private static Document testDocument2;
    private static Document badinput;
    private static Document skinnyturd;
    private static Document fatman;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        testDocument1 = new Document("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new Document("The Ant and The Cricket", new URL("http://textfiles.com/stories/antcrick.txt"));
        badinput      = new Document("badinput", "resources/badinput.txt");
        skinnyturd    = new Document("skinnyturd", "resources/skinnyturd.txt");
        fatman        = new Document("fatman", "resources/fatman.txt");
    }

    @Test
    public void testGrouping(){
        Set<Document> testSet = new HashSet<Document>();
        testSet.add(testDocument1);
        testSet.add(testDocument2);
        testSet.add(badinput);

        Set<Set<Document>> outSet = GroupingDocuments.groupBySimilarity(testSet, 2);

        Set<Set<Document>> expectedOut = new HashSet<>();
        Set<Document> partition1 = new HashSet<>();
        Set<Document> partition2 = new HashSet<>();
        partition1.add(badinput);
        partition2.add(testDocument1);
        partition2.add(testDocument2);
        expectedOut.add(partition1);
        expectedOut.add(partition2);

        Assertions.assertEquals(expectedOut, outSet);
    }

    @Test
    public void testWithFourDocs(){
        Set<Document> testSet = new HashSet<>();
        testSet.add(skinnyturd);
        testSet.add(fatman);
        testSet.add(badinput);
        testSet.add(testDocument1);
        testSet.add(testDocument2);

        Set<Set<Document>> outSet = GroupingDocuments.groupBySimilarity(testSet, 3);

        Set<Set<Document>> expectedOut = new HashSet<>();
        Set<Document> partition1 = new HashSet<>();
        Set<Document> partition2 = new HashSet<>();
        Set<Document> partition3 = new HashSet<>();
        partition1.add(badinput);
        partition2.add(testDocument1);
        partition2.add(testDocument2);
        partition3.add(skinnyturd);
        partition3.add(fatman);
        expectedOut.add(partition1);
        expectedOut.add(partition2);
        expectedOut.add(partition3);

        Assertions.assertEquals(expectedOut, outSet);

        int i=1;
        for(Set<Document> docSet : outSet){
            int j=1;
            System.out.println(i + ") ");
            for(Document doc : docSet){
                System.out.println("--" + j + ". " + doc.getDocId());
                j++;
            }
            i++;
        }
    }

}
