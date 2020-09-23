package movieRecommender;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * A custom linked list that stores user info. Each node in the list is of type
 * UserNode.
 * FILL IN CODE. Also, add other methods as needed.
 *
 * @author okarpenko
 *
 */
public class UsersList {
    private UserNode head = null;
    private UserNode tail = null; // ok to store tail here, will be handy for appending


    /** Insert the rating for the given userId and given movieId.
     *
     * @param userId  id of the user
     * @param movieId id of the movie
     * @param rating  rating given by this user to this movie
     */
    public void insert(int userId, int movieId, double rating) {

        if (get(userId) == null) {
            UserNode newNode = new UserNode(userId);
            append(newNode);
            get(userId).insert(movieId, rating);
        } else {
            get(userId).insert(movieId, rating);
        }

    }

    /**
     * Append a new node to the list
     * @param newNode a new node to append to the list
     */
    public void append(UserNode newNode) {

        if (head == null) {
            head = newNode;
            tail = head;
        } else {
            tail.setNext(newNode);
            tail = newNode;

        }

    }

    /** Return a UserNode given userId
     *
     * @param userId id of the user (as defined in ratings.csv)
     * @return UserNode for a given userId
     */
    public UserNode get(int userId) {
        // FILL IN CODE
        UserNode tmp = head;
        while(tmp != null) {
            if (tmp.getId() == userId) {
                return tmp;
            }
            tmp = tmp.next();
        }

        return null;
    }


    /**
     * The method computes the similarity between the user with the given userid
     * and all the other users. Finds the maximum similarity and returns the
     * "most similar user".
     * Calls computeSimilarity method in class MovieRatingsList/
     *
     * @param userid id of the user
     * @return the node that corresponds to the most similar user
     */
    public UserNode findMostSimilarUser(int userid) {
        UserNode mostSimilarUser = null;
        UserNode tmp = head;
        double similarity = -1;
        double tmpSimilarity;
        while(tmp != null) {
            if (tmp.getId() != userid) {
                tmpSimilarity = tmp.computeSimilarity(get(userid));
                if (tmpSimilarity==1){
                    mostSimilarUser = tmp;
                    break;
                }
                if (tmpSimilarity >= similarity) {
                    similarity = tmpSimilarity;
                    mostSimilarUser = tmp;
                }
            }

            tmp = tmp.next();
        }
        return mostSimilarUser;
    }



    /** Print UsersList to a file  with the given name in the following format:
     (userid) movieId:rating; movieId:rating; movieId:rating;
     (userid) movieId:rating; movieId:rating;
     (userid) movieId:rating; movieId:rating; movieId:rating; movieId:rating;
     Info for each userid should be printed on a separate line
     * @param filename name of the file where to output UsersList info
     */
    public void print(String filename) {

        try (PrintWriter pw = new PrintWriter(filename)) {

            UserNode tmp = head;
            while(tmp != null) {
                pw.println(tmp);
                tmp = tmp.next();
            }
            pw.flush();

        }
        catch (IOException e) {
            System.out.println("Error writing to a file: "  + e);
        }

    }
}
