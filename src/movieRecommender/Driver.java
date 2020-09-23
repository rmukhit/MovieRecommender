package movieRecommender;

import java.util.Arrays;

/** A driver class for the MovieRecommender. In the main method, we
 * create a movie recommender, load movie data from files and compute
 * recommendations and anti-recommendations for a particular user.
 */
public class Driver {
    public static void main(String[] args) {

        MovieRecommender recommender = new MovieRecommender();

//         movies.csv and ratings.csv should be in the project folder
        recommender.loadData("movies.csv","ratings.csv");
        System.out.println("Loaded movie data...");

        recommender.findRecommendations(3, 15, "recommendations");
        System.out.println();
        recommender.findAntiRecommendations(3, 15, "antiRecommendations");


//        recommender.loadData("movies.csv", "ratings.csv");


//        UserNode user1 = new UserNode(1);
//        UserNode user2 = new UserNode(2);
//
//        user1.insert(1,5 );
//        user1.insert(2,5 );
//        user1.insert(3,2 );
//        user1.insert(4,5 );
//        user1.insert(5,4 );
//        user1.insert(6,3 );
//        user1.insert(7,4);
//
//        user2.insert(2,5);
//        user2.insert(4,4);
//        user2.insert(7,4);
//
//        double hehe = user2.computeSimilarity(user1);
//        System.out.println(hehe);
//
////
////        UsersList ul = new UsersList();
////        ul.append(user2);
////        System.out.println(ul);
////        user2.print();
//
////        System.out.println(Arrays.toString(user2.getLeastFavoriteMovies(4)));
//
////        MovieRatingsList list = new MovieRatingsList();
////        list.insertByRating(1,5 );
////        list.insertByRating(2,5 );
////        list.insertByRating(3,2 );
////        list.insertByRating(4,5 );
////        list.insertByRating(5,4 );
////        list.insertByRating(6,3 );
////        list.insertByRating(7,4 );
////        list.print();
////
////        list.reverse(new MovieRatingNode(1,3 )).print();
    }
}
