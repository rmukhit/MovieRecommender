package movieRecommender;

/**
 * MovieRatingsList.
 * A class that stores movie ratings for a user in a custom singly linked list of
 * MovieRatingNode objects. Has various methods to manipulate the list. Stores
 * only the head of the list (no tail! no size!). The list should be sorted by
 * rating (from highest to smallest).
 * Fill in code in the methods of this class.
 * Do not modify signatures of methods. Not all methods are needed to compute recommendations,
 * but all methods are required for the assignment.
 */

import java.util.HashMap;
import java.util.Iterator;

public class MovieRatingsList implements Iterable<MovieRatingNode> {

	private MovieRatingNode head;
	// Note: you are not allowed to store the tail or the size of this list

	/**
	 * Changes the rating for a given movie to newRating. The position of the
	 * node within the list should be changed accordingly, so that the list
	 * remains sorted by rating (from largest to smallest).
	 *
	 * @param movieId id of the movie
	 * @param newRating new rating of this movie
	 */
	public void setRating(int movieId, double newRating) {

		remove(movieId);
		insertByRating(movieId, newRating);

	}

	/**
	 * Helper method that removes particular MovieRatingNode from the list
	 *
	 * @param movieId id of the movie
	 */
	public void remove(int movieId) {
		MovieRatingNode tmp = head;
		MovieRatingNode previousNode = null;
		while (tmp != null) {
			if(tmp.getMovieId() == movieId) {
				if (previousNode == null) {
					head = tmp.next();
				} else {
					previousNode.setNext(tmp.next());
				}
			}
			previousNode = tmp;
			tmp = tmp.next();
		}

	}

    /**
     * Return the rating for a given movie. If the movie is not in the list,
     * returns -1.
     * @param movieId movie id
     * @return rating of a movie with this movie id
     */
	public double getRating(int movieId) {

		MovieRatingNode tmp = head;

 		if (tmp == null) {
			return -1;
		}

		while (tmp != null) {
 			if (tmp.getMovieId() == movieId) {
				return tmp.getMovieRating();
			}
 			tmp = tmp.next();
		}

		return -1;
	}


    /**
     * Insert a new node (with a given movie id and a given rating) into the list.
     * Insert it in the right place based on the value of the rating. Assume
     * the list is sorted by the value of ratings, from highest to smallest. The
     * list should remain sorted after this insert operation.
     *
     * @param movieId id of the movie
     * @param rating rating of the movie
     */
	public void insertByRating(int movieId, double rating) {

		MovieRatingNode newNode = new MovieRatingNode(movieId, rating);
		MovieRatingNode previousNode = null;
		MovieRatingNode tmp = head;

		while((tmp != null && tmp.getMovieRating() > newNode.getMovieRating()) ||
				(tmp != null && tmp.getMovieRating() == newNode.getMovieRating() && tmp.getMovieId() > newNode.getMovieId())) {
			previousNode = tmp;
			tmp = tmp.next();
		}

		if (tmp == null || tmp.getMovieRating() < newNode.getMovieRating() ||
				(tmp.getMovieRating() == newNode.getMovieRating() && tmp.getMovieId() <= newNode.getMovieId())) {
			newNode.setNext(tmp);
		}

		if (previousNode == null) {
			head = newNode;
		} else {
			previousNode.setNext(newNode);
		}


	}

    /**
     * Computes similarity between two lists of ratings using Pearson correlation.
	 * https://en.wikipedia.org/wiki/Pearson_correlation_coefficient
	 * Note: You are allowed to use a HashMap for this method.
     *
     * @param otherList another MovieRatingList
     * @return similarity computed using Pearson correlation
     */
    public double computeSimilarity(MovieRatingsList otherList) {
    	MovieRatingNode tmp = head;
		double similarity = -1;
		HashMap<Integer, double[]> hm = new HashMap<>();
		while (tmp != null) {
			MovieRatingNode tmpOtherList = otherList.head;
			while (tmpOtherList != null) {
				if(tmp.getMovieId() == tmpOtherList.getMovieId()) {
					hm.put(tmp.getMovieId(), new double[] {tmp.getMovieRating(), tmpOtherList.getMovieRating()});
				}
				tmpOtherList = tmpOtherList.next();
			}
			tmp = tmp.next();
		}

		double xy = 0;
		double x = 0;
		double y = 0;
		double xSqr = 0;
		double ySqr = 0;
		int count = hm.size();

		for (int key : hm.keySet()) {
			xy = xy + hm.get(key)[0] * hm.get(key)[1];
			x = x + hm.get(key)[0];
			y = y + hm.get(key)[1];
			xSqr = xSqr + hm.get(key)[0] * hm.get(key)[0];
			ySqr = ySqr + hm.get(key)[1] * hm.get(key)[1];
		}

		double num = count*xy - x*y;
		double denom = (Math.sqrt(count*xSqr - x*x)) * (Math.sqrt(count*ySqr - y*y));
		if (denom != 0) {
			similarity = num/denom;
		}

		return similarity;

    }
    /**
     * Returns a sublist of this list where the rating values are in the range
     * from begRating to endRating, inclusive.
     *
     * @param begRating lower bound for ratings in the resulting list
     * @param endRating upper bound for ratings in the resulting list
     * @return sublist of the MovieRatingsList that contains only nodes with
     * rating in the given interval
     */
	public MovieRatingsList sublist(int begRating, int endRating) {
		MovieRatingsList res = new MovieRatingsList();

		MovieRatingNode tmp = head;
		while (tmp != null) {
			if (tmp.getMovieRating() >= begRating && tmp.getMovieRating() <= endRating){
				res.insertByRating(tmp.getMovieId(), tmp.getMovieRating());
			}
			tmp = tmp.next();
		}
		return res;
	}

