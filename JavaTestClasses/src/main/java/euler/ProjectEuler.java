package euler;

import helper.ExerciseBaseClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.math.BigInteger;

/**
 * 2019-01-06: Solve the first 50 Project Euler problems for coding/algorithm practice.
 *
 * Project Euler problems: https://projecteuler.net/archives
 * Original article:
 * https://blog.usejournal.com/consider-yourself-a-developer-you-should-solve-the-project-euler-problems-ed8d13397c9c
 */
@SuppressWarnings("unused")
public class ProjectEuler extends ExerciseBaseClass {
  // This caches prime numbers up to the given limit using the Sieve of Eratosthenes.
  // I am only caching up to the largest value required by the Euler problems
  // (the max is somewhere between Integer.MAX_VALUE/20 and Integer.MAX_VALUE/10)
  private static int primeLimit = 20000000;
  private static ArrayList<Integer> primeCache = getPrimeNumbersUsingSieveOfEratosthenes(primeLimit);

  /*
   * 001. Multiples of 3 and 5
   *
   * If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9.
   * The sum of these multiples is 23. Find the sum of all the multiples of 3 or 5 below 1000.
   */
  static final int MAX_3_AND_5_FACTORS = 1000;
  public static int multiplesOf3And5() {
    int sum = 0;
    for (int i = 3; i < MAX_3_AND_5_FACTORS; i++) {
      if (i % 3 == 0 || i % 5 == 0) {
        sum += i;
      }
    }
    return sum;
  }

  /*
   * 002. Even Fibonacci numbers
   *
   * Each new term in the Fibonacci sequence is generated by adding the previous two terms.
   * By starting with 1 and 2, the first 10 terms will be: 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
   * By considering the terms in the Fibonacci sequence whose values do not exceed four million,
   * find the sum of the even-valued terms.
   */
  static final int MAX_FIB = 4000000;
  public static int evenFibonacciNumbers() {
    int sum = 0;
    int prev1 = 0;
    int prev2 = 1;
    while (prev1 + prev2 <= MAX_FIB) {
      int temp = prev1 + prev2;
      if (temp % 2 == 0) {
        sum += temp;
      }
      prev1 = prev2;
      prev2 = temp;
    }
    return sum;
  }

  /*
   * So many Euler Project problems require different types of factoring that
   * it makes sense to create a generic helper method for it.
   */
  enum FactorMode {
    DEFAULT,
    PRIME_CHECK,
    THREE_DIGIT_FACTORS,
    EVEN_FACTORS_UP_TO_20
  }

  static boolean threeDigitNumber(long number) {
    return number >= 100 && number < 1000;
  }

  static Set<Long> factorUtility(long number, FactorMode mode) {
    Set<Long> factors = new HashSet<>();
    long nextHighFactor = number;
    boolean done = false;

    for (long nextLowFactor = 2; nextLowFactor < nextHighFactor; nextLowFactor++) {
      if (number % nextLowFactor == 0) {
        nextHighFactor = number/nextLowFactor;

        boolean update = true;
        // Note that I am using the case fall-through behavior here
        switch (mode) {
          case PRIME_CHECK:   // Shortcut for using this method to determine primes
            done = true;
            break;
          case THREE_DIGIT_FACTORS:
            if (threeDigitNumber(nextLowFactor) && threeDigitNumber(nextHighFactor)) {
              ExerciseBaseClass.debugLevel(2, "Two 3-digit factors: " + nextLowFactor + ", " + nextHighFactor);
              done = true;
            }
            else {
              update = false;
            }
            break;
          case EVEN_FACTORS_UP_TO_20:
            if (nextLowFactor > 20) {
              done = true;
            }
            else if (nextHighFactor % 2 != 0) {
              update = false;
            }
            break;
          default:
        }
        if (update) {
          ExerciseBaseClass.debugLevel(2, "Adding factor " + nextLowFactor);
          factors.add(nextLowFactor);
          if (nextHighFactor != 1L && nextHighFactor != nextLowFactor) {
            ExerciseBaseClass.debugLevel(2, "Adding factor " + nextHighFactor);
            factors.add(nextHighFactor);
          }
        }
      }
      if (done) { break; }
    }
    return factors;
  }

  /*
   * 003. Largest prime factor
   *
   * The prime factors of 13195 are 5, 7, 13 and 29.
   * What is the largest prime factor of the number 600851475143?
   * The answer is: 6857
   *
   * Note that the factoring methods below are designed to return
   * all factors of a number except 1 and the number.
   */
  static Set<Long> factorNumber(long number) {
    return factorUtility(number, FactorMode.DEFAULT);
  }

  static boolean numberIsPrimeBruteForce(long number) {
    return factorUtility(number, FactorMode.PRIME_CHECK).isEmpty();
  }

  static boolean numberIsPrime(int number) {
    return primeCache.contains(number);
  }

