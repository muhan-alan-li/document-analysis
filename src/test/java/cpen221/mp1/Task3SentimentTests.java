package cpen221.mp1;

import com.google.protobuf.InvalidProtocolBufferException;
import cpen221.mp1.exceptions.NoSuitableSentenceException;
import cpen221.mp1.sentiments.SentimentAnalysis;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class Task3SentimentTests {

    private static cpen221.mp1.Document testDocument1;
    private static cpen221.mp1.Document testDocument2;
    private static cpen221.mp1.Document skinnyturd;
    private static cpen221.mp1.Document positive;
    private static cpen221.mp1.Document manyhate;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        testDocument1 = new cpen221.mp1.Document("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new cpen221.mp1.Document("The Ant and The Cricket", new URL("http://textfiles.com/stories/antcrick.txt"));
        skinnyturd    = new cpen221.mp1.Document("skinnyturd", "resources/skinnyturd.txt");
        positive      = new cpen221.mp1.Document("positive", "resources/positive.txt");
        manyhate      = new cpen221.mp1.Document("manyhate","resources/manyhate.txt");

    }

    @Test
    public void getPositiveSentence() throws NoSuitableSentenceException {
        System.out.println(testDocument1.getMostPositiveSentence()); // For debugging
        Assertions.assertEquals("\"Well, try dancing now!\"", testDocument1.getMostPositiveSentence());
    }

    @Test
    public void getNegativeSentence() throws NoSuitableSentenceException {
        System.out.println(testDocument1.getMostNegativeSentence()); // For debugging
        Assertions.assertEquals("said the ant.", testDocument1.getMostNegativeSentence());
    }

    @Test
    public void crossChecking() throws NoSuitableSentenceException{
        Assertions.assertEquals(testDocument1.getMostPositiveSentence(), testDocument2.getMostPositiveSentence());
    }

    @Test
    public void testSkinnyturd() throws NoSuitableSentenceException {
        try {
            Assertions.assertEquals("", skinnyturd.getMostPositiveSentence());
        }catch(NoSuitableSentenceException e){
            System.out.println("No positive sentence");
        }
        Assertions.assertEquals("the skinny nerd took a big fat turd with a bird.", skinnyturd.getMostNegativeSentence());
    }

    @Test
    public void testNoNegDoc() throws NoSuitableSentenceException{
        try{
            Assertions.assertEquals("", positive.getMostNegativeSentence());
        }catch(NoSuitableSentenceException e){
            System.out.println("No negative sentence");
        }

        Assertions.assertEquals("Pooping is fun.", positive.getMostPositiveSentence());

    }

    @Test
    public void testSentimentAnalysisConstructor(){
        SentimentAnalysis skinnyturdAnalysis = new SentimentAnalysis(skinnyturd);
        Assertions.assertEquals("the skinny nerd took a big fat turd with a bird.", skinnyturdAnalysis.getMostNeg());
    }

    @Test
    public void testManyHate() throws NoSuitableSentenceException{
        Assertions.assertEquals("I hate you with an undying passion, you are my worst enemy, I hope you die by the worst form of torture known to mankind.", manyhate.getMostNegativeSentence());
    }
}

