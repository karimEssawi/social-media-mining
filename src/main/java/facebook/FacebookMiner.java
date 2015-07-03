package facebook;

import facebook4j.*;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookMiner {
    /**
     * A simple Facebook4J client which
     * illustrates how to access group feeds / posts / comments.
     *
     * @param args
     * @throws FacebookException
     */
    public static void main(String[] args) throws FacebookException {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setHttpProxyHost("proxy.tradermedia.co.uk");
        cb.setHttpProxyPort(8080);
        cb.setDebugEnabled(true)
                .setOAuthAppId("643803779050193")
                .setOAuthAppSecret("47ee9f59aa7717e218ef1fecc23c25d5")
                .setOAuthAccessToken("CAACEdEose0cBAGHtKD0Yls4x3bZAqaKDjOrpsMBOuUguXmf0ybF0YmAxFRKOzkXR8gqO4gT55e5hay5ORuHAwcyH0Hf7gImyQVnM72QL7ZCujAbTJ6bO4ykhQlW7EUyQcLnmuQxSDI4lWOjgTISUx9HI2CVS3UhmDLqnc7cKxNEVWE0pCdLxuMW4NRlI2oGzqoXkUYUKPuXWHyReE2");
        // Generate facebook instance.
        Facebook facebook = new FacebookFactory(cb.build()).getInstance();
        // Use default values for oauth app id.
//        facebook.setOAuthAppId("643803779050193", "47ee9f59aa7717e218ef1fecc23c25d5");
        // Get an access token from:
        // https://developers.facebook.com/tools/explorer
        // Copy and paste it below.
//        String accessTokenString = "CAACEdEose0cBADwUJtEoq4c7G16yY5idE8E07PmLjrVbS05WbE0SOzqXqTmN3U1ZA1Ut3jw9vjrbOZBQJbVaLMrdZBZCflEIzLzoEGIv493qj8ZClqsMK9tdhmj31RRC5W2rsJiiPnZC7fZBrK1RGXxzZALEsZC9pRDDPuRSdnlQxoC8b9TGPvBAZBrNMZAKPvqE7SfbGpUSZCaB9Jmini5UNEj6";
//        AccessToken at = new AccessToken(accessTokenString);
        // Set access token.
//        facebook.setOAuthAccessToken(at);

        // We're done.
        // Access group feeds.
        // You can get the group ID from:
        // https://developers.facebook.com/tools/explorer

        // Set limit to 25 feeds.

        ResponseList<Post> feeds = //facebook.searchPosts("egypt");
                facebook.getPosts("145432176214", new Reading().limit(25));

        // For all 25 feeds...
        for (int i = 0; i < feeds.size(); i++) {
            // Get post.
            Post post = feeds.get(i);
            // Get (string) message.
            String message = post.getMessage();
            // Print out the message.
            ResponseList<Like> likes = facebook.getPostLikes(post.getId(), new Reading().limit(500));
            System.out.println(message + " - Likes: " + likes.size() /*+ " - Comments: " + new FacebookMiner().getComments(post).size()*/ + " - Shares: " + post.getSharesCount() + " - " + post.getCreatedTime().toString());

            // Get more stuff...
            PagableList<Comment> comments = post.getComments();
            String date = post.getCreatedTime().toString();
            String name = post.getFrom().getName();
            String id = post.getId();
        }

//        ResponseList<Page> page = facebook.searchPages("Auto Trader UK");
//        for(Page p : page){
//            System.out.println(p.getId());
//        }

//        Page page2 = facebook.getPage("145432176214");
//        System.out.println(page2.getLikes());
    }

//    public List<Comment> getComments(Post post) {
//        List<Comment> fullComments = new ArrayList<>();
//        // get first few comments using getComments from post
//        PagableList<Comment> comments = post.getComments();
//        Paging<Comment> paging;
//        do {
//            for (Comment comment: comments)
//                fullComments.add(comment);
//
//            // get next page
//            // NOTE: somehow few comments will not be included.
//            // however, this won't affect much on our research
//            paging = comments.getPaging();
//        } while ((paging != null));// && ((comments = fb_.fetchNext(paging)) != null));
//
//        return fullComments;
//    }
}

