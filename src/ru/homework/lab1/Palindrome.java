package ru.homework.lab1;

public class Palindrome {

    public static void main(String[] args) {
//      test program
        for (String line : args) {
            if (isPalindrome(line)) {
                System.out.println(line);
            }
        }

    }

    //  method for reverse string
    public static String reverseString(String word) {
        int lens = word.length();
        String newWord = "";
        for (int i = lens - 1; i >= 0; i--) {
            newWord += word.charAt(i);
        }
        return newWord;
    }

    //  check is Palindrome our string
    public static boolean isPalindrome(String s) {
        String changeWord = reverseString(s);
        return s.equals(changeWord);
    }
}
