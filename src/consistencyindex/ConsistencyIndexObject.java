/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 * @author thedadams
 */
public class ConsistencyIndexObject {

    boolean error = true;
    boolean excludeTopFivePercent = true;
    double obp = 0, ba = 0;
    int totalPA = 0, totalOnBase = 0, totalAB = 0, totalHits = 0;
    int pa = 0, ab, hits, bb;
    int onBaseStreakCount = 0, noBaseStreakCount = 0;
    int hitStreakCount = 0, noHitStreakCount = 0;
    int totalOnBaseGames = 0, totalNoBaseGames = 0;
    int totalHitGames = 0, totalNoHitGames = 0;
    int onBase = 0, noBase = 0;
    int didHit = 0, noHit = 0;
    boolean isOnBaseStreaking = false, isHitStreaking = false;
    double finalOnBaseStreakMedian = 0, finalNoBaseStreakMedian = 0;
    double finalHitStreakMedian = 0, finalNoHitStreakMedian = 0;
    double finalDifferenceHitStreakMedian = 0, finalDifferenceOnBaseStreakMedian = 0;
    ArrayList<Integer> onBaseStreakMedian = new ArrayList<Integer>(0);
    ArrayList<Integer> noBaseStreakMedian = new ArrayList<Integer>(0);
    ArrayList<Integer> hitStreakMedian = new ArrayList<Integer>(0);
    ArrayList<Integer> noHitStreakMedian = new ArrayList<Integer>(0);
    Integer[] onBaseStreakMedianArray, noBaseStreakMedianArray;
    Integer[] hitStreakMedianArray, noHitStreakMedianArray;
    int[] differenceHitStreakMedianArray, differenceOnBaseStreakMedianArray;
    double noBaseStreakAverage = 0, onBaseStreakAverage = 0;
    double hitStreakAverage = 0, noHitStreakAverage = 0;
    double differenceHitStreakAverage = 0, differenceOnBaseStreakAverage = 0;
    double noBaseStreakStdDev = 0, onBaseStreakStdDev = 0;
    double noHitStreakStdDev = 0, hitStreakStdDev = 0;
    double onBaseCI = 0, hitCI = 0;
    int onBaseTopFivePercent, noBaseTopFivePercent;
    int hitTopFivePercent, noHitTopFivePercent;
    int differenceHitTopFivePercent, differenceOnBaseTopFivePercent;
    double tmp = 0;
    String[][] player;

