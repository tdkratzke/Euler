package com.euler.tdk.euler0205;

public class MultiEuler0205 extends Thread{
    int triangles = 0;
    int squares = 0;
    double total = 0;
    double triangleWins = 0;
    long millis = System.currentTimeMillis();


    @Override
    public void run(){
        while(System.currentTimeMillis() - millis < 60000){
            total ++;
            triangles = 0;
            squares = 0;
            for(int t = 0; t < 9; t++){
                triangles += Math.floor((Math.random() * 4) + 1);
            }
            for(int s = 0; s < 6; s++){
                squares += Math.floor((Math.random() * 6) + 1);
            }

            if(triangles > squares){
                triangleWins ++;
            }
        }
    }

    public double getTotal() {
        return total;
    }
    public double getTriangleWins() {
        return triangleWins;
    }
}
