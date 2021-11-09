package cpen221.mp1;

import cpen221.mp1.exceptions.NoSuitableSentenceException;
import cpen221.mp1.sentiments.SentimentAnalysis;
import com.google.cloud.language.v1.Sentiment;

import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.text.BreakIterator;
import java.util.*;

public class Document {

    /* ------- Task 0 ------- */
    /*  all the basic things  */
    private final List<String> doc = new ArrayList<>();
    private final List<String> docWords = new ArrayList<>();
    private String docId;

    private final HashSet<String> uniqueWords = new HashSet<>();
    private final HashMap<String, Integer> hapaxLegomanaWords = new HashMap<>();
    private final List<String> arrContainingHapaxKeys = new ArrayList<>();
    private SentimentAnalysis sentimentResults;
    private boolean sentimentStatus = false;

    private final static String invalidCharsAtStart =
        "! \" $ % & ' ( ) * + , - . / : ; < = > ? @ [ \\ ] ^ _ ` { | } ~";
    private final static String invalidCharsAtEnd =
        "! # \" $ % & ' ( ) * + , - . / : ; < = > ? @ [ \\ ] ^ _ ` { | } ~";
    private final List<Character> invalidCharactersAtStart = new ArrayList<>();
    private final List<Character> invalidCharactersAtEnd = new ArrayList<>();

    /**
     * Create a new document using a URL
     *
     * @param docId  the document identifier
     * @param docURL the URL with the contents of the document
     */
    public Document(String docId, URL docURL) {
        List<String> tempDoc = new ArrayList<>();
        //this code block was taken from the MP1 file on notion, minor adjustments were made
        try {
            Scanner urlScanner = new Scanner(docURL.openStream());
            while (urlScanner.hasNext()) {
                tempDoc.add(urlScanner.nextLine());
            }
            init(tempDoc);
            this.docId = docId;
        } catch (IOException ioe) {
            System.out.println("Problem reading from URL!");
        }
    }

