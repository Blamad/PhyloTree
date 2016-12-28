package tree.utils;

import java.util.Comparator;

/**
 * Created by Aga on 2016-12-28.
 */
public class LengthComparator implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        return new Integer(o1.length()).compareTo(o2.length());
    }
}
