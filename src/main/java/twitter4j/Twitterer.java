
package twitter4j;

import twitter4j.GeoLocation;       // jar found at http://twitter4j.org/en/index.html
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;



public class Twitterer
   {
      private Twitter twitter;
      private List<Status> statuses;

     
      public Twitterer()
      {
         // Makes an instance of Twitter - this is re-useable and thread safe.
         // Connects to Twitter and performs authorizations.
          //twitter = TwitterFactory.getSingleton();

          //Instancia con el ConfigBuilder

          ConfigurationBuilder cb = new ConfigurationBuilder();
          cb.setDebugEnabled(true)
                  .setOAuthConsumerKey("Ffa46RvpGy2pum1skmx2CJp1S")
                  .setOAuthConsumerSecret("7JeYgXR9zdYSKAEQY4NFghiNleNkW3Txs0pEKefS8AmGabvKnD")
                  .setOAuthAccessToken("1133090362141106179-IV11yo8hG4uYzGU4VuvGeX3UDAHkPD")
                  .setOAuthAccessTokenSecret("9BGfZCZmtqth8e91k70pBIbsXJQZ8ylrcg6dO8wNreoLo");
          TwitterFactory tf = new TwitterFactory(cb.build());
          twitter = tf.getInstance();

         statuses = new ArrayList<Status>();
      }
   

     /** 
      * This method tweets a given message.
      * param** String a message you wish to Tweet out
      */
      public void tweetOut(String message) throws TwitterException, IOException
      {
          Status status = twitter.updateStatus(message);
          System.out.println("Updated status to:[ " + status.getText()+" ]");
      }
   
   }  
