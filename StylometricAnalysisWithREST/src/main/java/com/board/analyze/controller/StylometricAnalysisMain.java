package com.board.analyze.controller;

/**
 *
 * @author ITE
 */
import com.board.analyze.IOHandler.IOProperties;
import com.board.analyze.IOHandler.IOReadWrite;
import com.board.analyze.model.Alias;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * This is some code for doing stylometric matching of aliases based on posts
 * (such as discussion board messages). Features: letters (26), digits (10),
 * punctuation (11), function words (293), word length (20), sentence length
 * (6). Except for freq. of sentence lengths, this is a subset of the features
 * used in Narayanan et al. (On the Feasibility of Internet-Scale Author
 * Identification)
 *
 * Some problems to consider: The more features, the more "sparse" the feature
 * vectors will be (many zeros) in case of few posts --> similar feature vectors
 * due to a majority of zeros
 *
 * Since all features are not of the same "dimension", it makes sense to
 * normalize/standardize the features to have mean 0 and variance 1, as in
 * Narayanan et al. The above standardization works when finding the best
 * matching candidate, but may be problematic since the "similarity" between two
 * aliases will depend on the features of other aliases (since the
 * standardization works column/(feature)-wise).
 *
 * If we do not use normalization/standardization, we cannot use feature which
 * are not frequencies, since the features with large magnitudes otherwise will
 * dominate completely!!! Even if we do only use frequencies, the results
 * without normalization seems poor (good with normalization) Try to improve the
 * unnormalized version before using it on real problems...
 *
 * Observe that the obtained similarity values cannot be used directly as a
 * measure of the "match percentage"!
 *
 *
 * @author frejoh
 *
 */
public class StylometricAnalysisMain {

    private Set<String> functionWords;			// Contains the function words we are using
    private static String path = "C:\\Users\\ITE\\Documents\\NetBeansProjects\\StylometricAnalysisWithREST\\StylometricAnalysisWithREST\\src\\main\\resources\\functionWord\\function_words.txt"; 	// TODO: Change to the correct path;
    private List<Alias> aliases;				// The aliases we are interested in to compare        
    private List<List<Float>> featVectorForAllAliases;

    public StylometricAnalysisMain() {
        functionWords = new LinkedHashSet<String>();
        loadFunctionWords();
        aliases = new ArrayList<Alias>();
    }

    public List<Float> executeAnalysis(String ID) throws IOException, SQLException {
        IOReadWrite ioRW = new IOReadWrite();
        Alias user = new Alias();       
        String basePath = IOProperties.INDIVIDUAL_USER_FILE_PATH;
        String ext = IOProperties.USER_FILE_EXTENSION;
        
        user =  ioRW.convertTxtFileToAliasObj(basePath, ID, ext);
        List<Float> freatuteVector = createFeatureVectors(user);
        return freatuteVector;
    }
    
    public List<Float> executePostAnalysis(List posts){
//        List<String> posts = new ArrayList<String>();
//        posts.add(post);
        Alias user = new Alias();  
        user.setPosts(posts);
         List<Float> freatuteVector = createFeatureVectors(user);
        return freatuteVector;        
    }

