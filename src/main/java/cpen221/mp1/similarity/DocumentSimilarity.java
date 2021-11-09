package cpen221.mp1.similarity;

import cpen221.mp1.Document;

import java.util.*;

public class DocumentSimilarity {

    /* DO NOT CHANGE THESE WEIGHTS */
    private static final int WT_AVG_WORD_LENGTH = 5;
    private static final int WT_UNIQUE_WORD_RATIO = 15;
    private static final int WT_HAPAX_LEGOMANA_RATIO = 25;
    private static final int WT_AVG_SENTENCE_LENGTH = 1;
    private static final int WT_AVG_SENTENCE_CPLXTY = 4;
    private static final int WT_JS_DIVERGENCE = 50;
    /* ---- END OF WEIGHTS ------ */

    private final double divergenceScore;

    /* ------- Task 4 ------- */

    /**
     * Constructor method
     *
     * @param doc1 doc1, to be compared to doc2
     * @param doc2 see above, but vice versa
     */
    public DocumentSimilarity(Document doc1, Document doc2) {
        divergenceScore = documentDivergence(doc1, doc2);
    }

    public double getDivergenceScore() {
        return divergenceScore;
    }

    /**
     * Compute the Jensen-Shannon Divergence between the given documents
     *
     * @param doc1 the first document, is not null
     * @param doc2 the second document, is not null
     * @return the Jensen-Shannon Divergence between the given documents
     */
    public static double jsDivergence(Document doc1, Document doc2) {
        HashMap<String, Integer> doc1UniqueWordsMap = getWordMapAndCount(doc1);
        HashSet<String> doc1UniqueWords = getUniqueWords(doc1);

        HashMap<String, Integer> doc2UniqueWordsMap = getWordMapAndCount(doc2);
        HashSet<String> doc2UniqueWords = getUniqueWords(doc2);

        Set<String> allwords = new HashSet<>();
        double outputProbability = 0;

        allwords.addAll(doc1UniqueWords);
        allwords.addAll(doc2UniqueWords);

        double p_i, q_i, m_i;
        double p_iterm, q_iterm;
        for (String word : allwords) {
            if (doc1UniqueWordsMap.get(word) == null) {
                p_i = 0;
            } else {
                p_i = (double) doc1UniqueWordsMap.get(word) / getDocWords(doc1).size();
            }

            if (doc2UniqueWordsMap.get(word) == null) {
                q_i = 0;
            } else {
                q_i = (double) doc2UniqueWordsMap.get(word) / getDocWords(doc2).size();
            }

            m_i = (p_i + q_i) / 2.0;

            if (p_i == 0) {
                p_iterm = 0;
            } else {
                p_iterm = (p_i * logBase2(p_i / m_i));
            }

            if (q_i == 0) {
                q_iterm = 0;
            } else {
                q_iterm = (q_i * logBase2(q_i / m_i));
            }

            outputProbability += p_iterm + q_iterm;
        }

        outputProbability = outputProbability / 2.0;

        return outputProbability;
    }

    /**
     * Outputs the base 2 logarithm of an input double
     *
     * @param num number to take the base 2 logarithm of
     * @return log_2 (num)
     */
    private static double logBase2(double num) {
        return java.lang.Math.log(num) / java.lang.Math.log(2);
    }


