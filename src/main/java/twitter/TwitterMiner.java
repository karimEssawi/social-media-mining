package twitter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterMiner{

    private ConfigurationBuilder cb = new ConfigurationBuilder();
    private TwitterStream twitterStream;
    private FilterQuery query;
    private String[] keywords;
    private double[][] coordinates;

    // Coordinates for UK and Ireland
    private double latatitude = 53.186288;
    private double longitude = -8.043709;
    private double latitude1 = latatitude - 4;
    private double longitude1 = longitude - 8;
    private double latitude2 = latatitude + 4;
    private double longitude2 = longitude + 8;

    public TwitterMiner(StatusListener listener){

        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(listener);
        keywords = Util.readKeyWords("keywords.txt");
        coordinates = new double[][]{{longitude1, latitude1}, {longitude2, latitude2}};
        query = new FilterQuery();
        query.track(keywords);
//        query.locations(new double[][]{{longitude1, latitude1}, {longitude2, latitude2}});

        try{
            getStream(query);
        }catch(TwitterException e){e.printStackTrace();}
    }

    public void getStream(FilterQuery query)throws TwitterException{
        twitterStream.filter(query);
    }
}
