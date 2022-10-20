package ru.homework.lab1;

public class Primes {

    //  print on screen result and numbers
    public static void main(String[] args) {
        for (int i = 2; i <= 100; i++) {
            if (isPrime(i)){
                System.out.println(i);
            }
        }
    }

    //  check isSimple our number
    public static boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