    /**
     * Compute the Document Divergence between the given documents
     *
     * @param doc1 the first document, is not null
     * @param doc2 the second document, is not null
     * @return the Document Divergence between the given documents
     */
    public static double documentDivergence(Document doc1, Document doc2) {
        // Use the provided weights in computing the document divergence
        double outputDivergence = 0;

        outputDivergence += WT_AVG_WORD_LENGTH *
            java.lang.Math.abs(doc1.averageWordLength() - doc2.averageWordLength());

        outputDivergence += WT_UNIQUE_WORD_RATIO *
            java.lang.Math.abs(doc1.uniqueWordRatio() - doc2.uniqueWordRatio());

        outputDivergence += WT_HAPAX_LEGOMANA_RATIO *
            java.lang.Math.abs(doc1.hapaxLegomanaRatio() - doc2.hapaxLegomanaRatio());

        outputDivergence += WT_AVG_SENTENCE_LENGTH *
            java.lang.Math.abs(doc1.averageSentenceLength() - doc2.averageSentenceLength());

        outputDivergence += WT_AVG_SENTENCE_CPLXTY *
            java.lang.Math.abs(doc1.averageSentenceComplexity() - doc2.averageSentenceComplexity());

        outputDivergence += WT_JS_DIVERGENCE * jsDivergence(doc1, doc2);

        return outputDivergence;
    }

    /**
     * Helper method to return the words and count of each word of doc
     *
     * @param doc doc to find words + count
     * @return Hashmap containing words + count
     */
    private static HashMap<String, Integer> getWordMapAndCount(Document doc) {
        HashMap<String, Integer> AllwordsMapAndCount = new HashMap<>();
        for (String str : getDocWords(doc)) {
            if (AllwordsMapAndCount.get(str) == null) {
                AllwordsMapAndCount.put(str, 1);
            } else {
                AllwordsMapAndCount.put(str, AllwordsMapAndCount.get(str) + 1);
            }
        }
        return AllwordsMapAndCount;
    }

    /**
     * Helper method to get all unique words in each doc
     *
     * @param doc doc to find unique words
     * @return Hashset containing all unique words
     */
    private static HashSet<String> getUniqueWords(Document doc) {
        return new HashSet<>(getDocWords(doc));
    }

    /**
     * Helper method to get all doc words.
     *
     * @param doc document to extract words from
     * @return List of all words.
     */
    public static List<String> getDocWords(Document doc) {
        List<String> output = new ArrayList<>();

        List<Character> invStart = new ArrayList<>();
        // Defining set of illegal characters at the start of a word:
        String invalidCharsAtStart =
            "! \" $ % & ' ( ) * + , - . / : ; < = > ? @ [ \\ ] ^ _ ` { | } ~";
        String illegalChars = invalidCharsAtStart.replaceAll(" ", "");
        for (int i = 0; i < illegalChars.length(); i++) {
            invStart.add(illegalChars.charAt(i));
        }

        List<Character> invEnd = new ArrayList<>();
        // Defining set of illegal characters at end of a word:
        String invalidCharsAtEnd =
            "! # \" $ % & ' ( ) * + , - . / : ; < = > ? @ [ \\ ] ^ _ ` { | } ~";
        String illegalCharsAtEnd = invalidCharsAtEnd.replaceAll(" ", "");
        for (int i = 0; i < illegalCharsAtEnd.length(); i++) {
            invEnd.add(illegalCharsAtEnd.charAt(i));
        }

        for (int i = 1; i <= doc.numSentences(); i++) {
            String s = doc.getSentence(i);
            String[] temp;
            temp = s.toLowerCase().split(" +");
            for (String word : temp) {
                String clean = cleanWords(word, invStart, invEnd);
                if (!clean.isBlank()) {
                    output.add(clean);
                }
            }
        }
        return output;
    }

    /**
     * Helper method to clean words.
     *
     * @param word     word to clean
     * @param invStart invalid characters at the start of a word
     * @param invEnd   invalid characters at the end of a word
     * @return The cleaned word.
     */
    private static String cleanWords(String word, List<Character> invStart,
                                     List<Character> invEnd) {

        if (word.length() == 0) {
            return word;
        }
        if (invStart.contains(word.charAt(0))) {
            return cleanWords(word.substring(1), invStart, invEnd);
        }
        if (invEnd.contains(word.charAt(word.length() - 1))) {
            return cleanWords(word.substring(0, word.length() - 1), invStart, invEnd);
        } else {
            return word;
        }
    }
}
