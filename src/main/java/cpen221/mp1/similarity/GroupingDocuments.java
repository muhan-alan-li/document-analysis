package cpen221.mp1.similarity;

import cpen221.mp1.Document;

import java.util.*;

public class GroupingDocuments {

    /* ------- Task 5 ------- */

    /**
     * Group documents by similarity
     * @param allDocuments the set of all documents to be considered,
     *                     is not null
     * @param numberOfGroups the number of document groups to be generated
     * @return groups of documents, where each group (set) contains similar
     * documents following this rule: if D_i is in P_x, and P_x contains at
     * least one other document, then P_x contains some other D_j such that
     * the divergence between D_i and D_j is smaller than (or at most equal
     * to) the divergence between D_i and any document that is not in P_x.
     */
    public static Set<Set<Document>> groupBySimilarity(Set<Document> allDocuments, int numberOfGroups) {
        int numOfMerges = allDocuments.size() - numberOfGroups;
        List<Set<Document>> output = new ArrayList<>();
        //initialize output list
        for(Document doc : allDocuments){
            Set<Document> partition = new HashSet<>();
            partition.add(doc);
            output.add(partition);
        }

        List<DocPair> divScoreList = getSortedList(allDocuments);
        for(int i=0; i<numOfMerges; i++){
            DocPair pairToMerge = divScoreList.get(i);
            Set<Document> set1 = new HashSet<>();
            Set<Document> set2 = new HashSet<>();

            //finding set1 and set2
            for(int j=0; j<output.size(); j++){
                Set<Document> set = output.get(j);
                if(set.contains(pairToMerge.getDoc1()) && !set.contains(pairToMerge.getDoc2())){
                    set1 = set;
                    output.remove(set);
                    j--;
                }

                if(set.contains(pairToMerge.getDoc2())){
                    set2 = set;
                    output.remove(set);
                    j--;
                }
            }

            //merge set1 and set2 and place it back into output
            Set<Document> mergedSet = new HashSet<>();
            if(!set1.isEmpty() && !set2.isEmpty()){
                mergedSet.addAll(set1);
                mergedSet.addAll(set2);
            }
            output.add(mergedSet);
        }
        //quickfix to turn output into a set
        return new HashSet<>(output);
    }


    private static List<DocPair> getSortedList(Set<Document> allDocs){
        List<DocPair> output = new ArrayList<>();
        List<Document> docList = new ArrayList<>();
        docList.addAll(allDocs);

        //initialize list
        for(int i=0; i < docList.size(); i++){
            Document doc1 = docList.get(i);
            for(int j=i+1; j < docList.size(); j++){
                if(i!=j){
                    Document doc2 = docList.get(j);
                    output.add(new DocPair(doc1, doc2));
                }
            }
        }

        //sort list
        output.sort(Comparator.comparing(DocPair::getDivScore));

        return output;
    }
}
