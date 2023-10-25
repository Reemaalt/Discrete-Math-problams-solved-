public class LinearCongruenceSolver {
    public static int [] solveLinearCongruence(int a, int b, int m) {
        // Calculate the greatest common divisor of a and m
        int gcd = gcd(a, m);

        // Check if a solution exists
        if (b % gcd != 0) {
            return -1; //  if No solution
        }

        // Reduce the congruence to the form ax ≡ b (mod m)
        a /= gcd;
        b /= gcd;
        m /= gcd;

        // Calculate the multiplicative modular inverse of a modulo m
        int inverse = modInverse(a, m);

        // Calculate the first solution
        int x0 = (b * inverse) % m;

        // Calculate other solutions
        int[] solutions = new int[gcd];
        for (int i = 0; i < gcd; i++) {
            solutions[i] = (x0 + i * m) % (m * gcd);
        }

        return solutions;
    }

    // Calculate the greatest common divisor using the Euclidean algorithm
    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    // Calculate the multiplicative modular inverse using the extended Euclidean algorithm
    private static int modInverse(int a, int m) {
        int m0 = m;
        int x0 = 0, x1 = 1;

        while (a > 1) {
            int q = a / m;
            int t = m;

            m = a % m;
            a = t;

            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }

        if (x1 < 0) {
            x1 += m0;
        }

        return x1;
    }

    public static void main(String[] args) {
        int a = 7;
        int b = 5;
        int m = 12;
        int[] solutions = solveLinearCongruence(a, b, m);

        if (solutions == -1) {
            System.out.println("No solution");
        } else {
            System.out.print("Solutions: ");
            for (int solution : solutions) {
                System.out.print(solution + " ");
            }
        }
    }
}