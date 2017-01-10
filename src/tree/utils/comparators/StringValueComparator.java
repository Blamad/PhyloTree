package tree.utils.comparators;

import java.util.Comparator;

/**
 * Created by Blamad on 10.01.2017.
 */
public class StringValueComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.toUpperCase().compareTo(o2.toUpperCase());
    }
}
