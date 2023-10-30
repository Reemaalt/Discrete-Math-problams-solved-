import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MathHW2 {

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
            long q = a / m;
            long temp = m;
            m = a % m;
            a = temp;

            temp = x0;
            x0 = x1 - q * x0;
            x1 = temp;
        }
        if (a != 1) {
            return -1; // No modular inverse exists
        }

        if (x1 < 0) {
            x1 += m0;
        }

        return x1;
    }

    // Q1: Solve a linear congruence ax ≡ b (mod m)
    public static ArrayList<Long> solveLinearCongruence(long a, long b, long m) {
        ArrayList<Long> solutions = new ArrayList<>();
        long gcdAM = gcd(a, m);

        if (gcdAM == 0 || b % gcdAM != 0) {
            return solutions; // No solutions
        }

        long aInverse = modularInverse(a, m);

        if (aInverse == -1) {
            return solutions; // No modular inverse exists
        }

        long x0 = (b / gcdAM) * aInverse;

        for (long i = 0; i < gcdAM; i++) {
            long z = (x0 + i * (m / gcdAM)) % m;
            solutions.add(z);
        }

        return solutions;
    }

    // Q2: Chinese Remainder Theorem
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
            long inverseNi = modularInverse(Ni, n.get(i));
            if (inverseNi == -1) {
                throw new ArithmeticException("No modular inverse exists for one of the divisors");
            }
            result += a.get(i) * Ni * inverseNi;
        }

        return result % N;
    }

    // Q3: Fermat primality test
    public static boolean fermatPrimalityTest(long n, int k) {
        if (n <= 1) {
            return false;
        }

        if (n <= 3) {
            return true;
        }

        Random rand = new Random();

        for (int i = 0; i < k; i++) {
            long a = 1 + rand.nextInt((int) (n - 1));
            if (fastModularExponentiation(a, n - 1, n) != 1) {
                return false;
            }
        }

        return true;
    }

    // Modular exponentiation for a^b % m
    private static long fastModularExponentiation(long a, long b, long m) {
        long result = 1;
        a %= m;
        a = (a % m + m) % m;

        while (b > 0) {
            if (b % 2 == 1) {
                result = (result * a) % m;
            }
            b /= 2;
            a = (a * a) % m;
        }

        return result;
    }

    // Q4: Compute (base^exponent) mod modulus
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input values from the user
        System.out.print("Enter value for a: ");
        long a = scanner.nextLong();
        System.out.print("Enter value for b: ");
        long b = scanner.nextLong();
        System.out.print("Enter value for m: ");
        long m = scanner.nextLong();
        System.out.print("Enter value for base: ");
        long base = scanner.nextLong();
        System.out.print("Enter value for exponent: ");
        long exponent = scanner.nextLong();
        System.out.print("Enter value for modulus: ");
        long modulus = scanner.nextLong();
        System.out.print("Enter the number of iterations for Fermat primality test (k): ");
        int k = scanner.nextInt();

        scanner.close();

        // Q1: Solve a linear congruence
        ArrayList<Long> solutions = solveLinearCongruence(a, b, m);
        if (solutions.isEmpty()) {
            System.out.println("No solutions");
        } else {
            System.out.println("Solutions: " + solutions);
        }

        // Q2: Solve a system of simultaneous congruences using CRT
        ArrayList<Long> n = new ArrayList<>();
        ArrayList<Long> aCRT = new ArrayList<>();
        n.add(3L);
        n.add(4L);
        aCRT.add(2L);
        aCRT.add(3L);

        try {
            long crtResult = crt(n, aCRT);
            System.out.println("CRT Result: " + crtResult);
        } catch (ArithmeticException e) {
            System.out.println("CRT cannot be computed due to the lack of a modular inverse for one of the divisors.");
        }

        // Q3: Test if a number is probably prime using Fermat primality test
        long numberToTest = 17;
        boolean isPrime = fermatPrimalityTest(numberToTest, k);
        System.out.println(numberToTest + " is probably prime: " + isPrime);

        // Q4: Compute (base^exponent) mod modulus
        long calculatedResult = modularPow(base, exponent, modulus);
        long manualResult = (long) Math.pow(base, exponent) % modulus;

        System.out.println("Using modular exponentiation: (" + base + "^" + exponent + ") % " + modulus + " = " + calculatedResult);
        System.out.println("Manually calculated result: (" + base + "^" + exponent + ") % " + modulus + " = " + manualResult);

        if (calculatedResult == manualResult) {
            System.out.println("Results match. The modular exponentiation is correct.");
        } else {
            System.out.println("Results do not match. There might be an issue with the implementation.");
        }
    }
}
