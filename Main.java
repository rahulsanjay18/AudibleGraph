import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

	public static final double DEFAULT_FREQ = 440;
	public static final double DEFAULT_DURATION = .0625;

	public static void main(String[] args) {

		generateNotes("qudratic");
		System.out.println("finished.");

	}

	public void generateNotes(String filename){
		generateNotes(filename, DEFAULT_FREQ, DEFAULT_DURATION);
	}

	// could probably turn this into a struct
	public void generateNotes(String filename, double hz, double duration){

		Double[] generatedNotes = createNotesArray(Summands::quadraticSummand, hz, duration);
		double[] primitiveTypeNotes = convertDoubleObjToPrimitive(generatedNotes);

		StdAudio.save("graph_samples/" + filename + ".wav", primitiveTypeNotes);
	}

	public Object[] createNotesArray(Runnable toRun, double hz, double duration){
		Double[] a = tone(hz, duration);
		ArrayList<Double> notes = new ArrayList<>();

		for (int i = 1; i <= 100; i++){
			hz = hz + toRun.run();
			a = tone(hz, duration);
			Collections.addAll(notes, a);
		}

		return notes.toArray();
	}

	public double[] convertDoubleObjToPrimitive(Double[] arr){
		return Stream.of(arr).mapToDouble(Double::doubleValue).toArray();
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

	// create a pure tone of the given frequency for the given duration
	public static Double[] tone(double hz, double duration) {
		int n = (int) (StdAudio.SAMPLE_RATE * duration);
		Double[] a = new Double[n+1];
		for (int i = 0; i <= n; i++) {
			a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
		}
		return a;
	}
}
