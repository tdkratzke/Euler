package com.euler.tdk.euler0205;
import java.math.*;

public class Euler0205 {
    public static void main(String[] args) throws InterruptedException {

        double total = 0;
        double totalTri = 0;

        MultiEuler0205 threads1 = new MultiEuler0205();
        MultiEuler0205 threads2 = new MultiEuler0205();
        MultiEuler0205 threads3 = new MultiEuler0205();
        MultiEuler0205 threads4 = new MultiEuler0205();
        MultiEuler0205 threads5 = new MultiEuler0205();
        MultiEuler0205 threads6 = new MultiEuler0205();
        MultiEuler0205 threads7 = new MultiEuler0205();
        MultiEuler0205 threads8 = new MultiEuler0205();
        MultiEuler0205 threads9 = new MultiEuler0205();
        MultiEuler0205 threads10 = new MultiEuler0205();
        MultiEuler0205 threads11 = new MultiEuler0205();
        MultiEuler0205 threads12 = new MultiEuler0205();
        MultiEuler0205 threads13 = new MultiEuler0205();
        MultiEuler0205 threads14 = new MultiEuler0205();
        MultiEuler0205 threads15 = new MultiEuler0205();
        MultiEuler0205 threads16 = new MultiEuler0205();

        threads1.start();
        threads2.start();
        threads3.start();
        threads4.start();
        threads5.start();
        threads6.start();
        threads7.start();
        threads8.start();
        threads9.start();
        threads10.start();
        threads11.start();
        threads12.start();
        threads13.start();
        threads14.start();
        threads15.start();
        threads16.start();

        Thread.sleep(60000);

        total += threads1.getTotal();
        total += threads2.getTotal();
        total += threads3.getTotal();
        total += threads4.getTotal();
        total += threads5.getTotal();
        total += threads6.getTotal();
        total += threads7.getTotal();
        total += threads8.getTotal();
        total += threads9.getTotal();
        total += threads10.getTotal();
        total += threads11.getTotal();
        total += threads12.getTotal();
        total += threads13.getTotal();
        total += threads14.getTotal();
        total += threads15.getTotal();
        total += threads16.getTotal();

        totalTri += threads1.getTriangleWins();
        totalTri += threads2.getTriangleWins();
        totalTri += threads3.getTriangleWins();
        totalTri += threads4.getTriangleWins();
        totalTri += threads5.getTriangleWins();
        totalTri += threads6.getTriangleWins();
        totalTri += threads7.getTriangleWins();
        totalTri += threads8.getTriangleWins();
        totalTri += threads9.getTriangleWins();
        totalTri += threads10.getTriangleWins();
        totalTri += threads11.getTriangleWins();
        totalTri += threads12.getTriangleWins();
        totalTri += threads13.getTriangleWins();
        totalTri += threads14.getTriangleWins();
        totalTri += threads15.getTriangleWins();
        totalTri += threads16.getTriangleWins();


        System.out.println(totalTri/total);
    }
}
