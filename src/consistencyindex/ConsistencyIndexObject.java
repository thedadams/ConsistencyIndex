/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 * @author thedadams
 */
public class ConsistencyIndexObject {

    boolean error = true;
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
    ArrayList<Integer> onBaseStreakMedian = new ArrayList<Integer>(0);
    ArrayList<Integer> noBaseStreakMedian = new ArrayList<Integer>(0);
    ArrayList<Integer> hitStreakMedian = new ArrayList<Integer>(0);
    ArrayList<Integer> noHitStreakMedian = new ArrayList<Integer>(0);
    Integer[] onBaseStreakMedianArray, noBaseStreakMedianArray;
    Integer[] hitStreakMedianArray, noHitStreakMedianArray;
    double noBaseStreakAverage = 0, onBaseStreakAverage = 0;
    double hitStreakAverage = 0, noHitStreakAverage = 0;
    double noBaseStreakStdDev = 0, onBaseStreakStdDev = 0;
    double noHitStreakStdDev = 0, hitStreakStdDev = 0;
    double onBaseCI = 0, hitCI = 0;
    int onBaseTopFivePercent, noBaseTopFivePercent;
    int hitTopFivePercent, noHitTopFivePercent;
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
        if (totalOnBaseGames < 2 || totalNoBaseGames < 2 || totalHitGames < 2 || totalNoHitGames < 2) {

            error = true;
            return;

        }

        onBaseStreakMedianArray = new Integer[onBaseStreakMedian.size()];
        for (int i = 0;
                i < onBaseStreakMedianArray.length;
                i++) {

            onBaseStreakMedianArray[i] = onBaseStreakMedian.get(i);

        }
        noBaseStreakMedianArray = new Integer[noBaseStreakMedian.size()];
        for (int i = 0;
                i < noBaseStreakMedianArray.length;
                i++) {

            noBaseStreakMedianArray[i] = noBaseStreakMedian.get(i);

        }

        Arrays.sort(onBaseStreakMedianArray);
        Arrays.sort(noBaseStreakMedianArray);

        hitStreakMedianArray = new Integer[hitStreakMedian.size()];
        for (int i = 0;
                i < hitStreakMedianArray.length;
                i++) {

            hitStreakMedianArray[i] = hitStreakMedian.get(i);

        }
        noHitStreakMedianArray = new Integer[noHitStreakMedian.size()];
        for (int i = 0;
                i < noHitStreakMedianArray.length;
                i++) {

            noHitStreakMedianArray[i] = noHitStreakMedian.get(i);

        }

        Arrays.sort(hitStreakMedianArray);

        Arrays.sort(noHitStreakMedianArray);

        if (noBaseStreakMedianArray.length
                % 2 == 0) {

            finalNoBaseStreakMedian = (((double) noBaseStreakMedianArray[(noBaseStreakMedianArray.length / 2) - 1]) + ((double) noBaseStreakMedianArray[(noBaseStreakMedianArray.length / 2)])) / 2;

        } else {

            finalNoBaseStreakMedian = ((double) noBaseStreakMedianArray[((noBaseStreakMedianArray.length - 1) / 2)]);

        }
        if (onBaseStreakMedianArray.length
                % 2 == 0) {

            finalOnBaseStreakMedian = ((double) onBaseStreakMedianArray[(onBaseStreakMedianArray.length / 2) - 1] + (double) onBaseStreakMedianArray[(onBaseStreakMedianArray.length / 2)]) / 2;

        } else {

            finalOnBaseStreakMedian = onBaseStreakMedianArray[((onBaseStreakMedianArray.length - 1) / 2)];

        }
        if (noHitStreakMedianArray.length
                % 2 == 0) {

            finalNoHitStreakMedian = (((double) noHitStreakMedianArray[(noHitStreakMedianArray.length / 2) - 1]) + ((double) noHitStreakMedianArray[(noHitStreakMedianArray.length / 2)])) / 2;

        } else {

            finalNoHitStreakMedian = ((double) noHitStreakMedianArray[((noHitStreakMedianArray.length - 1) / 2)]);

        }
        if (hitStreakMedianArray.length
                % 2 == 0) {

            finalHitStreakMedian = ((double) hitStreakMedianArray[(hitStreakMedianArray.length / 2) - 1] + (double) hitStreakMedianArray[(hitStreakMedianArray.length / 2)]) / 2;

        } else {

            finalHitStreakMedian = hitStreakMedianArray[((hitStreakMedianArray.length - 1) / 2)];

        }
        obp = (double) totalOnBase / (double) totalPA;
        ba = (double) totalHits / (double) totalAB;
        onBaseStreakAverage = ((double) totalOnBaseGames) / ((double) onBaseStreakCount);
        noBaseStreakAverage = ((double) totalNoBaseGames) / ((double) noBaseStreakCount);
        hitStreakAverage = ((double) totalHitGames) / ((double) hitStreakCount);
        noHitStreakAverage = ((double) totalNoHitGames) / ((double) noHitStreakCount);
        for (int i = 0;
                i < noBaseStreakMedianArray.length;
                i++) {

            noBaseStreakStdDev = noBaseStreakStdDev + Math.pow(noBaseStreakMedianArray[i] - noBaseStreakAverage, 2);

        }
        noBaseStreakStdDev = noBaseStreakStdDev / noBaseStreakCount;
        noBaseStreakStdDev = Math.sqrt(noBaseStreakStdDev);
        for (int i = 0;
                i < onBaseStreakMedianArray.length;
                i++) {

            onBaseStreakStdDev = onBaseStreakStdDev + Math.pow(onBaseStreakMedianArray[i] - onBaseStreakAverage, 2);

        }
        onBaseStreakStdDev = onBaseStreakStdDev / onBaseStreakCount;
        onBaseStreakStdDev = Math.sqrt(onBaseStreakStdDev);
        for (int i = 0;
                i < noHitStreakMedianArray.length;
                i++) {

            noHitStreakStdDev = noHitStreakStdDev + Math.pow(noHitStreakMedianArray[i] - noHitStreakAverage, 2);

        }
        noHitStreakStdDev = noHitStreakStdDev / noHitStreakCount;
        noHitStreakStdDev = Math.sqrt(noHitStreakStdDev);
        for (int i = 0;
                i < hitStreakMedianArray.length;
                i++) {

            hitStreakStdDev = hitStreakStdDev + Math.pow(hitStreakMedianArray[i] - hitStreakAverage, 2);

        }
        hitStreakStdDev = hitStreakStdDev / hitStreakCount;
        hitStreakStdDev = Math.sqrt(hitStreakStdDev);