  // Not sure if I can extend the sieve algorithm on demand to find larger primes.
  // (make a note about this)
  // If not then I'll just have to cache the largest amount possible (square root of MAX_LONG?)

  // Will have to watch for overflow with this algorithm
  // Add some more documentation of this algorithm
  // Index at i represents 2i + 1 (we are skipping all even numbers)
  // This fails with "OutOfMemoryError: Java heap space" for values much greater than Integer.MAX_VALUE/20.
  static ArrayList<Integer> getPrimeNumbersUsingSieveOfEratosthenes(int number) {
    ArrayList<Boolean> isPrime = new ArrayList<>();
    int sieveLimit = number/2;
    // We mark prime numbers as true and ignore all even indexes
    isPrime.addAll(Collections.nCopies(sieveLimit, Boolean.TRUE));
    ArrayList<Integer> primeList = new ArrayList<>(Arrays.asList(2));

    // Note that I am combining the sieving algorithm with constructing the prime list
    for (long i = 1; i < sieveLimit; i++) {
      if (isPrime.get((int)i)) {
        if (i*i < number) {
          for (long j = 2*i*(i + 1); j < sieveLimit; j += 2*i + 1) {
            isPrime.set((int)j, Boolean.FALSE);
          }
        }
        primeList.add(2*(int)i + 1);
      }
    }
    return primeList;
  }

  // This version causes numeric overflow in j, even though it seems like it should never get that high.
  static ArrayList<Integer> getPrimeNumbersUsingSieveOfEratosthenesOverflow(int number) {
    ArrayList<Boolean> sieve = new ArrayList<>();
    int sieveLimit = number/2;
    // We mark prime numbers as true and ignore all even indexes
    sieve.addAll(Collections.nCopies(sieveLimit, Boolean.TRUE));
    ArrayList<Integer> primeList = new ArrayList<>(Arrays.asList(2));

    for (int i = 1; i < sieveLimit; i++) {
      if (sieve.get(i)) {
        if (i*i < number) {
          // Keep getting ArrayIndexOutOfBoundsException: -2147334008
          // here for numbers that shouldn't be that big
          for (int j = 2*i*(i + 1); j < sieveLimit; j += 2*i + 1) {
            if (Math.addExact(j, 2*i + 1) < 1) {
              ExerciseBaseClass.debugLevel(0,
                  "int j will overflow for the sum of " + j + " and " + 2*i + 1);
            }
            sieve.set(j, Boolean.FALSE);
          }
        }
        primeList.add(2*i + 1);
        // This caused error "IndexOutOfBoundsException: Index: 249997, Size: 249996"
        // sieve.remove(i);
      }
    }
    return primeList;
  }

  // Note that this took more than 24 hours to factor 600851475143!
  static Set<Long> slowFactorNumber(long number) {
    Set<Long> factors = new HashSet<>();
    for (long factorBy = 2; factorBy + 1 <= number/2; factorBy++) {
      // Had to add this to make sure my code wasn't hanging!
      if (factorBy % 10000000000L == 0) {
        ExerciseBaseClass.debugLevel(2, getCurrentTimestamp() + ": Up to " + factorBy);
      }
      if (!factors.contains(factorBy) && number % factorBy == 0) {
        Long newFactor = number/factorBy;
        ExerciseBaseClass.debugLevel(2, "Adding factor " + factorBy);
        factors.add(factorBy);
        if (newFactor != 1L && newFactor != factorBy) {
          ExerciseBaseClass.debugLevel(2, "Adding factor " + factorBy);
          factors.add(newFactor);
        }
      }
    }
    return factors;
  }

  // Note that this fails with a StackOverflowError for sufficiently large numbers!
  // (should probably try and figure out what the approximate limit is)
  static void recursiveFactorNumber(long number, long factorBy, Set<Long> factors) {
    if (!factors.contains(factorBy) && number % factorBy == 0) {
      Long newFactor = number/factorBy;
      ExerciseBaseClass.debugLevel(2, "Adding factor " + factorBy);
      factors.add(factorBy);
      if (newFactor != 1L && newFactor != factorBy) {
        ExerciseBaseClass.debugLevel(2, "Adding factor " + factorBy);
        factors.add(newFactor);
      }
    }
    if (factorBy + 1 <= number/2) {
      recursiveFactorNumber(number, factorBy + 1, factors);
    }
  }

  static Set<Long> recursiveFactorNumber(long number) {
    Set<Long> factors = new HashSet<>();
    recursiveFactorNumber(number, 2, factors);
    return factors;
  }

  static final long PRIME_FACTOR_NUM = 600851475143L;
  public static long largestPrimeFactor() {
    Long largestPrimeFactor = 1L;
    for (long number : factorNumber(PRIME_FACTOR_NUM)) {
      if (numberIsPrime((int)number) && number > largestPrimeFactor) {
        ExerciseBaseClass.debugLevel(2, "Setting largest prime factor to " + number);
        largestPrimeFactor = number;
      }
    }
    return largestPrimeFactor;
  }

