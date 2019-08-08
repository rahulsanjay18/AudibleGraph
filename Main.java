

public class Main{

	public static void main(String[] args) {
		final double DEFAULT_MULTPLIER = 1;
		final double UPPER_BOUND = Note.DEFAULT_FREQ * Note.DEFAULT_FREQ;
		final int EXPONENT = 3;

		MusicSheet sheet = new MusicSheet();
		double f = 0;
		for(int i = 0; i < 100  && f <= UPPER_BOUND; i++){
			f = NoteGenerator.getPolynomialNote(EXPONENT, i + 1) + Note.DEFAULT_FREQ;
			sheet.addNote(f, Note.DEFAULT_DURATION);
		}

		StdFileIO.save("graph_samples/third.wav", sheet.getArray());

		System.out.println("finished.");
		StdAudio.close();
	}

}
