/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cophi.modules.variants.builtin;

import eu.cophi.api.variants.Variant;
import eu.cophi.modules.variants.builtin.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author angelodel80
 */
public class HuntVariants {

    private static final Pattern spaces = Pattern.compile("(\\s+)");
    
    private HuntVariants() {
    }

    /**
     * @param lines1 array of lines from the first source
     * @param lines2 array of lines from the second source
     * @param options additional paremeters for the diff algorithm
     * @return computed diff
     */ 
    public static Variant[] diff(String[] lines1, String[] lines2, Provider.Options options) {
        int m = lines1.length;
        int n = lines2.length;
        String [] lines1_original = copy(lines1);
        String [] lines2_original = copy(lines2);
        applyDiffOptions(lines1, lines2, options);

        Line[] l2s = new Line[n + 1];
        // In l2s we have sorted lines of the second file <1, n>
        for (int i = 1; i <= n; i++) {
            l2s[i] = new Line(i, lines2[i - 1]);
        }
        Arrays.sort(l2s, 1, n+1, new Comparator<Line>() {
            
            public int compare(Line l1, Line l2) {
                return l1.line.compareTo(l2.line);
            }
        });
        
        int[] equvalenceLines = new int[n+1];
        boolean[] equivalence = new boolean[n+1];
        for (int i = 1; i <= n; i++) {
            Line l = l2s[i];
            equvalenceLines[i] = l.lineNo;
            equivalence[i] = (i == n) || !l.line.equals(l2s[i+1].line);//((Line) l2s.get(i)).line);
        }
        equvalenceLines[0] = 0;
        equivalence[0] = true;
        int[] equivalenceAssoc = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            equivalenceAssoc[i] = findAssoc(lines1[i - 1], l2s, equivalence);
        }
        
        l2s = null;
        Candidate[] K = new Candidate[Math.min(m, n) + 2];
        K[0] = new Candidate(0, 0, null);
        K[1] = new Candidate(m + 1, n + 1, null);
        int k = 0;
        for (int i = 1; i <= m; i++) {
            if (equivalenceAssoc[i] != 0) {
                k = merge(K, k, i, equvalenceLines, equivalence, equivalenceAssoc[i]);
            }
        }
        int[] J = new int[m+2]; // Initialized with zeros
        
        Candidate c = K[k];
        while (c != null) {
            J[c.a] = c.b;
            c = c.c;
        }
        