        for (int i = 0;
                i < noHitStreakMedianArray.length;
                i++) {

            noHitStreakStdDev = noHitStreakStdDev + Math.pow(noHitStreakMedianArray[i] - noHitStreakAverage, 2);

        }
        noHitStreakStdDev = noHitStreakStdDev / noHitStreakCount;
        noHitStreakStdDev = Math.sqrt(noHitStreakStdDev);

        if (onBaseStreakMedianArray.length <= 1) {

            onBaseTopFivePercent = 0;

        } else {

            onBaseTopFivePercent = (int) Math.max(Math.floor(.05 * onBaseStreakCount), 1);

        }

        if (noBaseStreakMedianArray.length <= 1) {

            noBaseTopFivePercent = 0;

        } else {

            noBaseTopFivePercent = (int) Math.max(Math.floor(.05 * noBaseStreakCount), 1);

        }
        for (int i = onBaseTopFivePercent;
                i < 2 * onBaseTopFivePercent;
                i++) {
            onBaseCI = onBaseCI + onBaseStreakMedianArray[onBaseStreakMedianArray.length - i - 1];

        }
        onBaseCI = onBaseCI / (2 * onBaseTopFivePercent);
        for (int i = 0;
                i < onBaseTopFivePercent;
                i++) {
            tmp = tmp + onBaseStreakMedianArray[onBaseStreakMedianArray.length - i - 1];
        }

        tmp = tmp / (2 * onBaseTopFivePercent);

        onBaseCI = onBaseCI + tmp;
        tmp = 0;

        for (int i = noBaseTopFivePercent;
                i < 2 * noBaseTopFivePercent;
                i++) {
            tmp = tmp + noBaseStreakMedianArray[noBaseStreakMedianArray.length - i - 1];
        }
        tmp = tmp / (2 * noBaseTopFivePercent);
        onBaseCI = onBaseCI - tmp;
        tmp = 0;
        for (int i = 0;
                i < noBaseTopFivePercent;
                i++) {
            tmp = tmp + noBaseStreakMedianArray[noBaseStreakMedianArray.length - i - 1];
        }

        tmp = tmp / (2 * noBaseTopFivePercent);

        onBaseCI = onBaseCI - tmp;
        tmp = 0;

        onBaseCI = onBaseCI + (onBaseStreakAverage - noBaseStreakAverage) + (finalOnBaseStreakMedian - finalNoBaseStreakMedian);

        if (hitStreakMedianArray.length <= 1) {

            hitTopFivePercent = 0;

        } else {

            hitTopFivePercent = (int) Math.max(Math.floor(.05 * hitStreakCount), 1);

        }
        if (noHitStreakMedianArray.length <= 1) {

            noHitTopFivePercent = 0;

        } else {

            noHitTopFivePercent = (int) Math.max(Math.floor(.05 * noHitStreakCount), 1);

        }
        for (int i = hitTopFivePercent;
                i < 2 * hitTopFivePercent;
                i++) {
            hitCI = hitCI + hitStreakMedianArray[hitStreakMedianArray.length - i - 1];
        }
        hitCI = hitCI / (2 * hitTopFivePercent);
        for (int i = 0;
                i < hitTopFivePercent;
                i++) {
            tmp = tmp + hitStreakMedianArray[hitStreakMedianArray.length - i - 1];
        }

        tmp = tmp / (2 * hitTopFivePercent);

        hitCI = hitCI + tmp;
        tmp = 0;

        for (int i = noHitTopFivePercent;
                i < 2 * noHitTopFivePercent;
                i++) {
            tmp = tmp + noHitStreakMedianArray[noHitStreakMedianArray.length - i - 1];
        }

        tmp = tmp / (2 * noHitTopFivePercent);
        hitCI = hitCI - tmp;
        tmp = 0;

        for (int i = 0;
                i < noHitTopFivePercent;
                i++) {
            tmp = tmp + noHitStreakMedianArray[noHitStreakMedianArray.length - i - 1];
        }

        tmp = tmp / (2 * noHitTopFivePercent);
        hitCI = hitCI - tmp;
        tmp = 0;
        hitCI = hitCI + (hitStreakAverage - noHitStreakAverage) + (finalHitStreakMedian - finalNoHitStreakMedian);
        onBaseCI = (onBaseCI / 3);
        hitCI = (hitCI / 3);
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
}
