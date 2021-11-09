package cpen221.mp1.similarity;

import cpen221.mp1.Document;

public class DocPair {

    private final Document doc1;
    private final Document doc2;
    private final double   divScore;

    /**
     * Constructor method, creates a class that contains a pair of docs and their divergence score
     * Assumes the two docs are not identical
     * @param doc1 the first doc
     * @param doc2 the second doc
     */
    public DocPair(Document doc1, Document doc2){
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.divScore = new DocumentSimilarity(doc1, doc2).getDivergenceScore();
    }

    /**
     * A getter function
     * @return the divergence score of a given pair of docs
     */
    public double getDivScore(){
        return this.divScore;
    }

    /**
     * A getter function
     * @return the first doc in a given pair of docs
     */
    public Document getDoc1(){
        return doc1;
    }

    /**
     * A getter function
     * @return the second doc in a given pair of docs
     */
    public Document getDoc2(){
        return doc2;
    }
}