  /*
   * 004. Largest palindrome product
   *
   * A palindromic number reads the same both ways. The largest palindrome made
   * from the product of two 2-digit numbers is 9009 = 91 × 99. Find the largest
   * palindrome made from the product of two 3-digit numbers.
   *
   * The answer is 906609.
   */
  // Doesn't work for 0 (need a special case?)
  static ArrayList<Integer> convertNumberToDigits(int number) {
    ArrayList<Integer> digits = new ArrayList<>();
    for (int n = number; n > 0; n /= 10) {
      digits.add(0, n % 10);
    }
    return digits;
  }

  static boolean isPalindrome(ArrayList<Integer> digits) {
    for (int i = 0; i < digits.size()/2; i++) {
      if (!digits.get(i).equals(digits.get(digits.size() - i - 1))) {
        return false;
      }
    }
    return true;
  }

  public static int largestPalindromeProduct() {
    for (int number = 999*999; number > 100*100; number--) {
      if (isPalindrome(convertNumberToDigits(number))) {
        Set<Long> factors = factorUtility(number, FactorMode.THREE_DIGIT_FACTORS);
        if (!factors.isEmpty()) {
          return number;
        }
      }
    }
    return 0;
  }

  /*
   * 005. Smallest evenly divisible multiple
   *
   * 2520 is the smallest number that can be divided by each of the numbers from 1 to 10 without any remainder.
   * What is the smallest positive number that is *evenly divisible* by all of the numbers from 1 to 20?
   *
   * Wrong answers:
   *
   * 1. 11*12*13*14*15*16*17*18*19*20 = 427674624 (but this value is BOGUS due to OVERFLOW!)
   * 2. 2*3*4*5*7*11*13*17*19 = 38798760 (this is missing factors [9, 16, 18])
   *
   * #1 is the maximum possible correct value, while #2 is the minimum possible value.
   * I came up with #2 by going up from 1 and omitting numbers that were multiples of
   * lower numbers.
   *
   * I got the right answer by figuring out that if I multiplied #2 by 2 and 3 it
   * would include the missing factors (9, 16, and 18).
   */
  public static int smallestEvenlyDivisibleMultipleOfOneThroughTwenty() {
/*
    // This version hangs...
    // If I can't get it to work I should remove the EVEN_FACTORS_UP_TO_20 case from factorUtility()
    int number = 427674624;
    while (true) {
      Set<Long> results = factorUtility(number, FactorMode.EVEN_FACTORS_UP_TO_20);
      if (results.size() == 19) {
        break;
      }
      number++;
    }
    return number;
*/
    // Check if other factor is even, if not multiply by 2?
/*
    int multiple = 1;
    for (int i = 11; i <= 20; i++) {
      multiple *= i;
      ExerciseBaseClass.debugLevel(0, "Multiplied by " + i + " (current total = " + multiple + ")");
    }
    return multiple;
*/
    // Is there any more systematic way to derive this value?
    return 2*2*3*3*4*5*7*11*13*17*19;
  }

  // This is used to check answers for the above Euler problem
  static boolean checkForFactorsOneThroughTwenty(int number) {
    ArrayList<Integer> nonFactors = new ArrayList<>();
    for (int i = 2; i <= 20; i++) {
      if (number % i != 0) {
        ExerciseBaseClass.debugLevel(0, i + " is not a factor of " + number);
        nonFactors.add(i);
      }
    }
    if (nonFactors.size() > 0) {
      ExerciseBaseClass.debugLevel(0, "The following aren't factors of " + number + ": " + nonFactors.toString());
    }
    return nonFactors.size() == 0;
  }

  /*
   * 006. Difference between sum of squares and square of sum
   *
   * The sum of the squares of the first ten natural numbers is: 1^2 + 2^2 + ... + 10^2 = 385
   * The square of the sum of the first ten natural numbers is: (1 + 2 + ... + 10)^2 = 55^2 = 3025
   * Hence the difference between the sum of the squares of the first ten natural numbers and the
   * square of the sum is 3025 − 385 = 2640.
   *
   * Find the difference between the sum of the squares of the first one hundred natural numbers
   * and the square of the sum.
   */
  public static int squareOfSumMinusSumOfSquares(int max) {
    int sumOfOneToMax = 0;
    for (int i = 1; i <= max; i++) { sumOfOneToMax += i; }
    int result = 0;
    for (int i = 1; i <= max; i++) {
      result += i*(sumOfOneToMax - i);
    }
    return result;
  }

  /*
   * 007. 10,001st prime number
   *
   * By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see that the 6th prime is 13.
   * What is the 10,001st prime number?
   */
  public static int find10001stPrimeNumberBruteForce() {
    int primeNumbers = 0;
    for (int i = 2; i < Integer.MAX_VALUE && primeNumbers < 10001; i++) {
      if (numberIsPrime(i)) {
        primeNumbers++;
        if (primeNumbers % 1000 == 0) {
          ExerciseBaseClass.debugLevel(1, String.format("Prime number %d is %d", primeNumbers, i));
        }
        if (primeNumbers == 10001) {
          ExerciseBaseClass.debugLevel(1, "The 10001st prime number is " + i);
          return i;
        }
      }
    }
    return 0;
  }


