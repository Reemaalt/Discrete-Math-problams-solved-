import java.util.ArrayList;
import java.util.Random;
public class mathHW2 {

// Calculate the (GCD) using the Euclidean algorithm
   private static long gcd(long a, long b) {
    if (b == 0) {
        return a;
    }
    return gcd(b, a % b);
}
// Calculate the modular inverse of 'a' modulo 'm' if it exists
private static long modularInverse(long a, long m) {
    long m0 = m;
    long x0 = 0;
    long x1 = 1;

    while (a > 1) {
// q is the quotient of a divided by m
        long q = a / m;

        long temp = m;
        m = a % m;
        a = temp;

        temp = x0;
        x0 = x1 - q * x0;
        x1 = temp;
    }
    if (a != 1) {
        return -1;
    }

// Ensure the result is non-negative
    if (x1 < 0) {
        x1 += m0;
    }

    return x1;
}
//Q1
public static ArrayList<Long> solveLinearCongruence(long a, long b, long m) {
    ArrayList<Long> solutions = new ArrayList<>();
    
    long gcdAM = gcd(a, m);

// If the equation has no solutions, return an empty list
    if (b % gcdAM != 0) {
        return solutions;
    }

// Find the modular inverse of 'a' modulo 'm'
    long aInverse = modularInverse(a, m);

// Calculate the first solution
    long x0 = (b / gcdAM) * aInverse;

 // Add all solutions (there are gcd(a, m) solutions)
    for (long i = 0; i < gcdAM; i++) {
        solutions.add((x0 + i * (m / gcdAM)) % m);
    }

    return solutions;
}

//Q2 Chinese Remainder Theorem
    public static long crt(ArrayList<Long> n, ArrayList<Long> a) {
        if (n.size() != a.size()) {
            throw new IllegalArgumentException("Lists n and a must have the same size");
        }

        int k = n.size();
        long result = 0;
        long N = 1;

        for (long num : n) {
            N *= num;
        }

        for (int i = 0; i < k; i++) {
            long Ni = N / n.get(i);
            result += a.get(i) * Ni * modularInverse(Ni, n.get(i));
        }

        return result % N;
    }

    //Q3 Fermat primality test
    public static boolean fermatPrimalityTest(long n, int k) {
        if (n <= 1) {
            return false;
        }

        if (n <= 3) {
            return true;
        }

        Random rand = new Random();

        for (int i = 0; i < k; i++) {
            long a = 2 + rand.nextInt((int) (n - 2));
            if (fastModularExponentiation(a, n - 1, n) != 1) {
                return false;
            }
        }

        return true;
    }

// modular exponentiation for a^b % m  to make the Fermat primality test more efficient 
    private static long fastModularExponentiation(long a, long b, long m) {
        long result = 1;
        a %= m;

        while (b > 0) {
            if (b % 2 == 1) {
                result = (result * a) % m;
            }
            b /= 2;
            a = (a * a) % m;
        }

        return result;
    }

    public static long modularPow(long base, long exponent, long modulus) {
        if (modulus == 1) {
            return 0; // If modulus is 1, the result is always 0.
        }

        long result = 1;
        base = base % modulus;

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            exponent = exponent >> 1; // Equivalent to dividing exponent by 2
            base = (base * base) % modulus;
        }

        return (result + modulus) % modulus;
    }

//the run ((testing)) 
public static void main(String[] args) {
    long a1 = 3;
    long b = 6;
    long m = 9;
    long base = 5;
    long exponent = 3;
    long modulus = 7;


    ArrayList<Long> solutions = solveLinearCongruence( a1, b, m);
    ArrayList<Long> n = new ArrayList<>();
    ArrayList<Long> a = new ArrayList<>();
//Q1: finds all solutions to the congruence
    if (solutions.isEmpty()) {
    System.out.println("No solutions");
    } 
    else {
    System.out.println("Solutions: " + solutions);
    }
 // Q2: Solve a system of simultaneous congruences using CRT
        n.add(3L);
        n.add(4L);
        a.add(2L);
        a.add(3L);
        long crtResult = crt(n, a);
        System.out.println("CRT Result: " + crtResult);

//Q3: Test if a number is probably prime using Fermat primality test
        long numberToTest = 17;
        int k = 5; // Number of iterations
        boolean isPrime = fermatPrimalityTest(numberToTest, k);
        System.out.println(numberToTest + " is probably prime: " + isPrime);
    
//Q4:  computes: (baseexponent) mod modulus then ,find its remainder when divided by modulus, compare the two results.
    long calculatedResult = modularPow(base, exponent, modulus);
    long manualResult = (long) Math.pow(base, exponent) % modulus; // Calculate (base^exponent) manually.

    System.out.println("Using modular exponentiation: (" + base + "^" + exponent + ") % " + modulus + " = " + calculatedResult);
    System.out.println("Manually calculated result: (" + base + "^" + exponent + ") % " + modulus + " = " + manualResult);
// compare the two results
if (calculatedResult == manualResult) {
    System.out.println("Results match. The modular exponentiation is correct.");
} else {
    System.out.println("Results do not match. There might be an issue with the implementation.");
}  

}
}

