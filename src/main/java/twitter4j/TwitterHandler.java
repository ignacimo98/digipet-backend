package twitter4j;

public class TwitterHandler {
    public static void postTweet(String name, int rate){
        try {
            Twitterer bigBird = new Twitterer();
            String message = name + " ha obtenido una calificación de " + rate + " \uD83D\uDC36 en su último paseo con DigiPet.";
            bigBird.tweetOut(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