    public void Calculate(String firstName, String lastName, String yearStart, String yearEnd, String hasPlayerID) {

        variableInit();

        player = GetPlayerInfo.main(firstName.trim(), lastName.trim(), yearStart.trim(), yearEnd.trim(), hasPlayerID);
        error = false;
        if (player == null) {

            error = true;
            return;

        }

        pa = Integer.parseInt(player[0][1]);
        ab = Integer.parseInt(player[0][2]);
        hits = Integer.parseInt(player[0][3]);
        bb = Integer.parseInt(player[0][4]);

        totalPA = totalPA + pa;
        totalAB = totalAB + ab;
        totalOnBase = totalOnBase + hits + bb;
        totalHits = totalHits + hits;

        if (hits + bb == 0) {

            noBase++;
            totalNoBaseGames++;
            isOnBaseStreaking = false;

        } else {

            onBase++;
            totalOnBaseGames++;
            isOnBaseStreaking = true;

        }

        if (ab != 0) {

            if (hits == 0) {

                noHit++;
                totalNoHitGames++;
                isHitStreaking = false;

            } else {

                didHit++;
                totalHitGames++;
                isHitStreaking = true;

            }

        }

        for (int i = 1; i < player.length; i++) {
            if (player[i][1] == null) {

                break;

            }

            pa = Integer.parseInt(player[i][1]);
            ab = Integer.parseInt(player[i][2]);
            hits = Integer.parseInt(player[i][3]);
            bb = Integer.parseInt(player[i][4]);

            totalPA = totalPA + pa;
            totalAB = totalAB + ab;
            totalOnBase = totalOnBase + (hits + bb);
            totalHits = totalHits + hits;

            if (hits + bb == 0) {

                if (isOnBaseStreaking) {

                    onBaseStreakMedian.add(onBase);
                    onBase = 0;
                    onBaseStreakCount++;
                    isOnBaseStreaking = false;

                }
                noBase++;
                totalNoBaseGames++;

            } else {

                if (!isOnBaseStreaking) {

                    isOnBaseStreaking = true;
                    noBaseStreakMedian.add(noBase);
                    noBase = 0;
                    noBaseStreakCount++;

                }
                onBase++;
                totalOnBaseGames++;
            }

            if (ab != 0) {

                if (hits == 0) {

                    if (isHitStreaking) {

                        hitStreakMedian.add(didHit);
                        didHit = 0;
                        hitStreakCount++;
                        isHitStreaking = false;

                    }
                    noHit++;
                    totalNoHitGames++;

                } else {

                    if (!isHitStreaking) {

                        isHitStreaking = true;
                        noHitStreakMedian.add(noHit);
                        noHit = 0;
                        noHitStreakCount++;

                    }
                    didHit++;
                    totalHitGames++;
                }

            }

        }
        if (isOnBaseStreaking) {

            onBaseStreakMedian.add(onBase);
            onBaseStreakCount++;

        } else {

            noBaseStreakMedian.add(noBase);
            noBaseStreakCount++;

        }
        if (isHitStreaking) {

            hitStreakMedian.add(didHit);
            hitStreakCount++;

        } else {

            noHitStreakMedian.add(noHit);
            noHitStreakCount++;

        }

        /* At this point we have all the streak data for the given player.
         * We now check to see if we have enough data (below), then calculate
         * the stats we need.
         */

        if (totalOnBaseGames < 2 || totalNoBaseGames < 2 || totalHitGames < 2 || totalNoHitGames < 2) {

            error = true;
            return;

        }

        this.calculateDifferences();
        this.calculateDifferenceMedians();
        this.calculateHitMedians();
        this.calculateOnBaseMedians();
        this.calculateTopFiveIndicies();
        this.calculateAllAverages(this.excludeTopFivePercent);
        this.calculateOnBaseCI();
        this.calculateHitCI();

    }

    void variableInit() {
        obp = 0;
        ba = 0;
        totalPA = 0;
        totalOnBase = 0;
        totalAB = 0;
        totalHits = 0;
        onBaseStreakCount = 0;
        noBaseStreakCount = 0;
        hitStreakCount = 0;
        noHitStreakCount = 0;
        totalOnBaseGames = 0;
        totalNoBaseGames = 0;
        totalHitGames = 0;
        totalNoHitGames = 0;
        onBase = 0;
        noBase = 0;
        didHit = 0;
        noHit = 0;
        isOnBaseStreaking = false;
        isHitStreaking = false;
        finalOnBaseStreakMedian = 0;
        finalNoBaseStreakMedian = 0;
        finalHitStreakMedian = 0;
        finalNoHitStreakMedian = 0;
        onBaseStreakMedian = new ArrayList<Integer>(0);
        noBaseStreakMedian = new ArrayList<Integer>(0);
        hitStreakMedian = new ArrayList<Integer>(0);
        noHitStreakMedian = new ArrayList<Integer>(0);
        noBaseStreakAverage = 0;
        onBaseStreakAverage = 0;
        hitStreakAverage = 0;
        noHitStreakAverage = 0;
        noBaseStreakStdDev = 0;
        onBaseStreakStdDev = 0;
        noHitStreakStdDev = 0;
        hitStreakStdDev = 0;
        onBaseCI = 0;
        hitCI = 0;
        hitTopFivePercent = 0;
        noHitTopFivePercent = 0;
        onBaseTopFivePercent = 0;
        noBaseTopFivePercent = 0;
        tmp = 0;
        player = new String[0][0];

    }

    void calculateDifferences() {

        int size = Math.min(this.onBaseStreakMedian.size(), this.noBaseStreakMedian.size());

        differenceOnBaseStreakMedianArray = new int[size];
        for (int i = 0; i < size; i++) {

            differenceOnBaseStreakMedianArray[i] = onBaseStreakMedian.get(i) - noBaseStreakMedian.get(i);

        }

        size = Math.min(this.hitStreakMedian.size(), this.noHitStreakMedian.size());

        differenceHitStreakMedianArray = new int[size];
        for (int i = 0; i < size; i++) {

            differenceHitStreakMedianArray[i] = hitStreakMedian.get(i) - noHitStreakMedian.get(i);

        }

    }

