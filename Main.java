import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Stream;


public class Main {

	// create a pure tone of the given frequency for the given duration
	public static Double[] tone(double hz, double duration) { 
		int n = (int) (StdAudio.SAMPLE_RATE * duration);
		Double[] a = new Double[n+1];
		for (int i = 0; i <= n; i++) {
			a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
		}
		return a; 
	} 


	public static void main(String[] args) {

		// frequency
		double hz = 440;

		// number of seconds to play the note
		double duration = .0625;
		
		Double[] a= tone(hz, duration);
		
		ArrayList<Double> notes = new ArrayList<>();
		
		for (int i = 0; i < 100; i++){
			hz += 1;
			a = tone(hz, duration);
			Collections.addAll(notes, a);
		}
		
		double[] newNotes = Stream.of(notes.toArray(a)).mapToDouble(Double::doubleValue).toArray();
		//StdAudio.play(newNotes);
		StdAudio.save("graph_samples/const.wav", newNotes);
		
		
		for (int i = 0; i < 100; i++){
			hz += i;
			a = tone(hz, duration);
			Collections.addAll(notes, a);
		}
		
		newNotes = Stream.of(notes.toArray(a)).mapToDouble(Double::doubleValue).toArray();
		//StdAudio.play(newNotes);
		StdAudio.save("graph_samples/linear.wav", newNotes);
		
		for (int i = 0; i < 100; i++){
			hz += .5*i*i;
			a = tone(hz, duration);
			Collections.addAll(notes, a);
		}
		
		newNotes = Stream.of(notes.toArray(a)).mapToDouble(Double::doubleValue).toArray();
		//StdAudio.play(newNotes);
		StdAudio.save("graph_samples/quadratic.wav", newNotes);
		
		
		System.out.println("finished.");
		
	}
	
	public void calibrate(double hz, Double[] a, double duration){
		Scanner scanner = new Scanner(System.in);
		boolean cont = true;
		System.out.println("Beginning Calibration:");
		while (cont) {
			if(!scanner.next().equals("f")){
				cont = false;
			}else{
				duration /= 2;
				// create the array
				a = tone(hz, duration);
				
				System.out.println("Duration: " + Double.toString(duration));
				double[] newNotes = Stream.of(a).mapToDouble(Double::doubleValue).toArray();
				StdAudio.play(newNotes);
				System.out.println("Press Space if you heard it.");
			}
			
			
		}
		scanner.close();
	}

}
