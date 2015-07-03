package classification;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.FeatureSelectingClassifierTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.types.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Evaluator {
    public double averageAccuracy = 0.0;
    public double positivePrecision = 0.0;
    public double positiveRecall = 0.0;
    public double positiveF1 = 0.0;
    public double negativePrecision = 0.0;
    public double negativeRecall = 0.0;
    public double negativeF1 = 0.0;

    public void crossValidationStandard(InstanceList Instances, ClassifierTrainer trainer, Classifier classifier, int foldNum){
        InstanceList.CrossValidationIterator cvIter = Instances.crossValidationIterator(foldNum);
        int foldCounter = 1;
        InstanceList [] trainTestSplits;
        InstanceList trainSplit, testSplit;
        Trial trial;

        while (cvIter.hasNext()){
            trainTestSplits = cvIter.nextSplit();
            trainSplit = trainTestSplits[0];
            testSplit = trainTestSplits[1];

            FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 2.0);
            fs.selectFeaturesFor(trainSplit);
            FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(trainer, fs);
            classifier = c.train(trainSplit);

            trial = new Trial(classifier, testSplit);
            averageAccuracy += trial.getAccuracy();

            positivePrecision += trial.getPrecision("POS");
            positiveRecall += trial.getRecall("POS");
            positiveF1 += trial.getF1("POS");
            negativePrecision += trial.getPrecision("NGV");
            negativeRecall += trial.getRecall("NGV");
            negativeF1 += trial.getF1("NGV");

            System.out.println("FOLD NUMBER: " + foldCounter + " ACCURACY: " + trial.getAccuracy());
            foldCounter++;
        }

        System.out.println("Positive: " + "Precision: " + (positivePrecision / foldNum)+ " Recall:" + (positiveRecall / foldNum) + " F1:" + (positiveF1 / foldNum));
        System.out.println("Negative: " + "Precision: " + (negativePrecision / foldNum)+ " Recall:" + (negativeRecall / foldNum) + " F1:" + (negativeF1 / foldNum));
        System.out.println("Overall Accuracy: " + averageAccuracy / foldNum);
    }

    // Perform modified n-fold cross validation
    public void crossValidationModified(InstanceList tweetInstances, InstanceList wpInstances, ClassifierTrainer trainer, Classifier classifier, int foldNum){
        InstanceList.CrossValidationIterator cvIter = tweetInstances.crossValidationIterator(foldNum);
        int foldCounter = 1;
        InstanceList [] trainTestSplits;
        InstanceList trainSplit, testSplit;
        ArrayList<InstanceList> arrayList  = new ArrayList<InstanceList>();
        MultiInstanceList multiList = null;
        Trial trial;

        while (cvIter.hasNext()){
            trainTestSplits = cvIter.nextSplit();
            arrayList.add(trainTestSplits[0]);
            arrayList.add(wpInstances);
            multiList = new MultiInstanceList(arrayList);
            trainSplit = multiList;
            testSplit = trainTestSplits[1];

            FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 8.0);
            fs.selectFeaturesFor(trainSplit);
            FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(trainer, fs);
            classifier = c.train(trainSplit);

            trial = new Trial(classifier, testSplit);
            arrayList.clear();

            averageAccuracy += trial.getAccuracy();
            positivePrecision += trial.getPrecision("POS");
            positiveRecall += trial.getRecall("POS");
            positiveF1 += trial.getF1("POS");
            negativePrecision += trial.getPrecision("NGV");
            negativeRecall += trial.getRecall("NGV");
            negativeF1 += trial.getF1("NGV");

            Iterator t = testSplit.iterator();
            Instance instance = null;
            while (t.hasNext()){
                instance = (Instance)t.next();
                System.out.println("FOLD NUMBER: " + foldCounter + " INSTANCE: " + instance.getName());
            }
            System.out.println("FOLD NUMBER: " + foldCounter + " ACCURACY: " + trial.getAccuracy());
            foldCounter++;
        }

        System.out.println("Positive: " + "Precision: " + (positivePrecision / foldNum)+ " Recall:" + (positiveRecall / foldNum) + " F1:" + (positiveF1 / foldNum));
        System.out.println("Negative: " + "Precision: " + (negativePrecision / foldNum)+ " Recall:" + (negativeRecall / foldNum) + " F1:" + (negativeF1 / foldNum));
        System.out.println("Overall Accuracy: " + averageAccuracy / foldNum);
    }

    // Perform modified Holdout testing
    public Trial holdOutModified(InstanceList instances, InstanceList wpInstances, ClassifierTrainer trainer, Classifier classifier, double trainPercentage, double testPercentage) {

        int TRAINING = 0;
        int TESTING = 1;
        int VALIDATION = 2;

        // Split the input list into training (90%) and testing (10%) lists.
        // The division takes place by creating a copy of the list,
        //  randomly shuffling the copy, and then allocating
        //  instances to each sub-list based on the provided proportions.

        InstanceList[] instanceLists = instances.split(new Random(), new double[] {trainPercentage, testPercentage, 0.0});

        ArrayList<InstanceList> arrayList  = new ArrayList<InstanceList>();
        arrayList.add(instanceLists[TRAINING]);
        arrayList.add(wpInstances);
        MultiInstanceList multiList = new MultiInstanceList(arrayList);

        // The third position is for the "validation" set,
        //  which is a set of instances not used directly
        //  for training, but available for determining
        //  when to stop training and for estimating optimal
        //  settings of nuisance parameters.
        // Most Mallet ClassifierTrainers can not currently take advantage
        //  of validation sets.

        FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 8.0);
        fs.selectFeaturesFor(multiList);
        FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(trainer, fs);
        classifier = c.train(multiList);

        for(Instance i : instanceLists[TESTING]){
            System.out.println(i.getName());
        }

        return new Trial(classifier, instanceLists[TESTING]);
    }

    //Perform standard Holdout
    public Trial holdOutStandard(InstanceList tweetInstances, InstanceList wpInstances, ClassifierTrainer trainer, Classifier classifier) {
        FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 8.0);
        fs.selectFeaturesFor(wpInstances);
        FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(trainer, fs);
        classifier = c.train(wpInstances);

        return new Trial(classifier, tweetInstances);
    }
}
