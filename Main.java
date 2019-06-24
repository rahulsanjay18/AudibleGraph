

public class Main{

	public static void main(String[] args) {
		final double DEFAULT_MULTPLIER = 1;
		final double UPPER_BOUND = Note.DEFAULT_FREQ * Note.DEFAULT_FREQ;


		MusicSheet sheet = new MusicSheet();
		double f = Note.DEFAULT_FREQ;
		for(int i = 0; i < 100  && f <= UPPER_BOUND; i++){
			f += NoteGenerator.constant();
			sheet.addNote(f, Note.DEFAULT_DURATION);
		}

		StdFileIO.save("graph_samples/constant.wav", sheet.getArray());

		System.out.println("finished.");
		StdAudio.close();
	}

}