    void calculateOnBaseCI() {

        if (!this.excludeTopFivePercent) {

            for (int i = 0;
                    i < this.onBaseTopFivePercent;
                    i++) {
                this.tmp = this.tmp + this.onBaseStreakMedianArray[this.onBaseStreakMedianArray.length - i - 1];
            }

            this.tmp = this.tmp / (this.onBaseTopFivePercent);

            this.onBaseCI = this.onBaseCI + this.tmp;
            this.tmp = 0;

            for (int i = 0;
                    i < this.noBaseTopFivePercent;
                    i++) {
                this.tmp = this.tmp + this.noBaseStreakMedianArray[this.noBaseStreakMedianArray.length - i - 1];
            }

            this.tmp = this.tmp / (this.noBaseTopFivePercent);

            this.onBaseCI = this.onBaseCI - this.tmp;

        }

        this.tmp = 0;

        this.onBaseCI = this.onBaseCI + (this.onBaseStreakAverage - this.noBaseStreakAverage) + (this.finalOnBaseStreakMedian - this.finalNoBaseStreakMedian) + this.finalDifferenceOnBaseStreakMedian + this.differenceOnBaseStreakAverage;

        if (!this.excludeTopFivePercent) {

            this.onBaseCI = (this.onBaseCI / 5);

        } else {

            this.onBaseCI = (1 + obp) * (this.onBaseCI / 4);

        }
    }

    void calculateHitCI() {

        if (!this.excludeTopFivePercent) {

            for (int i = 0;
                    i < this.hitTopFivePercent;
                    i++) {
                this.tmp = this.tmp + this.hitStreakMedianArray[this.hitStreakMedianArray.length - i - 1];
            }

            this.tmp = this.tmp / (this.hitTopFivePercent);

            this.hitCI = this.hitCI + this.tmp;
            this.tmp = 0;

            for (int i = 0;
                    i < this.noHitTopFivePercent;
                    i++) {
                this.tmp = this.tmp + this.noHitStreakMedianArray[this.noHitStreakMedianArray.length - i - 1];
            }

            this.tmp = this.tmp / (this.noHitTopFivePercent);
            this.hitCI = this.hitCI - this.tmp;

        }

        this.tmp = 0;
        this.hitCI = this.hitCI + (this.hitStreakAverage - this.noHitStreakAverage) + (this.finalHitStreakMedian - this.finalNoHitStreakMedian) + this.finalDifferenceHitStreakMedian + this.differenceHitStreakAverage;

        if (!this.excludeTopFivePercent) {

            this.hitCI = (this.hitCI / 5);

        } else {

            this.hitCI = (1 + this.ba) * (this.hitCI / 4);

        }
    }

    void calculateOnBaseMedians() {

        this.onBaseStreakMedianArray = new Integer[this.onBaseStreakMedian.size()];
        for (int i = 0;
                i < this.onBaseStreakMedianArray.length;
                i++) {

            this.onBaseStreakMedianArray[i] = this.onBaseStreakMedian.get(i);

        }
        this.noBaseStreakMedianArray = new Integer[this.noBaseStreakMedian.size()];
        for (int i = 0;
                i < this.noBaseStreakMedianArray.length;
                i++) {

            this.noBaseStreakMedianArray[i] = this.noBaseStreakMedian.get(i);

        }

        Arrays.sort(this.onBaseStreakMedianArray);
        Arrays.sort(this.noBaseStreakMedianArray);

        if (this.noBaseStreakMedianArray.length
                % 2 == 0) {

            this.finalNoBaseStreakMedian = (((double) this.noBaseStreakMedianArray[(this.noBaseStreakMedianArray.length / 2) - 1]) + ((double) this.noBaseStreakMedianArray[(this.noBaseStreakMedianArray.length / 2)])) / 2;

        } else {

            this.finalNoBaseStreakMedian = ((double) this.noBaseStreakMedianArray[((this.noBaseStreakMedianArray.length - 1) / 2)]);

        }
        if (this.onBaseStreakMedianArray.length
                % 2 == 0) {

            this.finalOnBaseStreakMedian = ((double) this.onBaseStreakMedianArray[(this.onBaseStreakMedianArray.length / 2) - 1] + (double) this.onBaseStreakMedianArray[(this.onBaseStreakMedianArray.length / 2)]) / 2;

        } else {

            this.finalOnBaseStreakMedian = this.onBaseStreakMedianArray[((this.onBaseStreakMedianArray.length - 1) / 2)];

        }

    }

