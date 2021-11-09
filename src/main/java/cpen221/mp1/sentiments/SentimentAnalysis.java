package cpen221.mp1.sentiments;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import cpen221.mp1.exceptions.*;
import cpen221.mp1.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentAnalysis {

    final static double THRESHOLD = 0.3;
    private String mostPos;
    private String mostNeg;

    /**
     * Constructs a SentimentAnalysis object that contains Strings mostPos and mostNeg
     * @param doc Document passed to sentiment analysis to be analysed
     */
    public SentimentAnalysis(Document doc){
        try {
            mostPos = getMostPositiveSentence(doc);
        }catch(NoSuitableSentenceException ignored) {
            mostPos = "";
        }
        try{
            mostNeg = getMostNegativeSentence(doc);
        }catch(NoSuitableSentenceException ignored){
            mostNeg = "";
        }
    }

    /**
     * getter function
     * @return most positive sentence of a given doc passed to sentiment analysis
     */
    public String getMostPos(){
        return mostPos;
    }

    /**
     * getter function
     * @return most negative sentence of a given doc passed to sentiment analysis
     */
    public String getMostNeg() {
        return mostNeg;
    }

    /**
     *
     * @param document Document to be analyzed
     * @return The most positive sentence in the document
     * @throws NoSuitableSentenceException - Most positive sentence could not be found
     */
    private static String getMostPositiveSentence(Document document) throws NoSuitableSentenceException {
        Map<String, Sentiment> sentimentMap = generateSentimentScores(document);
        String mostPos = null;
        float largestScore = 0;
        for (int i = 0; i < sentimentMap.size(); i++) {
            int sentenceNum = i + 1;
            Sentiment currentSent = sentimentMap.get(document.getSentence(sentenceNum));
            float currentScore = currentSent.getScore();
            if (currentScore >= THRESHOLD) {
                if (currentScore >= largestScore) {
                    mostPos = document.getSentence(sentenceNum);
                }
            }
        }
        if (mostPos == null) {
            throw new NoSuitableSentenceException();
        }
        return mostPos;
    }

    /**
     *
     * @param document Document to be analyzed
     * @return The most negative sentence in the document
     * @throws NoSuitableSentenceException - Most negative sentence could not be found
     */
    private static String getMostNegativeSentence(Document document) throws NoSuitableSentenceException {
        Map<String, Sentiment> sentimentMap = generateSentimentScores(document);
        String mostNeg = null;
        float smallestScore = 0;
        for (int i = 0; i < sentimentMap.size(); i++) {
            int sentenceNum = i + 1;
            Sentiment currentSent = sentimentMap.get(document.getSentence(sentenceNum));
            float currentScore = currentSent.getScore();
            if (currentScore <= -THRESHOLD) {
                if (currentScore <= smallestScore) {
                    mostNeg = document.getSentence(sentenceNum);
                }
            }
        }
        if (mostNeg == null) {
            throw new NoSuitableSentenceException();
        }
        return mostNeg;
    }

    /**
     * Generates a map of sentences and their sentiments
     *
     * @param document document to be analyzed by the method
     * @return generates a map with each sentence as key and their corresponding sentiments as value
     */
    public static HashMap<String, Sentiment> generateSentimentScores(Document document) {
        HashMap<String, Sentiment> output = new HashMap<>();
        for (int i=1; i<=document.numSentences(); i++) {
            String sentence = document.getSentence(i);
            try (LanguageServiceClient language = LanguageServiceClient.create()) {

                com.google.cloud.language.v1.Document doc
                        = com.google.cloud.language.v1.
                        Document.newBuilder().setContent(sentence).setType(Type.PLAIN_TEXT).build();

                AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
                Sentiment sentiment = response.getDocumentSentiment();
                if (sentiment != null) {
                    output.put(sentence, sentiment);
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
                throw new RuntimeException("Unable to communicate with Sentiment Analyzer!");
            }
        }
        return output;
    }
}
