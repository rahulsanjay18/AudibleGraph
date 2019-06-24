public class Note{
  private double[] tone;
  public static final double DEFAULT_FREQ = 440;
  public static final double DEFAULT_DURATION = .0625;

  public Note(){
      this(DEFAULT_FREQ, DEFAULT_DURATION);
  }

  public Note(double hz, double s){
    this.tone = createTone(hz, s);
  }

  // create a pure createTone of the given frequency for the given duration
  private double[] createTone(double hz, double duration) {
    int n = (int) (StdAudio.SAMPLE_RATE * duration);
    double[] a = new double[n+1];
    for (int i = 0; i <= n; i++) {
      a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
    }
    return a;
  }

  public void play(){
      for(double a : tone){
          StdAudio.play(a);
      }
  }

  public long getNoteLength(){
      return tone.length;
  }

  public double[] getTone(){
      return tone;
  }

}
