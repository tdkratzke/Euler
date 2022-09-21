package com.euler.tdk.euler0009;
import java.util.*;

public class euler0009 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            int n = in.nextInt();
            System.out.println(pythag(n));
        }in.close();
    }

    public static int pythag(int target){
        int temp=-1;
        for(int a=1;a<=(target/3)-1;a++){
            for(int b=a+1;b<=(target-a)/2;b++){
                int c=target-b-a;
                if(a*a+b*b==c*c&&a*b*c>temp){
                    temp = a*b*c;
                }
            }
        }

        return temp;
    }
}