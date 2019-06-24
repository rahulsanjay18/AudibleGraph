public class NoteGenerator {


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
