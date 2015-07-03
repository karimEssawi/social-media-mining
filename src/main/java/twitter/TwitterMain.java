package twitter;

import twitter4j.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TwitterMain{

    private TwitterMiner twitterMiner;
    private String tweetId;
    private String tweetText;
    private java.util.Date tweetDate;
    private String tweetSentiment;
    private String tweetCategory;
    private Double tweetLatitude;
    private Double tweetLongitude;

    public void tweetListener(){

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                String tweet = null;
                String hashtags = "hashtags: ";
                String geo = "N/A";
                HashtagEntity[] hashTagEntity;
                URLEntity[] urlEntity;
                List hashtagList;
                List urlList;
                String urls = "";

                if (!status.getText().contains(":)") && status.getText().contains("a") && status.getUser().getLang().equalsIgnoreCase("en")){
                    try {
                            if(status.getGeoLocation() != null)
                                geo = status.getGeoLocation().toString();

                            tweetSentiment = "N";
                            tweetId = String.valueOf(status.getId());
                            tweetText = status.getText();
                            tweetDate = status.getCreatedAt();


                            if(status.getGeoLocation()!= null){
                                tweetLatitude = status.getGeoLocation().getLatitude();
                                tweetLongitude = status.getGeoLocation().getLongitude();
                                geo = tweetLatitude + "|" + tweetLongitude;
                            }
                            else{
                                tweetLatitude = null;
                                tweetLongitude = null;
                            }

                            urlEntity = status.getURLEntities();
                            hashTagEntity = status.getHashtagEntities();

                            if(hashTagEntity.length == 0) {
                                hashtagList = null;
                            }
                            else{
                                hashtagList = new ArrayList<>();
                                for (int i=0; i < hashTagEntity.length; i++) {
                                    hashtagList.add(hashTagEntity[i].getText());
                                    hashtags = hashtags + hashTagEntity[i].getText() + ", ";
                                }
                            }

                            if(urlEntity.length == 0) {
                                urlList = null;
                            }
                            else{
                                urlList = new ArrayList<>();
                                for (int i=0; i < urlEntity.length; i++) {
                                    urlList.add(urlEntity[i].getExpandedURL());
                                    urls = urls + urlEntity[i].getExpandedURL() + ", ";
                                }
                            }

                        System.out.println(tweetText);
//                        Util.writeStringToFile("tweetsNegative.txt", tweetText);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

        };

        twitterMiner = new TwitterMiner(listener);
    }

    public static void main(String[] args) throws URISyntaxException {
        TwitterMain t = new TwitterMain();
        t.tweetListener();
    }
}