package ir.ac.kntu.util;

import java.util.*;

/**
 * A simple BK-Tree for fuzzy string search.
 * <p>
 * ...
 */
public final class BKTree {
    private static final class Node {
        private final String term;
        private final Map<Integer, Node> children = new HashMap<>();

        private Node(String term) {
            this.term = term;
        }
    }

    private Node root;

    public void add(String term) {
        Objects.requireNonNull(term, "term");
        if (root == null) {
            root = new Node(term);
            return;
        }
        Node cur = root;
        while (true) {
            int d = levenshtein(cur.term, term);
            Node nxt = cur.children.get(d);
            if (nxt == null) {
                cur.children.put(d, new Node(term));
                return;
            }
            cur = nxt;
        }
    }

    public List<String> search(String query, int threshold) {
        Objects.requireNonNull(query, "query");
        List<String> out = new ArrayList<>();
        if (root == null) {
            return out;
        }
        ArrayDeque<Node> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node n = q.removeFirst();
            int d = levenshtein(n.term, query);
            if (d <= threshold) {
                out.add(n.term);
            }
            int lo = d - threshold;
            int hi = d + threshold;
            for (Map.Entry<Integer, Node> e : n.children.entrySet()) {
                int dist = e.getKey();
                if (dist >= lo && dist <= hi) {
                    q.addLast(e.getValue());
                }
            }
        }
        return out;
    }

    /**
     * Classic DP Levenshtein (edit distance).
     */
    public static int levenshtein(String a, String b) {
        if (a.equals(b)) {
            return 0;
        }
        int n = a.length();
        int m = b.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;
                cur[j] = Math.min(Math.min(cur[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
        }
        return prev[m];
    }
}
