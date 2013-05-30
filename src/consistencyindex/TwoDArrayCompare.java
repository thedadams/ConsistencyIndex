/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.util.Comparator;

class TwoDArrayCompare implements Comparator {

    int col;

    public TwoDArrayCompare() {

        this(0);

    }

    public TwoDArrayCompare(int i) {

        col = i;

    }

    @Override
    public int compare(Object o1, Object o2) {

        String[] row1 = (String[]) o1;
        String[] row2 = (String[]) o2;

        if (Double.parseDouble(row1[col]) > Double.parseDouble(row2[col])) {

            return -1;

        } else if (Double.parseDouble(row1[col]) < Double.parseDouble(row2[col])) {

            return 1;

        } else {

            return 0;

        }

    }
}
