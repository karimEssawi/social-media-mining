package classification;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Metric;

public class KnnTrainer extends ClassifierTrainer<Classifier> {
    private int k;
    private Metric metric;
    private Classifier classifier;

    public KnnTrainer(int k, Metric m) {
        this.k = k;
        this.metric = m;
    }

    @Override
    public Classifier getClassifier() {
        if(classifier == null)
            throw new RuntimeException("train has not been executed.");

        return classifier;
    }

    @Override
    public Classifier train(InstanceList trainingSet) {
        classifier = new Knn(k, metric, trainingSet);

        return classifier;
    }
}
