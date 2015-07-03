package classification;

import cc.mallet.classify.*;
import cc.mallet.types.InstanceList;

import java.io.File;
import java.io.IOException;

public class ModelTester {

    public static void main (String[] args) throws IOException, ClassNotFoundException {

        DataImporter importer = new DataImporter();
        Evaluator evaluator = new Evaluator();
        Serializer serializer = new Serializer();

        InstanceList positiveTweets = importer.readFiles(new File("tweetsPositiveClean.csv"));
        InstanceList negativeTweets = importer.readFiles(new File("tweetsNegativeClean.csv"));

        NaiveBayesTrainer naiveBayesTrainer = new NaiveBayesTrainer().setDocLengthNormalization(0.5);
        NaiveBayes naiveBayes = naiveBayesTrainer.train(positiveTweets);

        MaxEntTrainer maxEntTrainer = new MaxEntTrainer(1.0);
        MaxEnt maxEnt = maxEntTrainer.train(negativeTweets, 5);

        evaluator.crossValidationStandard(positiveTweets, naiveBayesTrainer, naiveBayes, 10);   // Test the classifier using positive tweets for training
        evaluator.crossValidationStandard(negativeTweets, maxEntTrainer, maxEnt, 10);   // Test the classifier using positive tweets for training
    }
}
