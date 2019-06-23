

public class Main{

	public static void main(String[] args) {

		MusicSheet sheet = new MusicSheet();
		double f = Note.DEFAULT_FREQ;
		for(int i = 0; i < 100 ; i++){
			f += NoteGenerator.quadraticNote(i);
			sheet.addNote(f, Note.DEFAULT_DURATION);
		}

		StdAudio.save("graph_samples/quadratic.wav", sheet.getArray());

		System.out.println("finished.");

	}

}
