package twitter4j;

import twitter4j.TwitterException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

   public class TwitterDriver
   {
      private static PrintStream consolePrint;
   
      public static void main (String []args) throws TwitterException, IOException
      {
         // set up classpath and properties file
             
         Twitterer bigBird = new Twitterer(consolePrint);

         String message = "Ya queremos ver sus perras \uD83D\uDC36 :3";
         bigBird.tweetOut(message);

      }//main         
         
   }//class    
         
   