  public static int find10001stPrimeNumber() {
    return primeCache.get(10000);
  }

  /*
   * 008. Largest product in a series
   *
   * The four adjacent digits in the 1000-digit number that have the greatest product are 9 × 9 × 8 × 9 = 5832.
   *
   * 7316717653133062491922511967442657474235534919493496983520312774506326239578318016984801869478851843
   * 8586156078911294949545950173795833195285320880551112540698747158523863050715693290963295227443043557
   * 6689664895044524452316173185640309871112172238311362229893423380308135336276614282806444486645238749
   * 3035890729629049156044077239071381051585930796086670172427121883998797908792274921901699720888093776
   * 6572733300105336788122023542180975125454059475224352584907711670556013604839586446706324415722155397
   * 5369781797784617406495514929086256932197846862248283972241375657056057490261407972968652414535100474
   * 8216637048440319989000889524345065854122758866688116427171479924442928230863465674813919123162824586
   * 1786645835912456652947654568284891288314260769004224219022671055626321111109370544217506941658960408
   * 0719840385096245544436298123098787992724428490918884580156166097919133875499200524063689912560717606
   * 0588611646710940507754100225698315520005593572972571636269561882670428252483600823257530420752963450
   *
   * Find the thirteen adjacent digits in the 1000-digit number that have the greatest product.
   * What is the value of this product?
   *
   * I basically figured out the correct algorithm right away, but I had to switch to using
    * BigInteger to deal with numeric overflow from the int type.
   */
  private static final int[] thousandDigitNumber = new int[] {
      7, 3, 1, 6, 7, 1, 7, 6, 5, 3, 1, 3, 3, 0, 6, 2, 4, 9, 1, 9, 2, 2, 5, 1, 1,
      9, 6, 7, 4, 4, 2, 6, 5, 7, 4, 7, 4, 2, 3, 5, 5, 3, 4, 9, 1, 9, 4, 9, 3, 4,
      9, 6, 9, 8, 3, 5, 2, 0, 3, 1, 2, 7, 7, 4, 5, 0, 6, 3, 2, 6, 2, 3, 9, 5, 7,
      8, 3, 1, 8, 0, 1, 6, 9, 8, 4, 8, 0, 1, 8, 6, 9, 4, 7, 8, 8, 5, 1, 8, 4, 3,
      8, 5, 8, 6, 1, 5, 6, 0, 7, 8, 9, 1, 1, 2, 9, 4, 9, 4, 9, 5, 4, 5, 9, 5, 0,
      1, 7, 3, 7, 9, 5, 8, 3, 3, 1, 9, 5, 2, 8, 5, 3, 2, 0, 8, 8, 0, 5, 5, 1, 1,
      1, 2, 5, 4, 0, 6, 9, 8, 7, 4, 7, 1, 5, 8, 5, 2, 3, 8, 6, 3, 0, 5, 0, 7, 1,
      5, 6, 9, 3, 2, 9, 0, 9, 6, 3, 2, 9, 5, 2, 2, 7, 4, 4, 3, 0, 4, 3, 5, 5, 7,
      6, 6, 8, 9, 6, 6, 4, 8, 9, 5, 0, 4, 4, 5, 2, 4, 4, 5, 2, 3, 1, 6, 1, 7, 3,
      1, 8, 5, 6, 4, 0, 3, 0, 9, 8, 7, 1, 1, 1, 2, 1, 7, 2, 2, 3, 8, 3, 1, 1, 3,
      6, 2, 2, 2, 9, 8, 9, 3, 4, 2, 3, 3, 8, 0, 3, 0, 8, 1, 3, 5, 3, 3, 6, 2, 7,
      6, 6, 1, 4, 2, 8, 2, 8, 0, 6, 4, 4, 4, 4, 8, 6, 6, 4, 5, 2, 3, 8, 7, 4, 9,
      3, 0, 3, 5, 8, 9, 0, 7, 2, 9, 6, 2, 9, 0, 4, 9, 1, 5, 6, 0, 4, 4, 0, 7, 7,
      2, 3, 9, 0, 7, 1, 3, 8, 1, 0, 5, 1, 5, 8, 5, 9, 3, 0, 7, 9, 6, 0, 8, 6, 6,
      7, 0, 1, 7, 2, 4, 2, 7, 1, 2, 1, 8, 8, 3, 9, 9, 8, 7, 9, 7, 9, 0, 8, 7, 9,
      2, 2, 7, 4, 9, 2, 1, 9, 0, 1, 6, 9, 9, 7, 2, 0, 8, 8, 8, 0, 9, 3, 7, 7, 6,
      6, 5, 7, 2, 7, 3, 3, 3, 0, 0, 1, 0, 5, 3, 3, 6, 7, 8, 8, 1, 2, 2, 0, 2, 3,
      5, 4, 2, 1, 8, 0, 9, 7, 5, 1, 2, 5, 4, 5, 4, 0, 5, 9, 4, 7, 5, 2, 2, 4, 3,
      5, 2, 5, 8, 4, 9, 0, 7, 7, 1, 1, 6, 7, 0, 5, 5, 6, 0, 1, 3, 6, 0, 4, 8, 3,
      9, 5, 8, 6, 4, 4, 6, 7, 0, 6, 3, 2, 4, 4, 1, 5, 7, 2, 2, 1, 5, 5, 3, 9, 7,
      5, 3, 6, 9, 7, 8, 1, 7, 9, 7, 7, 8, 4, 6, 1, 7, 4, 0, 6, 4, 9, 5, 5, 1, 4,
      9, 2, 9, 0, 8, 6, 2, 5, 6, 9, 3, 2, 1, 9, 7, 8, 4, 6, 8, 6, 2, 2, 4, 8, 2,
      8, 3, 9, 7, 2, 2, 4, 1, 3, 7, 5, 6, 5, 7, 0, 5, 6, 0, 5, 7, 4, 9, 0, 2, 6,
      1, 4, 0, 7, 9, 7, 2, 9, 6, 8, 6, 5, 2, 4, 1, 4, 5, 3, 5, 1, 0, 0, 4, 7, 4,
      8, 2, 1, 6, 6, 3, 7, 0, 4, 8, 4, 4, 0, 3, 1, 9, 9, 8, 9, 0, 0, 0, 8, 8, 9,
      5, 2, 4, 3, 4, 5, 0, 6, 5, 8, 5, 4, 1, 2, 2, 7, 5, 8, 8, 6, 6, 6, 8, 8, 1,
      1, 6, 4, 2, 7, 1, 7, 1, 4, 7, 9, 9, 2, 4, 4, 4, 2, 9, 2, 8, 2, 3, 0, 8, 6,
      3, 4, 6, 5, 6, 7, 4, 8, 1, 3, 9, 1, 9, 1, 2, 3, 1, 6, 2, 8, 2, 4, 5, 8, 6,
      1, 7, 8, 6, 6, 4, 5, 8, 3, 5, 9, 1, 2, 4, 5, 6, 6, 5, 2, 9, 4, 7, 6, 5, 4,
      5, 6, 8, 2, 8, 4, 8, 9, 1, 2, 8, 8, 3, 1, 4, 2, 6, 0, 7, 6, 9, 0, 0, 4, 2,
      2, 4, 2, 1, 9, 0, 2, 2, 6, 7, 1, 0, 5, 5, 6, 2, 6, 3, 2, 1, 1, 1, 1, 1, 0,
      9, 3, 7, 0, 5, 4, 4, 2, 1, 7, 5, 0, 6, 9, 4, 1, 6, 5, 8, 9, 6, 0, 4, 0, 8,
      0, 7, 1, 9, 8, 4, 0, 3, 8, 5, 0, 9, 6, 2, 4, 5, 5, 4, 4, 4, 3, 6, 2, 9, 8,
      1, 2, 3, 0, 9, 8, 7, 8, 7, 9, 9, 2, 7, 2, 4, 4, 2, 8, 4, 9, 0, 9, 1, 8, 8,
      8, 4, 5, 8, 0, 1, 5, 6, 1, 6, 6, 0, 9, 7, 9, 1, 9, 1, 3, 3, 8, 7, 5, 4, 9,
      9, 2, 0, 0, 5, 2, 4, 0, 6, 3, 6, 8, 9, 9, 1, 2, 5, 6, 0, 7, 1, 7, 6, 0, 6,
      0, 5, 8, 8, 6, 1, 1, 6, 4, 6, 7, 1, 0, 9, 4, 0, 5, 0, 7, 7, 5, 4, 1, 0, 0,
      2, 2, 5, 6, 9, 8, 3, 1, 5, 5, 2, 0, 0, 0, 5, 5, 9, 3, 5, 7, 2, 9, 7, 2, 5,
      7, 1, 6, 3, 6, 2, 6, 9, 5, 6, 1, 8, 8, 2, 6, 7, 0, 4, 2, 8, 2, 5, 2, 4, 8,
      3, 6, 0, 0, 8, 2, 3, 2, 5, 7, 5, 3, 0, 4, 2, 0, 7, 5, 2, 9, 6, 3, 4, 5, 0
  };

