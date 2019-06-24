import java.util.ArrayList;

class MusicSheet{
  private ArrayList<Note> sheet;

  MusicSheet(){
      sheet = new ArrayList<>();
  }

  MusicSheet(double[] freq, double[] durations){
	  this();

    if(freq.length != durations.length){
		  throw new IllegalArgumentException("IllegalArgumentException: freq array and durations array are not the same length");
	  }

	  for(int i = 0; i < freq.length; i++){
		  sheet.add(new Note(freq[i], durations[i]));
	  }

  }

  void addNote(double freq, double duration){
      addNote(new Note(freq, duration));
  }

  void addNote(Note n){
      sheet.add(n);
  }

  void play() {

	  for (Note n : sheet) {
		  n.play();
	  }
  }

  double[] getArray(){
      ArrayList<Double> tones = new ArrayList<>();

      for(Note n : sheet){
          for(double d : n.getTone()){
              tones.add(d);
          }
      }

      return tones.stream().mapToDouble(d -> d).toArray();
  }

}
