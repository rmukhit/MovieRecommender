package movieRecommender;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/** MovieRecommender. A class that is responsible for:
    - Reading movie and ratings data from the file and loading it in the data structure UsersList.
 *  - Computing movie recommendations for a given user and printing them to a file.
 *  - Computing movie "anti-recommendations" for a given user and printing them to file.
 *  Fill in code in methods of this class.
 *  Do not modify signatures of methods.
 */
public class MovieRecommender {
    private UsersList usersData; // linked list of users
    private HashMap<Integer, String> movieMap; // maps each movieId to the movie title

    public MovieRecommender() {
         movieMap = new HashMap<>();
         usersData = new UsersList();
    }

    /**
     * Read user ratings from the file and save data for each user in this list.
     * For each user, the ratings list will be sorted by rating (from largest to
     * smallest).
     * @param movieFilename name of the file with movie info
     * @param ratingsFilename name of the file with ratings info
     */
    public void loadData(String movieFilename, String ratingsFilename) {

        loadMovies(movieFilename);
        loadRatings(ratingsFilename);
    }

    /** Load information about movie ids and titles from the given file.
     *  Store information in a hashmap that maps each movie id to a movie title
     *
     * @param movieFilename csv file that contains movie information.
     *
     */
    private void loadMovies(String movieFilename) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(movieFilename));
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {

                String[] movies = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                movieMap.put(Integer.parseInt(movies[0]), movies[1]);
            }

            br.close();


        } catch (Exception e) {

        }


    }

    /**
     * Load users' movie ratings from the file into UsersList
     * @param ratingsFilename name of the file that contains ratings
     */
    private void loadRatings(String ratingsFilename) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(ratingsFilename));
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {

                String[] ratings = line.split(",");

                usersData.insert(Integer.parseInt(ratings[0]), Integer.parseInt(ratings[1]), Double.parseDouble(ratings[2]));

            }

            br.close();

        } catch (Exception e) {

        }

    }

    /**
     * * Computes up to num movie recommendations for the user with the given user
     * id and prints these movie titles to the given file. First calls
     * findMostSimilarUser and then getFavoriteMovies(num) method on the
     * "most similar user" to get up to num recommendations. Prints movies that
     * the user with the given userId has not seen yet.
     * @param userid id of the user
     * @param num max number of recommendations
     * @param filename name of the file where to output recommended movie titles
     *                 Format of the file: one movie title per each line
     */
    public void findRecommendations(int userid, int num, String filename) {

        try (PrintWriter pw = new PrintWriter(filename)) {

            UserNode otherUser = usersData.get(userid);
            UserNode user = usersData.findMostSimilarUser(userid);
            int arrayOfFavoriteMovies[] = user.getUniqueFavoriteMovies(otherUser, num);

            for(int i : arrayOfFavoriteMovies) {

              pw.println(movieMap.get(i).replaceAll("\"", ""));
            }

            pw.flush();

        }
        catch (IOException e) {
            System.out.println("Error writing to a file: "  + e);
        }


    }

    /**
     * Computes up to num movie anti-recommendations for the user with the given
     * user id and prints these movie titles to the given file. These are the
     * movies the user should avoid. First calls findMostSimilarUser and then
     * getLeastFavoriteMovies(num) method on the "most similar user" to get up
     * to num movies the most similar user strongly disliked. Prints only
     * those movies to the file that the user with the given userid has not seen yet.
     * Format: one movie title per each line
     * @param userid id of the user
     * @param num max number of anti-recommendations
     * @param filename name of the file where to output anti-recommendations (movie titles)
     */
    public void findAntiRecommendations(int userid, int num, String filename) {

        try (PrintWriter pw = new PrintWriter(filename)) {

            UserNode otherUser = usersData.get(userid);
            UserNode user = usersData.findMostSimilarUser(userid);

            int arrayOfLeastFavoriteMovies[] = user.getUniqueLeastFavoriteMovies(otherUser, num);

            for(int i : arrayOfLeastFavoriteMovies) {
                pw.println(movieMap.get(i));
            }
            pw.flush();

        }
        catch (IOException e) {
            System.out.println("Error writing to a file: "  + e);
        }

    }

}