    /**
     * Extract words from text string, remove punctuation etc.
     *
     * @param text
     * @return
     */
    public static List<String> extractWords(String text) {
        List<String> wordList = new ArrayList<String>();
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "");
            wordList.add(words[i]);
        }
        return wordList;
    }

    /**
     * Load the list of function words from file
     */
    public void loadFunctionWords() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                functionWords.add(strLine);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a list containing the number of occurrences of the various
     * function words in the post (list of extracted words)
     *
     * @param words
     * @return
     */
    public ArrayList<Float> countFunctionWords(List<String> words) {
        ArrayList<Float> tmpCounter = new ArrayList<Float>(Collections.nCopies(functionWords.size(), 0.0f));	// Initialize to zero
        for (int i = 0; i < words.size(); i++) {
            if (functionWords.contains(words.get(i))) {
                float value = (Float) tmpCounter.get(i);
                value++;
                tmpCounter.set(i, value);
            }
        }
        // "Normalize" the values by dividing with length of the post (nr of words in the post)
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) words.size());
        }
        return tmpCounter;
    }

    /**
     * Create a list containing the number of occurrences of letters a to z in
     * the text
     *
     * @param post
     * @return
     */
    public ArrayList<Float> countCharactersAZ(String post) {
        post = post.toLowerCase();	// Upper or lower case does not matter, so make all letters lower case first...
        char[] ch = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        ArrayList<Float> tmpCounter = new ArrayList<Float>(Collections.nCopies(ch.length, 0.0f));
        for (int i = 0; i < ch.length; i++) {
            int value = countOccurrences(post, ch[i]);
            tmpCounter.set(i, (float) value);
        }
        // "Normalize" the values by dividing with total nr of characters in the post (excluding white spaces)
        int length = post.replaceAll(" ", "").length();
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) length);
        }
        return tmpCounter;
    }

    /**
     * Create a list containing the number of special characters in the text
     *
     * @param post
     * @return
     */
    public ArrayList<Float> countSpecialCharacters(String post) {
        post = post.toLowerCase();	// Upper or lower case does not matter, so make all letters lower case first...
        char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '?', '!', ',', ';', ':', '(', ')', '"', '-', '´'};
        ArrayList<Float> tmpCounter = new ArrayList<Float>(Collections.nCopies(ch.length, 0.0f));
        for (int i = 0; i < ch.length; i++) {
            int value = countOccurrences(post, ch[i]);
            tmpCounter.set(i, (float) value);
        }
        // "Normalize" the values by dividing with total nr of characters in the post (excluding whitespaces)
        int length = post.replaceAll(" ", "").length();
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) length);
        }
        return tmpCounter;
    }

    /**
     * Counts the frequency of various word lengths in the list of words.
     *
     * @param words
     * @return
     */
    public ArrayList<Float> countWordLengths(List<String> words) {
        ArrayList<Float> tmpCounter = new ArrayList<Float>(Collections.nCopies(20, 0.0f));	// Where 20 corresponds to the number of word lengths of interest 
        int wordLength = 0;
        for (String word : words) {
            wordLength = word.length();
            // We only care about wordLengths in the interval 1-20
            if (wordLength > 0 && wordLength <= 20) {
                float value = (Float) tmpCounter.get(wordLength - 1);	// Observe that we use wordLength-1 as index!
                value++;
                tmpCounter.set(wordLength - 1, value);
            }
        }
        // "Normalize" the values by dividing with length of the post (nr of words in the post)
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) words.size());
        }
        return tmpCounter;
    }

    /**
     * Counts the frequency of various sentence lengths in the post.
     *
     * @param post
     * @return
     */
    public ArrayList<Float> countSentenceLengths(String post) {
        ArrayList<Float> tmpCounter = new ArrayList<Float>(Collections.nCopies(6, 0.0f));	// Where 6 corresponds to the number of sentence lengths of interest
        // Split the post into a number of sentences
        List<String> sentences = splitIntoSentences(post);
        int nrOfWords = 0;
        for (String sentence : sentences) {
            // Get number of words in the sentence
            List<String> words = extractWords(sentence);
            nrOfWords = words.size();
            if (nrOfWords > 0 && nrOfWords <= 10) {
                tmpCounter.set(0, tmpCounter.get(0) + 1);
            } else if (nrOfWords <= 20) {
                tmpCounter.set(1, tmpCounter.get(1) + 1);
            } else if (nrOfWords <= 30) {
                tmpCounter.set(2, tmpCounter.get(2) + 1);
            } else if (nrOfWords <= 40) {
                tmpCounter.set(3, tmpCounter.get(3) + 1);
            } else if (nrOfWords <= 50) {
                tmpCounter.set(4, tmpCounter.get(4) + 1);
            } else if (nrOfWords >= 51) {
                tmpCounter.set(5, tmpCounter.get(5) + 1);
            }
        }
        // "Normalize" the values by dividing with nr of sentences in the post
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) sentences.size());
        }
        return tmpCounter;
    }

    /**
     * Splits a post/text into a number of sentences
     *
     * @param text
     * @return
     */
    public List<String> splitIntoSentences(String text) {
        List<String> sentences = new ArrayList<String>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            sentences.add(text.substring(start, end));
        }
        return sentences;
    }

    /**
     * Count the number of occurrences of certain character in a String
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    /**
     * Loops through all aliases and construct their feature vectors
     */
    public List<Float> createFeatureVectors(Alias user) {
        List<Float> featureVector = new ArrayList<Float>();
        featVectorForAllAliases = new ArrayList<List<Float>>();
      //  for (Alias alias : aliases) {
            int cnt = 0;
            user.setFeatureVectorPosList(user.initializeFeatureVectorPostList());
            // Calculate each part of the "feature vector" for each individual post
            for (String post : user.getPosts()) {
                List<String> wordsInPost = extractWords(post);
                user.addToFeatureVectorPostList(countFunctionWords(wordsInPost), 0, cnt);
                user.addToFeatureVectorPostList(countWordLengths(wordsInPost), 293, cnt);
                user.addToFeatureVectorPostList(countCharactersAZ(post), 313, cnt);
                user.addToFeatureVectorPostList(countSpecialCharacters(post), 339, cnt);
                cnt++;
         //   }

            ArrayList<ArrayList<Float>> featureVectorList = user.getFeatureVectorPosList();

            int numberOfPosts = user.getPosts().size();
            int nrOfFeatures = featureVectorList.get(0).size();
            featureVector = new ArrayList<Float>(Collections.nCopies(nrOfFeatures, 0.0f));
            // Now we average over all posts to create a single feature vector for each alias
            for (int i = 0; i < nrOfFeatures; i++) {
                float value = 0.0f;
                for (int j = 0; j < numberOfPosts; j++) {
                    value += featureVectorList.get(j).get(i);
                }
                value /= numberOfPosts;
                featureVector.set(i, value);
            }
            user.setFeatureVector(featureVector);
            featVectorForAllAliases.add(featureVector);
        }
            return featureVector;
        //normalizeFeatureVector();
    }
    
    /**
     * Loops through all aliases and construct their feature vectors
     */
    public void createFeatureVectors(List<Alias> user) {
        List<Float> featureVector = new ArrayList<Float>();
        featVectorForAllAliases = new ArrayList<List<Float>>();
        for (Alias alias : user) {
            int cnt = 0;
            alias.setFeatureVectorPosList(alias.initializeFeatureVectorPostList());
            // Calculate each part of the "feature vector" for each individual post
            for (String post : alias.getPosts()) {
                List<String> wordsInPost = extractWords(post);
                alias.addToFeatureVectorPostList(countFunctionWords(wordsInPost), 0, cnt);
                alias.addToFeatureVectorPostList(countWordLengths(wordsInPost), 293, cnt);
                alias.addToFeatureVectorPostList(countCharactersAZ(post), 313, cnt);
                alias.addToFeatureVectorPostList(countSpecialCharacters(post), 339, cnt);
                cnt++;
            }

            ArrayList<ArrayList<Float>> featureVectorList = alias.getFeatureVectorPosList();

            int numberOfPosts = alias.getPosts().size();
            int nrOfFeatures = featureVectorList.get(0).size();
            featureVector = new ArrayList<Float>(Collections.nCopies(nrOfFeatures, 0.0f));
            // Now we average over all posts to create a single feature vector for each alias
            for (int i = 0; i < nrOfFeatures; i++) {
                float value = 0.0f;
                for (int j = 0; j < numberOfPosts; j++) {
                    value += featureVectorList.get(j).get(i);
                }
                value /= numberOfPosts;
                featureVector.set(i, value);
            }
            alias.setFeatureVector(featureVector);
            featVectorForAllAliases.add(featureVector);
        }
        normalizeFeatureVector();  
    }

    /**
     * Used for comparing two feature vectors
     *
     * @param featVector1
     * @param featVector2
     * @return
     */
    public double compareFeatureVectors(List<Float> featVector1, List<Float> featVector2) {
        List<Float> floatList = featVector1;
        float[] floatArray1 = new float[floatList.size()];

        for (int i = 0; i < floatList.size(); i++) {
            Float f = floatList.get(i);
            floatArray1[i] = (f != null ? f : Float.NaN);
        }

        List<Float> floatList2 = featVector2;
        float[] floatArray2 = new float[floatList2.size()];

        for (int i = 0; i < floatList2.size(); i++) {
            Float f = floatList2.get(i);
            floatArray2[i] = (f != null ? f : Float.NaN);
        }
        return calculateSimilarity(floatArray1, floatArray2);
    }

    /**
     * Calculates cosine similarity between two real vectors
     *
     * @param value1
     * @param value2
     * @return
     */
    public double calculateSimilarity(float[] value1, float[] value2) {
        float sum = 0.0f;
        float sum1 = 0.0f;
        float sum2 = 0.0f;
        for (int i = 0; i < value1.length; i++) {
            float v1 = value1[i];
            float v2 = value2[i];
            if ((!Float.isNaN(v1)) && (!Float.isNaN(v2))) {
                sum += v2 * v1;
                sum1 += v1 * v1;
                sum2 += v2 * v2;
            }
        }
        if ((sum1 > 0) && (sum2 > 0)) {
            double result = sum / (Math.sqrt(sum1) * Math.sqrt(sum2));
            // result can be > 1 (or -1) due to rounding errors for equal vectors, 
            //but must be between -1 and 1
            return Math.min(Math.max(result, -1d), 1d);
            //return result;
        } else if (sum1 == 0 && sum2 == 0) {
            return 1d;
        } else {
            return 0d;
        }
    }

    /**
     * Calculate similarity between all pairs of aliases (a lot of comparisons
     * if there are many aliases)
     */
    public void compareAllPairsOfAliases() {
        for (int i = 0; i < aliases.size(); i++) {
            for (int j = i + 1; j < aliases.size(); j++) {
                double sim = compareFeatureVectors(aliases.get(i).getFeatureVector(), aliases.get(j).getFeatureVector());
                System.out.println("Similarity between alias " + aliases.get(i).getUserID() + " and " + aliases.get(j).getUserID() + " is: " + sim);
            }
        }
    }

    /**
     * Find the index of the alias that is most similar to the selected alias.
     *
     * @param index
     * @return
     */
    public int findBestMatch(int index) {
        double highestSimilarity = -10.0;
        int indexMostSimilar = 0;
        for (int i = 0; i < aliases.size(); i++) {
            if (i != index) {
                double sim = compareFeatureVectors(aliases.
                        get(i).getFeatureVector(),
                        aliases.get(index).getFeatureVector());
                if (sim > highestSimilarity) {
                    highestSimilarity = sim;
                    indexMostSimilar = i;
                }
            }
        }
        return indexMostSimilar;
    }

    /**
     * Standardize/normalize the feature vectors for all aliases. Aim is mean 0
     * and variance 1 for each feature vector. Please note that this will result
     * in feature vectors that depend on the feature vectors of the other
     * aliases...
     */
    public void normalizeFeatureVector() {
        int nrOfFeatures = featVectorForAllAliases.get(0).size();
        List<Double> avgs = new ArrayList<Double>(nrOfFeatures);
        List<Double> stds = new ArrayList<Double>(nrOfFeatures);

        // Calculate avg (mean) for each feature
        for (int i = 0; i < nrOfFeatures; i++) {
            double sum = 0.0;
            for (int j = 0; j < aliases.size(); j++) {
                sum += featVectorForAllAliases.get(j).get(i);
            }
            avgs.add(sum / aliases.size());
        }

        // Calculate std for each feature
        for (int i = 0; i < nrOfFeatures; i++) {
            double avg = avgs.get(i);
            double tmp = 0.0;
            for (int j = 0; j < aliases.size(); j++) {
                tmp += (avg - featVectorForAllAliases.get(j).get(i)) * (avg - featVectorForAllAliases.get(j).get(i));
            }
            stds.add(Math.sqrt(tmp / aliases.size()));
        }

        // Do the standardization of the feature vectors
        for (int i = 0; i < nrOfFeatures; i++) {
            for (int j = 0; j < aliases.size(); j++) {
                if (stds.get(i) == 0) {
                    aliases.get(j).setFeatureValue(i, 0.0f);
                } else {
                    aliases.get(j).setFeatureValue(i, (float) ((featVectorForAllAliases.get(j).get(i) - avgs.get(i)) / stds.get(i)));
                }
            }
        }

    }
    
    /*public static void main(String args[]) throws SQLException, IOException{
        String userID = "1123";
        StylometricAnalysisMain init = new StylometricAnalysisMain();
        init.executeAnalysis(userID);
    }*/
}