package tree.utils.comparators;

import java.util.Comparator;

/**
 * Created by Aga on 2016-12-28.
 */
public class StringLengthComparator implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        return new Integer(o2.length()).compareTo(o1.length());
    }
}