        List<Variant> variants = getVariants(J, lines1_original, lines2_original);
        cleanup(variants);
        return variants.toArray(new Variant[variants.size()]);
    }
    
    private static String[] copy(String[] strings) {
        String [] copy = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            copy[i] = strings[i];
        }
        return copy;
    }

    private static void applyDiffOptions(String[] lines1, String[] lines2, Provider.Options options) {
        if (options.ignoreLeadingAndtrailingWhitespace && options.ignoreInnerWhitespace) {
            for (int i = 0; i < lines1.length; i++) {
                lines1[i] = spaces.matcher(lines1[i]).replaceAll("");
            }
            for (int i = 0; i < lines2.length; i++) {
                lines2[i] = spaces.matcher(lines2[i]).replaceAll("");
            }
        } else if (options.ignoreLeadingAndtrailingWhitespace) {
            for (int i = 0; i < lines1.length; i++) {
                lines1[i] = lines1[i].trim();
            }
            for (int i = 0; i < lines2.length; i++) {
                lines2[i] = lines2[i].trim();
            }
        } else if (options.ignoreInnerWhitespace) {
            for (int i = 0; i < lines1.length; i++) {
                replaceInnerSpaces(lines1, i);
            }
            for (int i = 0; i < lines2.length; i++) {
                replaceInnerSpaces(lines2, i);
            }
        }
        if (options.ignoreCase) {
            for (int i = 0 ; i < lines1.length; i++) {
                lines1[i] = lines1[i].toUpperCase();
            }
            for (int i = 0 ; i < lines2.length; i++) {
                lines2[i] = lines2[i].toUpperCase();
            }
        }
    }

    private static void replaceInnerSpaces(String[] strings, int idx) {
        Matcher m = spaces.matcher(strings[idx]);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            if (m.start() == 0 || m.end() == strings[idx].length()) {
                m.appendReplacement(sb, "$1");
            } else {
                m.appendReplacement(sb, "");
            }
        }
        m.appendTail(sb);
        strings[idx] = sb.toString();
    }

    private static int findAssoc(String line1, Line[] l2s, boolean[] equivalence) {
        int idx = binarySearch(l2s, line1, 1, l2s.length - 1);
        if (idx < 1) {
            return 0;
        } else {
            int lastGoodIdx = 0;
            for (; idx >= 1 && l2s[idx].line.equals(line1); idx--) {
                if (equivalence[idx - 1]) {
                    lastGoodIdx = idx;
                }
            }
            return lastGoodIdx;
        }
    }

    private static int binarySearch(Line[] L, String key, int low, int high) {
        while (low <= high) {
            int mid = (low + high) >> 1;
            String midVal = L[mid].line;
            int comparison = midVal.compareTo(key); 
            if (comparison < 0) {
                low = mid + 1;
            } else if (comparison > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
    	return -(low + 1);
    }
    
    private static int binarySearch(Candidate[] K, int key, int low, int high) {
        while (low <= high) {
            int mid = (low + high) >> 1;
            int midVal = K[mid].b;
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
    	return -(low + 1);
    }

    private static int merge(Candidate[] K, int k, int i, int[] equvalenceLines,
                             boolean[] equivalence, int p) {
        int r = 0;
        Candidate c = K[0];
        do {
            int j = equvalenceLines[p];
            int s = binarySearch(K, j, r, k);
            if (s >= 0) {
                // j was found in K[]
                s = k + 1;
            } else {
                s = -s - 2;
                if (s < r || s > k) s = k + 1; 
            }
            if (s <= k) {
                if (K[s+1].b > j) {
                    Candidate newc = new Candidate(i, j, K[s]);
                    K[r] = c;
                    r = s+1;
                    c = newc;
                }
                if (s == k) {
                    K[k+2] = K[k+1];
                    k++;
                    break;
                }
            }
            if (equivalence[p]) {
                break;
            } else {
                p++;
            }
        } while (true);
        K[r] = c;
        return k;
    }
    
    private static List<Variant> getVariants(int[] J, String[] lines1, String[] lines2) {
        List<Variant> differences = new ArrayList<Variant>();
        int n = lines1.length;
        int m = lines2.length;
        int start1 = 1;
        int start2 = 1;
        do {
            while (start1 <= n && J[start1] == start2) {
                start1++;
                start2++;
            }
            if (start1 > n) break;
            if (J[start1] < start2) { // There's something extra in the first file
                int end1 = start1 + 1;
                StringBuffer deletedText = new StringBuffer();
                deletedText.append(lines1[start1-1]).append('\n');
                while (end1 <= n && J[end1] < start2) {
                    String line = lines1[end1-1];
                    deletedText.append(line).append('\n');
                    end1++;
                }
                differences.add(new Variant(Variant.DELETE, start1, end1 - 1, start2 - 1, 0, deletedText.toString(), null));
                start1 = end1;
            } else { // There's something extra in the second file
                int end2 = J[start1];
                StringBuffer addedText = new StringBuffer();
                for (int i = start2; i < end2; i++) {
                    String line = lines2[i-1];
                    addedText.append(line).append('\n');
                }
                differences.add(new Variant(Variant.ADD, (start1 - 1), 0, start2, (end2 - 1), null, addedText.toString()));
                start2 = end2;
            }
        } while (start1 <= n);
        if (start2 <= m) { // There's something extra at the end of the second file
            int end2 = start2 + 1;
            StringBuilder addedText = new StringBuilder();
            addedText.append(lines2[start2-1]).append('\n');
            while (end2 <= m) {
                String line = lines2[end2-1];
                addedText.append(line).append('\n');
                end2++;
            }
            differences.add(new Variant(Variant.ADD, n, 0, start2, m, null, addedText.toString()));
        }
        return differences;
    }
    
    private static void cleanup(List<Variant> diffs) {
        Variant last = null;
        for (int i = 0; i < diffs.size(); i++) {
            Variant diff = diffs.get(i);
            if (last != null) {
                if (diff.getType() == Variant.ADD && last.getType() == Variant.DELETE ||
                    diff.getType() == Variant.DELETE && last.getType() == Variant.ADD) {

                    Variant add;
                    Variant del;
                    if (Variant.ADD == diff.getType()) {
                        add = diff;
                        del = last;
                    } else {
                        add = last;
                        del = diff;
                    }
                    int d1f1l1 = add.getFirstStart() - (del.getFirstEnd() - del.getFirstStart());
                    int d2f1l1 = del.getFirstStart();
                    if (d1f1l1 == d2f1l1) {
                        Variant newDiff = new Variant(Variant.CHANGE,
                            d1f1l1, del.getFirstEnd(), add.getSecondStart(), add.getSecondEnd(),
                            del.getFirstText(), add.getSecondText());
                        diffs.set(i - 1, newDiff);
                        diffs.remove(i);
                        i--;
                        diff = newDiff;
                    }
                }
            }
            last = diff;
        }
    }
    
    private static class Line {

        public int lineNo;
        public String line;
        public int hash;
        
        
        public Line(int lineNo, String line) {
            this.lineNo = lineNo;
            this.line = line;
            this.hash = line.hashCode();
        }
        
    }
    
    private static class Candidate {
        
        private int a;
        private int b;
        private Candidate c;
        
        public Candidate(int a, int b, Candidate c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    
}
