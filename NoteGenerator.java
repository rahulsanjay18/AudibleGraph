public class NoteGenerator {

    static double getSummandOfPower(int exponent){
      double bernoulliNum = getBernoulliNumber(exponent);



      return 0;
    }

    static double getBernoulliNumber(int n){
      double bernoulliNum = 0;
      for(int k = 0; k <= n; k++){
        double divisor = (double) 1 /(k + 1);

        double sum = 0;
        for(int v = 0; v <= k; v++){
          int negMultiplier = (v % 2 == 0) ? 1 : -1;

          int vPlus1 = v + 1;
          int raisedToPower = exp(vPlus1, n);

          double combination = getCombination(k, v);

          sum += negMultiplier * vPlus1 * combination;
        }

        bernoulliNum += sum * divisor;
      }

      return bernoulliNum;
    }

    static double getCombination(int top, int bottom){
      double numerator = factorial(top);
      double denominator = factorial(top - bottom) * factorial(bottom);

      return numerator / denominator;
    }

    static int factorial(int n){
      int total = 1;

      for(int i = 1; i <= n; i++){
        total *= i;
      }

      return total;
    }

    static int exp(int base, int exponent){
      int total = 1;

      for(int i = 0; i < exponent; i++){
        total *= base;
      }
      return total;
    }

    static double getNoteOfFunction(char c, double constant, int i){
        switch (c){
            case '1': return linearNote(constant);
            case '2': return quadraticNote(constant, i);
    }

        return 0;
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