	/** Function that checks if the user has seen this movie before
	 *
	 * @param otherList upper bound for ratings in the resulting list
	 * @return list of the unique movies that the user hasn't seen
	 */
	public MovieRatingsList uniqueSublist(MovieRatingsList otherList) {
        MovieRatingsList res = new MovieRatingsList();
        Boolean found = false;

        MovieRatingNode tmp = head;
        while (tmp != null) {
            MovieRatingNode tmpOtherList = otherList.head;
            while (tmpOtherList != null) {
                if(tmp.getMovieId() == tmpOtherList.getMovieId()) {
                    found=true;
                }
                tmpOtherList = tmpOtherList.next();
            }
            if (!found){
                res.insertByRating(tmp.getMovieId(), tmp.getMovieRating());
            }
            found=false;
            tmp = tmp.next();
        }
        return res;
    }

	/** Traverses the list and prints the ratings list in the following format:
	 *  movieId:rating; movieId:rating; movieId:rating;  */
	public void print() {
		MovieRatingNode tmp = head;
		while (tmp != null) {
			System.out.printf(tmp.getMovieId() + ":" + tmp.getMovieRating() + "; ");
			tmp = tmp.next();
		}
	}

	/**
	 * Returns the middle node in the list - the one half way into the list.
	 * Needs to have the running time O(n), and should be done in one pass
     * using slow & fast pointers (as described in class).
	 *
	 * @return the middle MovieRatingNode
	 */
	public MovieRatingNode getMiddleNode() {

		MovieRatingNode slow = head;
		MovieRatingNode fast = head;
		if (head != null)
		{
			while (fast != null && fast.next() != null)
			{
				fast = fast.next().next();
				slow = slow.next();
			}

		}
		return slow;
	}

    /**
     * Returns the median rating (the number that is halfway into the sorted
     * list). To compute it, find the middle node and return it's rating. If the
     * middle node is null, return -1.
     *
     * @return rating stored in the node in the middle of the list
     */
	public double getMedianRating() {
		MovieRatingNode result = getMiddleNode();
		if (result == null) {
			return -1;
		} else {
			return result.getMovieRating();
		}
	}

    /**
     * Returns a RatingsList that contains n best rated movies. These are
     * essentially first n movies from the beginning of the list. If the list is
     * shorter than size n, it will return the whole list.
     *
     * @param n the maximum number of movies to return
     * @return MovieRatingsList containing movies rated as 5
     */
	public MovieRatingsList getNBestRankedMovies(int n) {
		MovieRatingsList res = new MovieRatingsList();
		MovieRatingNode tmp = head;
		int count = 0;
		while (tmp != null && count < n) {
			res.insertByRating(tmp.getMovieId(), tmp.getMovieRating());
			count++;
			tmp = tmp.next();
		}
		return res;
	}

    /**
     * * Returns a RatingsList that contains n worst rated movies for this user.
     * Essentially, these are the last n movies from the end of the list. You are required to
	 * use slow & fast pointers to find the n-th node from the end (as discussed in class).
	 * Note: This method should compute the result in one pass. Do not use size variable
	 * Do NOT use reverse(). Do not destroy the list.
     *
     * @param n the maximum number of movies to return
     * @return MovieRatingsList containing movies rated as 1
     */
	public MovieRatingsList getNWorstRankedMovies(int n) {
		MovieRatingsList res = new MovieRatingsList();

		MovieRatingNode fast = head;
		MovieRatingNode slow = head;
		int start = 1;
		if (head == null) {
			return null;
		}

		while (fast.next() != null) {
			fast = fast.next();
			start++;
			if (start > n) {
				slow = slow.next();

			}
		}
		while (slow != null) {
			res.insertByRating(slow.getMovieId(), slow.getMovieRating());
			slow = slow.next();
		}

		return res;
	}

    /**
     * Return a new list that is the reverse of the original list. The returned
     * list is sorted from lowest ranked movies to the highest rated movies.
     * Use only one additional MovieRatingsList (the one you return) and constant amount
     * of memory. You may NOT use arrays, ArrayList and other built-in Java Collections classes.
     * Read description carefully for requirements regarding implementation of this method.
	 *
     * @param h head of the MovieRatingList to reverse
     * @return reversed list
     */
	public MovieRatingsList reverse(MovieRatingNode h) {
		MovieRatingsList r = new MovieRatingsList();

		MovieRatingNode tmp = h;
		MovieRatingNode previousNode = null;
		MovieRatingNode nextNode;

		while(tmp!=null){
			nextNode = tmp.next();
			tmp.setNext(previousNode);
			previousNode = tmp;
			tmp = nextNode;
		}
		r.head = previousNode;


		return r;

	}

	public Iterator<MovieRatingNode> iterator() {

		return new MovieRatingsListIterator(0);
	}

	/**
	 * Inner class, MovieRatingsListIterator
	 * The iterator for the ratings list. Allows iterating over the MovieRatingNode-s of
	 * the list.
	 * FILL IN CODE
	 */
	private class MovieRatingsListIterator implements Iterator<MovieRatingNode> {

		MovieRatingNode curr = null;

		public MovieRatingsListIterator(int index) {

			curr = head;
			for (int i = 0; i < index; i++) {
				curr = curr.next();
			}

		}

		@Override
		public boolean hasNext() {
			return curr != null;
		}

		@Override
		public MovieRatingNode next() {
			if (!hasNext()) {
				System.out.println("No next element");
				return null;
			}
			MovieRatingNode tmp = curr;
			curr = curr.next();
			return tmp;
		}


	}

}
