import java.util.Arrays;

import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class Autocomplete {
    private final Term[] terms; // initializing variable

    // Constructs an autocomplete data structure from an array of terms.
    public Autocomplete(Term[] terms) {
        // Initialize this.terms to a defensive copy of terms
        if (terms == null) {
            throw new NullPointerException("terms is null");
        }
        // Sort this.terms in lexicographic order
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            this.terms[i] = terms[i];
        }
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with prefix, in descending order of their weights.
    public Term[] allMatches(String prefix) {

        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }
        Term n = new Term(prefix);
        // Find the index i of the first term in terms that starts with prefix
        int i = BinarySearchDeluxe.firstIndexOf(terms, n, Term.byPrefixOrder(prefix.length()));

        // Find the number of terms (say n) in terms that start with prefix
        if (i == -1) {
            return new Term[0];
        }
        int j = BinarySearchDeluxe.lastIndexOf(terms, n, Term.byPrefixOrder(prefix.length()));
        // Construct an array matches containing n elements from terms, starting at index i
        Term[] matches = new Term[j - i + 1];
        for (int x = 0; x < matches.length; x++) {
            matches[x] = terms[x + i];
        }
        // Sort matches in reverse order of weight and return the sorted array
        Arrays.sort(matches, Term.byReverseWeightOrder());
        // return matches
        return matches;
    }

    // Returns the number of terms that start with prefix.
    public int numberOfMatches(String prefix) {

        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }

        Term n = new Term(prefix, 0);

        // Find the indices i and j of the first and last term in terms that start with prefix
        //  Using the indices, compute the number of terms that start with prefix,
        //  and return that value
        int i = BinarySearchDeluxe.firstIndexOf(terms, n, Term.byPrefixOrder(prefix.length()));

        if (i == -1) {
            return 0;
        }

        int j = BinarySearchDeluxe.lastIndexOf(terms, n, Term.byPrefixOrder(prefix.length()));
        return j - i + 1;
    }


    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        Autocomplete autocomplete = new Autocomplete(terms);
        StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            String msg = " matches for \"" + prefix + "\", in descending order by weight:";
            if (results.length == 0) {
                msg = "No matches";
            } else if (results.length > k) {
                msg = "First " + k + msg;
            } else {
                msg = "All" + msg;
            }
            StdOut.printf("%s\n", msg);
            for (int i = 0; i < Math.min(k, results.length); i++) {
                StdOut.println("  " + results[i]);
            }
            StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        }
    }
}