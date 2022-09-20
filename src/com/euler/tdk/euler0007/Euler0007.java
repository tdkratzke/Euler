package com.euler.tdk.euler0007;

import java.util.*;
import java.math.*;

public class Euler0007 {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        primes.add(3);

        for(int a0 = 0; a0 < t; a0++){
            int n = in.nextInt();
            int count = 1;
            int tempPrime = primes.get(primes.size()-1);

            for(int j=tempPrime;j<=1299709;j+=2){

                if(n<=primes.size()){
                    System.out.println(primes.get(n-1));
                    break;
                }

                if(isPrime(j)){
                    tempPrime = j;
                    count++;
                    if(tempPrime>primes.get(primes.size()-1)){
                        primes.add(j);
                    }
                }

                if(count==n){
                    System.out.println(tempPrime);
                    break;
                }
            }
        }in.close();
    }

    public static boolean isPrime(long num){
        for(int i=3;i<=Math.sqrt(num);i+=2){
            if(num%i==0){
                return false;
            }
        }
        //if(num%2==0){
        //    return false;
        //}
        return true;
    }
}