  public static long largestProductOfThirteenAdjacentDigits() {
    long largestProduct = 0;
    for (int i = 0; i < thousandDigitNumber.length; i++) {
      ExerciseBaseClass.debugLevel(2, "========== Resetting total to 1 =========");
      long total = 1;
      for (int j = i; j < i + 13 && j < thousandDigitNumber.length && thousandDigitNumber[j] != 0; j++) {
        total *= thousandDigitNumber[j];
        ExerciseBaseClass.debugLevel(2, "Multiplying by " + thousandDigitNumber[j] + " (total = " + total + ")");
      }
      if (total > largestProduct) {
        largestProduct = total;
        ExerciseBaseClass.debugLevel(1, "NEW LARGEST PRODUCT: " + largestProduct);
      }
    }
    return largestProduct;
  }

  // This is a version using BigInteger, but it's not necessary because long is sufficient:
  public static BigInteger largestProductOfThirteenAdjacentDigits2() {
    BigInteger largestProduct = BigInteger.valueOf(0);
    for (int i = 0; i < thousandDigitNumber.length; i++) {
      ExerciseBaseClass.debugLevel(2, "========== Resetting total to 1 =========");
      BigInteger total = BigInteger.valueOf(1);
      for (int j = i; j < i + 13 && j < thousandDigitNumber.length && thousandDigitNumber[j] != 0; j++) {
        total = total.multiply(BigInteger.valueOf(thousandDigitNumber[j]));
        ExerciseBaseClass.debugLevel(2, "Multiplying by " + thousandDigitNumber[j] + " (total = " + total + ")");
      }
      if (total.compareTo(largestProduct) == 1) {
        largestProduct = total;
        ExerciseBaseClass.debugLevel(1, "NEW LARGEST PRODUCT: " + largestProduct);
      }
    }
    return largestProduct;
  }

