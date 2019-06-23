import java.util.ArrayList;

public class MusicSheet{
  public ArrayList<Note> sheet;

  public MusicSheet(){
      sheet = new ArrayList<>();
  }

  public MusicSheet(double[] freq, double[] durations){
	  if(freq.length != durations.length){
		  throw new IllegalArgumentException("IllegalArgumentException: freq array and durations array are not the same length");
	  }

	  sheet = new ArrayList<>();
	  for(int i = 0; i < freq.length; i++){
		  sheet.add(new Note(freq[i], durations[i]));
	  }

  }

  public void addNote(double freq, double duration){
      addNote(new Note(freq, duration));
  }

  public void addNote(Note n){
      sheet.add(n);
  }

  public void play() {

	  for (Note n : sheet) {
		  n.play();
	  }
  }

  public double[] getArray(){
      ArrayList<Double> tones = new ArrayList<>();

      for(Note n : sheet){
          for(double d : n.getTone()){
              tones.add(d);
          }
      }

      return tones.stream().mapToDouble(d -> d).toArray();
  }

}