    void calculateDifferenceMedians() {

        Arrays.sort(this.differenceOnBaseStreakMedianArray);
        Arrays.sort(this.differenceHitStreakMedianArray);

        if (this.differenceOnBaseStreakMedianArray.length % 2 == 0) {

            this.finalDifferenceOnBaseStreakMedian = (((double) this.differenceOnBaseStreakMedianArray[(this.differenceOnBaseStreakMedianArray.length / 2) - 1]) + ((double) this.differenceOnBaseStreakMedianArray[(this.differenceOnBaseStreakMedianArray.length / 2)])) / 2;

        } else {

            this.finalDifferenceOnBaseStreakMedian = ((double) this.differenceOnBaseStreakMedianArray[((this.differenceOnBaseStreakMedianArray.length - 1) / 2)]);

        }
        if (this.differenceHitStreakMedianArray.length % 2 == 0) {

            this.finalDifferenceHitStreakMedian = ((double) this.differenceHitStreakMedianArray[(this.differenceHitStreakMedianArray.length / 2) - 1] + (double) this.differenceHitStreakMedianArray[(this.differenceHitStreakMedianArray.length / 2)]) / 2;

        } else {

            this.finalDifferenceHitStreakMedian = this.differenceHitStreakMedianArray[((this.differenceHitStreakMedianArray.length - 1) / 2)];

        }

    }

    void calculateHitMedians() {

        this.hitStreakMedianArray = new Integer[this.hitStreakMedian.size()];
        for (int i = 0;
                i < this.hitStreakMedianArray.length;
                i++) {

            this.hitStreakMedianArray[i] = this.hitStreakMedian.get(i);

        }
        this.noHitStreakMedianArray = new Integer[this.noHitStreakMedian.size()];
        for (int i = 0;
                i < this.noHitStreakMedianArray.length;
                i++) {

            this.noHitStreakMedianArray[i] = this.noHitStreakMedian.get(i);

        }

        Arrays.sort(this.hitStreakMedianArray);

        Arrays.sort(this.noHitStreakMedianArray);


        if (this.noHitStreakMedianArray.length
                % 2 == 0) {

            this.finalNoHitStreakMedian = (((double) this.noHitStreakMedianArray[(this.noHitStreakMedianArray.length / 2) - 1]) + ((double) this.noHitStreakMedianArray[(this.noHitStreakMedianArray.length / 2)])) / 2;

        } else {

            this.finalNoHitStreakMedian = ((double) this.noHitStreakMedianArray[((this.noHitStreakMedianArray.length - 1) / 2)]);

        }
        if (this.hitStreakMedianArray.length
                % 2 == 0) {

            this.finalHitStreakMedian = ((double) this.hitStreakMedianArray[(this.hitStreakMedianArray.length / 2) - 1] + (double) this.hitStreakMedianArray[(this.hitStreakMedianArray.length / 2)]) / 2;

        } else {

            this.finalHitStreakMedian = hitStreakMedianArray[((this.hitStreakMedianArray.length - 1) / 2)];

        }

    }

