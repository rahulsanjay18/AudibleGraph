import java.util.ArrayList;

public class NoteGenerator {

    static double[] bernoulliNumbers = {1, .5, 1.0/6.0, 0, -1.0/30.0, 0, 1.0/42, 5.0/66};


    static double getPolynomialNote(int p, int n){
        int sumOfExp = getSumOfExp(p, n);

        double sumWithBernoulli = getSumWithBernoulli(p, n);

        double division = Math.pow(n, p+1) / (p + 1);

        return 2 * (sumOfExp - (division + sumWithBernoulli));
    }

    static int getSumOfExp(int p, int n){
        int sum = 0;

        for(int i = 1; i <= n; i++){
            sum += Math.pow(i, p);
        }

        return sum;
    }

    static double getSumWithBernoulli(int p, int n){
        double sum = 0;
        int factP = getFactorial(p);
        for(int k = 2; k <= p; k++){
            double bernoulliDivFactorial = bernoulliNumbers[k] / getFactorial(k);
            double fallingFact = ((double) factP) / getFactorial(p - k + 1);
            double power = Math.pow(n, p - k + 1);

            sum += bernoulliDivFactorial * fallingFact * power;
        }

        return sum;
    }

    static int getFactorial(int n){
        int product = 1;

        for(int i = 2; i <= n; i++){
            product *= i;
        }

        return product;
    }

    static double getNoteOfFunction(char c, int i){
        switch (c){
            case '1': return linearNote();
            case '2': return quadraticNote(i);
        }

        return 0;
    }

    static double quadraticNote(double constant, int i){
        return constant*(2*i - 1);
    }

    static double quadraticNote(int i){
        return quadraticNote(1, i);
    }

    static double linearNote(double constant){
        return constant;
    }

    public static double linearNote(){
        return linearNote(1);
    }

    public static double constant(){
      return 0;
    }

}