  /*
   * 009. Special Pythagorean triplet
   *
   * A Pythagorean triplet is a set of three natural numbers, a < b < c, for which a2 + b2 = c2
   * For example, 3^2 + 4^2 = 9 + 16 = 25 = 5^2.
   *
   * There exists exactly one Pythagorean triplet for which a + b + c = 1000. Find the product abc.
   *
   * After grinding through some math I found the following equation relating a and b:
   * b = 1000 - 500000/(1000 - a)
   *
   * This implies that, for valid values of b, 500000 % (1000 - a) = 0.
   *
   * I used the above equations to derive a 2nd (presumably faster) version of the algorithm.
   */

  public static int pythagoreanTripletWithSumEquals1000BruteForce() {
    for (int a = 1; a < 333; a++) {
      for (int b = a + 1; b < 500; b++) {
        for (int c = b + 1; c < 500; c++) {
          if (a + b + c == 1000 && Math.pow(a, 2) + Math.pow(b, 2) == Math.pow(c, 2)) {
            ExerciseBaseClass.debugLevel(0, "Found answer: a = " + a + ", b = " + b + ", c = " + c);
            return a*b*c;
          }
        }
      }
    }
    return 0;
  }

  // I think this version is faster, but I need to measure it
  public static int pythagoreanTripletWithSumEquals1000() {
    for (int a = 1; a < 333; a++) {
      if (500000 % (1000 - a) == 0) {
        int b = 1000 - 500000/(1000 - a);
        int c = (int)Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        if (a + b + c == 1000) {
          ExerciseBaseClass.debugLevel(1, "Found answer: a = " + a + ", b = " + b + ", c = " + c);
          return a*b*c;
        }
      }
    }
    return 0;
  }

  /*
   * 010. The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17. Find the sum of all the primes below two million.
   *
   * Apparently 1 is not a prime number. Note that this algorithm takes over an hour to complete,
   * but I don't think there's any solution other than brute force.
   *
   * Actually, per https://projecteuler.net/overview=010, there is a much faster optimized algorithm!
   */
  // Need to skip all numbers divisible by 2 (start from 3 and go +=2) and memoize others?
  // It seems like the version that skips multiples of 2 takes even longer!
  public static long sumOfAllPrimesBelowTwoMillionBruteForce() {
    long primeTotal = 2;
    ExerciseBaseClass.debugLevel(0, "Start time: " + new java.util.Date());
    for (int i = 3; i < 2000000; i +=2) {  // Skip all even numbers
      if (numberIsPrime(i)) {
        primeTotal += i;
      }
      // This takes a long time to complete so give periodic updates and check for overflow
      if (i - 1 % 100000 == 0 || primeTotal < 0) {
        ExerciseBaseClass.debugLevel(1, "Prime total for i = " + i + ": " + primeTotal);
      }
    }
    ExerciseBaseClass.debugLevel(0, "End time: " + new java.util.Date());
    return primeTotal;
  }

  public static long sumOfAllPrimesBelowTwoMillion() {
    long primeTotal = 0;
    for (int i = 0; i < primeCache.size() && primeCache.get(i) < 2000000; i++) {
      primeTotal += primeCache.get(i);
    }
    return primeTotal;
  }