    void calculateAllAverages(boolean bool) {

        this.obp = (double) this.totalOnBase / (double) this.totalPA;
        this.ba = (double) this.totalHits / (double) this.totalAB;

        if (bool) {

            for (int i = 0; i < this.onBaseStreakCount - this.onBaseTopFivePercent; i++) {

                this.onBaseStreakAverage += this.onBaseStreakMedianArray[i];

            }

            this.onBaseStreakAverage = this.onBaseStreakAverage / (this.onBaseStreakCount - this.onBaseTopFivePercent);

            for (int i = 0; i < this.noBaseStreakCount - this.noBaseTopFivePercent; i++) {

                this.noBaseStreakAverage += this.noBaseStreakMedianArray[i];

            }

            this.noBaseStreakAverage = this.noBaseStreakAverage / (this.noBaseStreakCount - this.noBaseTopFivePercent);

            for (int i = 0; i < this.hitStreakCount - this.hitTopFivePercent; i++) {

                this.hitStreakAverage += this.hitStreakMedianArray[i];

            }

            this.hitStreakAverage = this.hitStreakAverage / (this.hitStreakCount - this.hitTopFivePercent);

            for (int i = 0; i < this.noHitStreakCount - this.noHitTopFivePercent; i++) {

                this.noHitStreakAverage += this.noHitStreakMedianArray[i];

            }

            this.noHitStreakAverage = this.noHitStreakAverage / (this.noHitStreakCount - this.noHitTopFivePercent);

            for (int i = 0; i < this.differenceOnBaseStreakMedianArray.length - this.differenceOnBaseTopFivePercent; i++) {

                this.differenceOnBaseStreakAverage += this.differenceOnBaseStreakMedianArray[i];

            }

            this.differenceOnBaseStreakAverage = this.differenceOnBaseStreakAverage / (this.differenceOnBaseStreakMedianArray.length - this.differenceOnBaseTopFivePercent);

            for (int i = 0; i < this.differenceHitStreakMedianArray.length - this.differenceHitTopFivePercent; i++) {

                this.differenceHitStreakAverage += this.differenceHitStreakMedianArray[i];

            }

            this.differenceHitStreakAverage = this.differenceHitStreakAverage / (this.differenceHitStreakMedianArray.length - this.differenceHitTopFivePercent);
        } else {

            this.onBaseStreakAverage = ((double) this.totalOnBaseGames) / ((double) this.onBaseStreakCount);
            this.noBaseStreakAverage = ((double) this.totalNoBaseGames) / ((double) this.noBaseStreakCount);
            this.hitStreakAverage = ((double) this.totalHitGames) / ((double) this.hitStreakCount);
            this.noHitStreakAverage = ((double) this.totalNoHitGames) / ((double) this.noHitStreakCount);
            this.differenceOnBaseStreakAverage = calculateVectorAverage(differenceOnBaseStreakMedianArray);
            this.differenceHitStreakAverage = calculateVectorAverage(differenceHitStreakMedianArray);

        }

    }

    double calculateVectorAverage(int[] array) {

        double average = 0;

        for (int i = 0; i < array.length; i++) {

            average += array[i];

        }

        return average / array.length;

    }

    void calculateTopFiveIndicies() {

        if (this.onBaseStreakMedianArray.length <= 1) {

            this.onBaseTopFivePercent = 0;

        } else {

            this.onBaseTopFivePercent = (int) Math.floor(.05 * this.onBaseStreakCount);

        }

        if (this.noBaseStreakMedianArray.length <= 1) {

            this.noBaseTopFivePercent = 0;

        } else {

            this.noBaseTopFivePercent = (int) Math.floor(.05 * this.noBaseStreakCount);

        }

        if (this.hitStreakMedianArray.length <= 1) {

            this.hitTopFivePercent = 0;

        } else {

            this.hitTopFivePercent = (int) Math.floor(.05 * this.hitStreakCount);

        }
        if (this.noHitStreakMedianArray.length <= 1) {

            this.noHitTopFivePercent = 0;

        } else {

            this.noHitTopFivePercent = (int) Math.floor(.05 * this.noHitStreakCount);

        }

        if (this.differenceOnBaseStreakMedianArray.length <= 1) {

            this.differenceOnBaseTopFivePercent = 0;

        } else {

            this.differenceOnBaseTopFivePercent = (int) Math.floor(.05 * this.differenceOnBaseStreakMedianArray.length);

        }

        if (this.differenceHitStreakMedianArray.length <= 1) {

            this.differenceHitTopFivePercent = 0;

        } else {

            this.differenceHitTopFivePercent = (int) Math.floor(.05 * this.differenceHitStreakMedianArray.length);

        }

    }
}
