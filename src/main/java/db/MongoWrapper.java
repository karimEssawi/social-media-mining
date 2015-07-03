package db;

import com.mongodb.*;
import com.mongodb.util.JSON;
import twitter4j.Status;
import twitter4j.json.DataObjectFactory;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

public class MongoWrapper {
    private Mongo mongo;
    private DB mongoDB;
    private DBCollection tweetsCollection;

    public MongoWrapper() {
        try{
            mongo = new Mongo("localhost");
            mongoDB = mongo.getDB("twitter_mining");
            tweetsCollection = mongoDB.getCollection("tweets");
        }catch(UnknownHostException e){e.printStackTrace();}
    }

    public void insertTweets(Status incomingTweet){
        String tweet = DataObjectFactory.getRawJSON(incomingTweet);
        DBObject doc = (DBObject) JSON.parse(tweet);
        tweetsCollection.save(doc);
    }

    public void insertTweets(String tweetId, String tweetText, Date tweetDate, String tweetSentiment, Double tweetLatitude,
                             Double tweetLongitude, String userID, String userScreenName, String userLocation, int userFollowers, boolean userVerified,
                             List hashtagList, List urlList) {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("_id", tweetId);
        dbObj.put("text", tweetText);
        dbObj.put("date", tweetDate);
        dbObj.put("sentiment", tweetSentiment);
        dbObj.put("latitude", tweetLatitude);
        dbObj.put("longitude", tweetLongitude);
        dbObj.put("userID", userID);
        dbObj.put("userScreenName", userScreenName);
        dbObj.put("userLocation", userLocation);
        dbObj.put("userFollowers", userFollowers);
        dbObj.put("userVarified", userVerified);
        dbObj.put("hashtagList", hashtagList);
        dbObj.put("urlList", urlList);
        tweetsCollection.save(dbObj);
    }
}