  /*
   * 011. Largest product in a grid
   *
   * In the 20×20 grid below, four numbers along a diagonal line have been marked in red.
   *
   * 08 02 22 97 38 15 00 40 00 75 04 05 07 78 52 12 50 77 91 08
   * 49 49 99 40 17 81 18 57 60 87 17 40 98 43 69 48 04 56 62 00
   * 81 49 31 73 55 79 14 29 93 71 40 67 53 88 30 03 49 13 36 65
   * 52 70 95 23 04 60 11 42 69 24 68 56 01 32 56 71 37 02 36 91
   * 22 31 16 71 51 67 63 89 41 92 36 54 22 40 40 28 66 33 13 80
   * 24 47 32 60 99 03 45 02 44 75 33 53 78 36 84 20 35 17 12 50
   * 32 98 81 28 64 23 67 10 26 38 40 67 59 54 70 66 18 38 64 70
   * 67 26 20 68 02 62 12 20 95 63 94 39 63 08 40 91 66 49 94 21
   * 24 55 58 05 66 73 99 26 97 17 78 78 96 83 14 88 34 89 63 72
   * 21 36 23 09 75 00 76 44 20 45 35 14 00 61 33 97 34 31 33 95
   * 78 17 53 28 22 75 31 67 15 94 03 80 04 62 16 14 09 53 56 92
   * 16 39 05 42 96 35 31 47 55 58 88 24 00 17 54 24 36 29 85 57
   * 86 56 00 48 35 71 89 07 05 44 44 37 44 60 21 58 51 54 17 58
   * 19 80 81 68 05 94 47 69 28 73 92 13 86 52 17 77 04 89 55 40
   * 04 52 08 83 97 35 99 16 07 97 57 32 16 26 26 79 33 27 98 66
   * 88 36 68 87 57 62 20 72 03 46 33 67 46 55 12 32 63 93 53 69
   * 04 42 16 73 38 25 39 11 24 94 72 18 08 46 29 32 40 62 76 36
   * 20 69 36 41 72 30 23 88 34 62 99 69 82 67 59 85 74 04 36 16
   * 20 73 35 29 78 31 90 01 74 31 49 71 48 86 81 16 23 57 05 54
   * 01 70 54 71 83 51 54 69 16 92 33 48 61 43 52 01 89 19 67 48
   *
   * The product of these numbers is 26 × 63 × 78 × 14 = 1788696.
   * What is the greatest product of four adjacent numbers in the
   * same direction (up, down, left, right, or diagonally) in the 20×20 grid?
   *
   * My original problem was that my algorithm was basically correct but I forget
   * to check the reverse diagonals.
   */
  private static final int[][] numberGrid = new int[][] {
      {  8,  2, 22, 97, 38, 15,  0, 40,  0, 75,  4,  5,  7, 78, 52, 12, 50, 77, 91,  8 },
      { 49, 49, 99, 40, 17, 81, 18, 57, 60, 87, 17, 40, 98, 43, 69, 48,  4, 56, 62,  0 },
      { 81, 49, 31, 73, 55, 79, 14, 29, 93, 71, 40, 67, 53, 88, 30,  3, 49, 13, 36, 65 },
      { 52, 70, 95, 23,  4, 60, 11, 42, 69, 24, 68, 56,  1, 32, 56, 71, 37,  2, 36, 91 },
      { 22, 31, 16, 71, 51, 67, 63, 89, 41, 92, 36, 54, 22, 40, 40, 28, 66, 33, 13, 80 },
      { 24, 47, 32, 60, 99,  3, 45,  2, 44, 75, 33, 53, 78, 36, 84, 20, 35, 17, 12, 50 },
      { 32, 98, 81, 28, 64, 23, 67, 10, 26, 38, 40, 67, 59, 54, 70, 66, 18, 38, 64, 70 },
      { 67, 26, 20, 68,  2, 62, 12, 20, 95, 63, 94, 39, 63,  8, 40, 91, 66, 49, 94, 21 },
      { 24, 55, 58,  5, 66, 73, 99, 26, 97, 17, 78, 78, 96, 83, 14, 88, 34, 89, 63, 72 },
      { 21, 36, 23,  9, 75,  0, 76, 44, 20, 45, 35, 14,  0, 61, 33, 97, 34, 31, 33, 95 },
      { 78, 17, 53, 28, 22, 75, 31, 67, 15, 94,  3, 80,  4, 62, 16, 14,  9, 53, 56, 92 },
      { 16, 39,  5, 42, 96, 35, 31, 47, 55, 58, 88, 24,  0, 17, 54, 24, 36, 29, 85, 57 },
      { 86, 56,  0, 48, 35, 71, 89,  7,  5, 44, 44, 37, 44, 60, 21, 58, 51, 54, 17, 58 },
      { 19, 80, 81, 68,  5, 94, 47, 69, 28, 73, 92, 13, 86, 52, 17, 77,  4, 89, 55, 40 },
      {  4, 52,  8, 83, 97, 35, 99, 16,  7, 97, 57, 32, 16, 26, 26, 79, 33, 27, 98, 66 },
      { 88, 36, 68, 87, 57, 62, 20, 72,  3, 46, 33, 67, 46, 55, 12, 32, 63, 93, 53, 69 },
      {  4, 42, 16, 73, 38, 25, 39, 11, 24, 94, 72, 18,  8, 46, 29, 32, 40, 62, 76, 36 },
      { 20, 69, 36, 41, 72, 30, 23, 88, 34, 62, 99, 69, 82, 67, 59, 85, 74,  4, 36, 16 },
      { 20, 73, 35, 29, 78, 31, 90,  1, 74, 31, 49, 71, 48, 86, 81, 16, 23, 57,  5, 54 },
      {  1, 70, 54, 71, 83, 51, 54, 69, 16, 92, 33, 48, 61, 43, 52,  1, 89, 19, 67, 48 }
  };

