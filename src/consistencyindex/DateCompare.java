/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.util.Comparator;

class DateCompare implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        String[] row1 = (String[]) o1;
        String[] row2 = (String[]) o2;

        return row1[0].substring(4, 13).compareTo(row2[0].substring(4, 13));

    }
}