    /**
     * Creates a new document from given filepath
     *
     * @param docId    the document identifier
     * @param fileName the name of the file with the contents of
     *                 the document
     */
    public Document(String docId, String fileName) {
        List<String> tempDoc = new ArrayList<>();
        //this code block was taken from the MP1 file on notion, minor adjustments were made
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null;
                 fileLine = reader.readLine()) {
                tempDoc.add(fileLine);
            }
            reader.close();
            init(tempDoc);
            this.docId = docId;
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }
    }

    /**
     * Initializes a Document, taking in a List of String from the constructor and using it to
     * init the variable doc in the Document datatype
     *
     * @param lineDoc a list of String, the init method will combine the contents of the list into
     *                one massive String, then use a breakIterator to break the document into
     *                sentence. {@code the only List ever passed as linedoc should be the tempDoc
     *                variable used in the constructor methods}
     */
    private void init(List<String> lineDoc) {
        List<String> sentenceDoc = new ArrayList<>();

        //making a massive run-on string of the entire document
        StringBuilder sb = new StringBuilder();
        for (String line : lineDoc) {
            sb.append(line);
            sb.append(
                ' '); // Added a space to the end of each line to account for words separated by \n character. Possibility of consecutive spaces, but that is caught by later code.
        }

        String strDoc = sb.toString().trim()
            .replaceAll(" +", " "); // improved strDoc by removing consecutive duplicate whitespaces

        //"borrowed" this iterator code from the MP1 notion page
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(strDoc);

        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String sentence = strDoc.substring(start, end);
            doc.add(sentence);
        }

        // Removing possible blank space at end of each sentence
        for (int i = 0; i < doc.size(); i++) {
            doc.set(i, doc.get(i).trim());
        }

        // Defining set of illegal characters at the start of a word:
        String illegalChars = invalidCharsAtStart.replaceAll(" ", "");
        for (int i = 0; i < illegalChars.length(); i++) {
            invalidCharactersAtStart.add(illegalChars.charAt(i));
        }

        // Defining set of illegal characters at end of a word:
        String illegalCharsAtEnd = invalidCharsAtEnd.replaceAll(" ", "");
        for (int i = 0; i < illegalCharsAtEnd.length(); i++) {
            invalidCharactersAtEnd.add(illegalCharsAtEnd.charAt(i));
        }

        // Converting sentence to words
        for (String s : doc) {
            String[] temp;
            temp = s.toLowerCase(Locale.US).split(" +");
            for (String value : temp) {
                if (!cleanWords(value).isBlank()) {
                    docWords.add(cleanWords(value));
                }
            }
        }
        // Initializing HapaxLegomanaWords
        for (String str : docWords) {
            if (hapaxLegomanaWords.get(str) == null) {
                hapaxLegomanaWords.put(str, 1);
                arrContainingHapaxKeys.add(str);
            } else {
                hapaxLegomanaWords.put(str, hapaxLegomanaWords.get(str) + 1);
            }
        }

        // Initializing uniqueWords
        uniqueWords.addAll(docWords);
    }

    /**
     * Remove punctuation from start to end of word, does not remove duplicate #'s
     *
     * @param word word to clean
     * @return cleaned word
     */
    private String cleanWords(String word) {
        if (word.length() == 0) {
            return word;
        }
        if (invalidCharactersAtStart.contains(word.charAt(0))) {
            return cleanWords(word.substring(1));
        }
        if (invalidCharactersAtEnd.contains(word.charAt(word.length() - 1))) {
            return cleanWords(word.substring(0, word.length() - 1));
        } else {
            return word;
        }
    }

    /**
     * Obtain the identifier for this document
     *
     * @return the identifier for this document
     */
    public String getDocId() {
        return this.docId;
    }

    /* ------- Task 1 ------- */
    /**
     * Gets the average word length of a word in a (text) file
     *
     * @return The average length of a word in the document, if no words then 0
     */
    public double averageWordLength() {
        int wordCount = 0;

        for (String docWord : docWords) {
            wordCount += docWord.length();
        }
        if (docWords.size() == 0){
            return 0;
        }
        return (double) wordCount / docWords.size();
    }

    /**
     * Divides the total number of unique words by the total number of words
     *
     * @return The ratio between unique words and total words
     */
    public double uniqueWordRatio() {
        if (docWords.size() == 0) {
            return 0;
        } else {
            return (double) uniqueWords.size() / docWords.size();
        }
    }

    /**
     * @return The number of words occurring exactly once in the document to the total number of words
     */
    public double hapaxLegomanaRatio() {
        int numWordsOccurOnce = 0;

        for (String str : arrContainingHapaxKeys) {
            if (hapaxLegomanaWords.get(str) == 1) {
                numWordsOccurOnce++;
            }
        }
        if (docWords.size() == 0) {
            return 0;
        } else {
            return (double) numWordsOccurOnce / docWords.size();
        }
    }

    /**
     * Counts number of words in a document
     *
     * @return The number of words in the document
     */
    private double getWordCount() {
        return docWords.size();
    }

    /* ------- Task 2 ------- */

    /**
     * Obtain the number of sentences in the document
     *
     * @return the number of sentences in the document
     */
    public int numSentences() {
        return doc.size();
    }

    /**
     * Obtain a specific sentence from the document.
     * Sentences are numbered starting from 1.
     *
     * @param sentence_number the position of the sentence to retrieve,
     *                        {@code 1 <= sentence_number <= this.getSentenceCount()}
     * @return the sentence indexed by {@code sentence_number}
     */
    public String getSentence(int sentence_number) {
        return doc.get(sentence_number - 1);
    }

    /**
     * Obtains the average length of a sentence in the document by diving
     * the number of words in the document by the number of sentences
     *
     * @return The average length of a sentence in the document
     */
    public double averageSentenceLength() {
        return getWordCount() / numSentences();
    }

    /**
     * Obtains the average sentence complexity of the document by dividing the number of phrases
     * by the number of sentences
     * <p>
     * A phrase is a subset of a sentence delimited by a colon, semi-colon or comma
     *
     * @return The average sentence complexity in the document
     */
    public double averageSentenceComplexity() {
        int phraseCount = 0;

        for (String s : doc) {
            boolean onePhraseSentence = true;
            boolean compensateOne = true;
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == ',' || s.charAt(j) == ';' || s.charAt(j) == ':') {
                    phraseCount++;
                    onePhraseSentence = false;
                }
                if (j == s.length() - 1 && onePhraseSentence) {
                    phraseCount++;
                    onePhraseSentence = false;
                    compensateOne = false;
                }
            }
            if (compensateOne) {
                phraseCount++;
            }
        }
        return (double) phraseCount / numSentences();
    }

    /* ------- Task 3 ------- */

//    To implement these methods while keeping the class
//    small in terms of number of lines of code,
//    implement the methods fully in sentiments.SentimentAnalysis
//    and call those methods here. Use the getSentence() method
//    implemented in this class when you implement the methods
//    in the SentimentAnalysis class.

//    Further, avoid using the Google Cloud AI multiple times for
//    the same document. Save the results (cache them) for
//    reuse in this class.

//    This approach illustrates how to keep classes small and yet
//    highly functional.

    /**
     * Obtain the sentence with the most positive sentiment in the document
     *
     * @return the sentence with the most positive sentiment in the
     * document; when multiple sentences share the same sentiment value
     * returns the sentence that appears later in the document
     * @throws NoSuitableSentenceException if there is no sentence that
     *                                     expresses a positive sentiment
     */
    public String getMostPositiveSentence() throws NoSuitableSentenceException {
        if (!sentimentStatus) {
            sentimentResults = new SentimentAnalysis(this);
            sentimentStatus = true;
        }
        if (sentimentResults.getMostPos().isBlank()) {
            throw new NoSuitableSentenceException();
        }
        return sentimentResults.getMostPos();
    }

    /**
     * Obtain the sentence with the most negative sentiment in the document
     *
     * @return the sentence with the most negative sentiment in the document;
     * when multiple sentences share the same sentiment value, returns the
     * sentence that appears later in the document
     * @throws NoSuitableSentenceException if there is no sentence that
     *                                     expresses a negative sentiment
     */
    public String getMostNegativeSentence() throws NoSuitableSentenceException {
        if (!sentimentStatus) {
            sentimentResults = new SentimentAnalysis(this);
            sentimentStatus = true;
        }
        if (sentimentResults.getMostNeg().isBlank()) {
            throw new NoSuitableSentenceException();
        }
        return sentimentResults.getMostNeg();
    }
}