  public static long largestProductOfFourAdjacentNumbers() {
    final int PRODUCT_SIZE = 4;
    long largestProduct = 0;
    for (int i = 0; i < numberGrid.length; i++) {
      for (int j = 0; j < numberGrid[i].length; j++) {
        // Can skip the loop if numberGrid[i][j] == 0
        long horizontalProduct = numberGrid[i][j];
        long verticalProduct = numberGrid[i][j];
        long diagonalProductR = numberGrid[i][j];
        long diagonalProductL = numberGrid[i][j];
        for (int k = 1; k < PRODUCT_SIZE; k++) {
          if (j + PRODUCT_SIZE <= numberGrid[i].length) {
            horizontalProduct *= numberGrid[i][j + k];
          }
          if (i + PRODUCT_SIZE <= numberGrid.length) {
            verticalProduct *= numberGrid[i + k][j];
          }
          if (i + PRODUCT_SIZE <= numberGrid.length && j + PRODUCT_SIZE <= numberGrid[i].length) {
            diagonalProductR *= numberGrid[i + k][j + k];
          }
          if (i >= PRODUCT_SIZE - 1 && j + PRODUCT_SIZE <= numberGrid[i].length) {
            diagonalProductL *= numberGrid[i - k][j + k];
          }
        }
        for (long product : new long[] { horizontalProduct, verticalProduct, diagonalProductR, diagonalProductL }) {
          if (product > largestProduct) {
            largestProduct = product;
          }
        }
      }
    }
    return largestProduct;
  }

  /*
   * TO DO:
   * 1. Write a function for measuring performance
   * 2. Figure out StackOverflow failure point for recursiveFactorNumber()
   * 3. Refactor/rewrite largestProductOfFourAdjacentNumbers() (print out the source)?
   * 4. Look over the official answers for the previous Euler problems, especially:
   *    4. Numeric palindromes
   *    5. Multiple of 1-20
   *    9. Pythagorean triplet
   */
  public static void main(String[] args) {
    // See ProjectEulerTest for tests with assertions for correct answers
    System.out.println("Start time: " + getCurrentTimestamp());
    // Correct answer: 70600674 (upward diagonal starting at (i = 15, j = 3)
    System.out.println("011. Largest product of 4 adjacent numbers: " + largestProductOfFourAdjacentNumbers());
    // Correct answer: 142913828922 (note that this takes over an hour to complete)
    // ExerciseBaseClass.DEBUG_LEVEL = 1;
    System.out.println("010. Sum of all primes below two million: " + sumOfAllPrimesBelowTwoMillion());
    // Correct answer: 31875000 (this is the product of the triplet: a = 200, b = 375, c = 425)
    System.out.println("009. Pythagorean triplet with sum = 1000: " + pythagoreanTripletWithSumEquals1000());
    // Correct answer: 23514624000
    System.out.println("008. Largest product of 13 adjacent digits: " + largestProductOfThirteenAdjacentDigits());
    // Correct answer: 104743
    System.out.println("007. The 10001st prime number is " + find10001stPrimeNumber());
    // Correct answer: 25164150
    System.out.println("006. Difference between the square of sum and sum of squares for 100: "
        + squareOfSumMinusSumOfSquares(100));
    // Correct answer: 232792560
    System.out.println("005. Smallest evenly divisible multiple of 1-20: "
        + smallestEvenlyDivisibleMultipleOfOneThroughTwenty());
    // Correct answer: 906609
    System.out.println("004. Largest palindrome product of two 3-digit numbers: " + largestPalindromeProduct());
    // Correct answer: 6857
    System.out.println("003. Largest prime factor of " + PRIME_FACTOR_NUM + ": " + largestPrimeFactor());
    // Correct answer: 4613732
    System.out.println("002. Sum of all even Fibonacci numbers below " + MAX_FIB + ": " + evenFibonacciNumbers());
    // Correct answer: 233168
    System.out.println("001. Sum of all multiples of 3 and 5 below " + MAX_3_AND_5_FACTORS + ": "
        + multiplesOf3And5());
    System.out.println("End time: " + getCurrentTimestamp());
  }
}
