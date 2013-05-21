/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 * @author thedadams
 */
public class ConsistencyIndex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner std;
        boolean error = true;
        double obp = 0, ba = 0;
        int totalPA = 0, totalOnBase = 0, totalAB = 0, totalHits = 0;
        int pa, ab, hits, bb, hbp;
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
        int onBaseTopTenPercent, noBaseTopTenPercent;
        int hitTopTenPercent, noHitTopTenPercent;
        double tmp = 0;



        File file = new File("TedWilliamsPAData1941.dat");
        try {

            error = false;
            std = new Scanner(file);

            pa = std.nextInt();
            ab = std.nextInt();
            hits = std.nextInt();
            bb = std.nextInt();
            hbp = std.nextInt();

            totalPA = totalPA + pa;
            totalAB = totalAB + ab;
            totalOnBase = totalOnBase + hits + bb + hbp;
            totalHits = totalHits + hits;

            if (hits + bb + hbp == 0) {

                noBase++;
                totalNoBaseGames++;
                isOnBaseStreaking = false;

            } else {

                onBase++;
                totalOnBaseGames++;
                isOnBaseStreaking = true;

            }

            if (hits == 0) {

                noHit++;
                totalNoHitGames++;
                isHitStreaking = false;

            } else {

                didHit++;
                totalHitGames++;
                isHitStreaking = true;

            }

            while (std.hasNext()) {

                pa = std.nextInt();
                ab = std.nextInt();
                hits = std.nextInt();
                bb = std.nextInt();
                hbp = std.nextInt();

                totalPA = totalPA + pa;
                totalAB = totalAB + ab;
                totalOnBase = totalOnBase + (hits + bb + hbp);
                totalHits = totalHits + hits;

                if (hits + bb + hbp == 0) {

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
            std.close();
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

        } catch (FileNotFoundException e) {

            error = true;
            return;

        }

        onBaseStreakMedianArray = new Integer[onBaseStreakMedian.size()];
        for (int i = 0; i < onBaseStreakMedianArray.length; i++) {

            onBaseStreakMedianArray[i] = onBaseStreakMedian.get(i);

        }
        noBaseStreakMedianArray = new Integer[noBaseStreakMedian.size()];
        for (int i = 0; i < noBaseStreakMedianArray.length; i++) {

            noBaseStreakMedianArray[i] = noBaseStreakMedian.get(i);

        }

        Arrays.sort(onBaseStreakMedianArray);
        Arrays.sort(noBaseStreakMedianArray);

        hitStreakMedianArray = new Integer[hitStreakMedian.size()];
        for (int i = 0; i < hitStreakMedianArray.length; i++) {

            hitStreakMedianArray[i] = hitStreakMedian.get(i);

        }
        noHitStreakMedianArray = new Integer[noHitStreakMedian.size()];
        for (int i = 0; i < noHitStreakMedianArray.length; i++) {

            noHitStreakMedianArray[i] = noHitStreakMedian.get(i);

        }

        Arrays.sort(hitStreakMedianArray);
        Arrays.sort(noHitStreakMedianArray);

        if (noBaseStreakMedianArray.length % 2 == 0) {

            finalNoBaseStreakMedian = (((double) noBaseStreakMedianArray[(noBaseStreakMedianArray.length / 2) - 1]) + ((double) noBaseStreakMedianArray[(noBaseStreakMedianArray.length / 2)])) / 2;

        } else {

            finalNoBaseStreakMedian = ((double) noBaseStreakMedianArray[((noBaseStreakMedianArray.length - 1) / 2)]);

        }
        if (onBaseStreakMedianArray.length % 2 == 0) {

            finalOnBaseStreakMedian = ((double) onBaseStreakMedianArray[(onBaseStreakMedianArray.length / 2) - 1] + (double) onBaseStreakMedianArray[(onBaseStreakMedianArray.length / 2)]) / 2;

        } else {

            finalOnBaseStreakMedian = onBaseStreakMedianArray[((onBaseStreakMedianArray.length - 1) / 2)];

        }

        if (noHitStreakMedianArray.length % 2 == 0) {

            finalNoHitStreakMedian = (((double) noHitStreakMedianArray[(noHitStreakMedianArray.length / 2) - 1]) + ((double) noHitStreakMedianArray[(noHitStreakMedianArray.length / 2)])) / 2;

        } else {

            finalNoHitStreakMedian = ((double) noHitStreakMedianArray[((noHitStreakMedianArray.length - 1) / 2)]);

        }
        if (hitStreakMedianArray.length % 2 == 0) {

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

        for (int i = 0; i < noBaseStreakMedianArray.length; i++) {

            noBaseStreakStdDev = noBaseStreakStdDev + Math.pow(noBaseStreakMedianArray[i] - noBaseStreakAverage, 2);

        }

        noBaseStreakStdDev = noBaseStreakStdDev / noBaseStreakCount;
        noBaseStreakStdDev = Math.sqrt(noBaseStreakStdDev);

        for (int i = 0; i < onBaseStreakMedianArray.length; i++) {

            onBaseStreakStdDev = onBaseStreakStdDev + Math.pow(onBaseStreakMedianArray[i] - onBaseStreakAverage, 2);

        }

        onBaseStreakStdDev = onBaseStreakStdDev / onBaseStreakCount;
        onBaseStreakStdDev = Math.sqrt(onBaseStreakStdDev);


        for (int i = 0; i < noHitStreakMedianArray.length; i++) {

            noHitStreakStdDev = noHitStreakStdDev + Math.pow(noHitStreakMedianArray[i] - noHitStreakAverage, 2);

        }

        noHitStreakStdDev = noHitStreakStdDev / noHitStreakCount;
        noHitStreakStdDev = Math.sqrt(noHitStreakStdDev);

        for (int i = 0; i < hitStreakMedianArray.length; i++) {

            hitStreakStdDev = hitStreakStdDev + Math.pow(hitStreakMedianArray[i] - hitStreakAverage, 2);

        }

        hitStreakStdDev = hitStreakStdDev / hitStreakCount;
        hitStreakStdDev = Math.sqrt(hitStreakStdDev);

        onBaseTopTenPercent = (int) Math.floor(.1 * onBaseStreakCount);
        noBaseTopTenPercent = (int) Math.floor(.1 * noBaseStreakCount);

        for (int i = 0; i <= onBaseTopTenPercent; i++) {
            onBaseCI = onBaseCI + onBaseStreakMedianArray[onBaseStreakMedianArray.length - i - 1];
        }
        onBaseCI = onBaseCI / onBaseTopTenPercent;

        for (int i = 0; i <= noBaseTopTenPercent; i++) {
            tmp = tmp + noBaseStreakMedianArray[noBaseStreakMedianArray.length - i - 1];
        }
        tmp = tmp / noBaseTopTenPercent;
        onBaseCI = onBaseCI + tmp;
        tmp = 0;
        onBaseCI = onBaseCI + (onBaseStreakAverage - noBaseStreakAverage) + (finalOnBaseStreakMedian - finalNoBaseStreakMedian);

        hitTopTenPercent = (int) Math.floor(.1 * hitStreakCount);
        noHitTopTenPercent = (int) Math.floor(.1 * noHitStreakCount);

        for (int i = 0; i <= hitTopTenPercent; i++) {
            hitCI = hitCI + hitStreakMedianArray[hitStreakMedianArray.length - i - 1];
        }
        hitCI = hitCI / hitTopTenPercent;

        for (int i = 0; i <= noHitTopTenPercent; i++) {
            tmp = tmp + noHitStreakMedianArray[noHitStreakMedianArray.length - i - 1];
        }
        tmp = tmp / noHitTopTenPercent;
        onBaseCI = onBaseCI + tmp;
        tmp = 0;
        hitCI = hitCI + (hitStreakAverage - noHitStreakAverage) + (finalHitStreakMedian - finalNoHitStreakMedian);

        System.out.format("%23s %10s  %8s\n", " ", "No Base", " On Base");
        System.out.format("%23s : %8d  %8d\n", "Maximum Streaks", noBaseStreakMedianArray[noBaseStreakMedianArray.length - 1], onBaseStreakMedianArray[onBaseStreakMedianArray.length - 1]);
        System.out.format("%23s : %8d  %8d\n", "Last Top Streaks", noBaseStreakMedianArray[noBaseStreakMedianArray.length - noBaseTopTenPercent - 1], onBaseStreakMedianArray[onBaseStreakMedianArray.length - onBaseTopTenPercent - 1]);
        System.out.format("%23s : %8d  %8d\n", "Top 10% Streaks", noBaseTopTenPercent, onBaseTopTenPercent);
        System.out.format("%23s : %2.6f  %2.6f\n", "Average Streaks", noBaseStreakAverage, onBaseStreakAverage);
        System.out.format("%23s : %2.6f  %2.6f\n", "Median Streaks", finalNoBaseStreakMedian, finalOnBaseStreakMedian);
        System.out.format("%23s : %8d  %8d\n", "Number of Streaks", noBaseStreakCount, onBaseStreakCount);
        System.out.format("%23s : %8d  %8d\n\n", "Total Occurrences", totalNoBaseGames, totalOnBaseGames);
        System.out.format("%23s : %2.6f\n", "No Base Stand. Deviation", noBaseStreakStdDev);
        System.out.format("%23s : %2.6f\n", "On Base Stand. Deviation", onBaseStreakStdDev);
        System.out.format("%23s : %2.6f\n", "On Base Percentage", obp);
        System.out.format("%23s : %2.6f\n\n", "Batting Average", ba);
        System.out.format("%23s : %2.6f**\n\n", "**On Base Consistency Index", onBaseCI / 3);

        System.out.format("%23s %10s  %8s\n", " ", "No Hit", " Hit");
        System.out.format("%23s : %8d  %8d\n", "Maximum Streaks", noHitStreakMedianArray[noHitStreakMedianArray.length - 1], hitStreakMedianArray[hitStreakMedianArray.length - 1]);
        System.out.format("%23s : %8d  %8d\n", "Last Top Streaks", noHitStreakMedianArray[noHitStreakMedianArray.length - noHitTopTenPercent - 1], hitStreakMedianArray[hitStreakMedianArray.length - hitTopTenPercent - 1]);
        System.out.format("%23s : %8d  %8d\n", "Top 10% Streaks", noHitTopTenPercent, hitTopTenPercent);
        System.out.format("%23s : %2.6f  %2.6f\n", "Average Streaks", noHitStreakAverage, hitStreakAverage);
        System.out.format("%23s : %2.6f  %2.6f\n", "Median Streaks", finalNoHitStreakMedian, finalHitStreakMedian);
        System.out.format("%23s : %8d  %8d\n", "Number of Streaks", noHitStreakCount, hitStreakCount);
        System.out.format("%23s : %8d  %8d\n\n", "Total Occurrences", totalNoHitGames, totalHitGames);
        System.out.format("%23s : %2.6f\n", "No Hit Stand. Deviation", noHitStreakStdDev);
        System.out.format("%23s : %2.6f\n\n", "Hit Stand. Deviation", hitStreakStdDev);
        System.out.format("%23s : %2.6f**\n", "**Hit Consistency Index", hitCI / 3);

    }
}
