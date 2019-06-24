import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StdFileIO {

    /**
     * Reads audio samples from a file (in .wav or .au format) and returns
     * them as a double array with values between -1.0 and +1.0.
     * The audio file must be 16-bit with a sampling rate of 44,100.
     * It can be mono or stereo.
     *
     * @param  filename the name of the audio file
     * @return the array of samples
     */
    public static double[] read(String filename) {

        // make sure that AudioFormat is 16-bit, 44,100 Hz, little endian
        final AudioInputStream ais = getAudioInputStreamFromFile(filename);
        AudioFormat audioFormat = ais.getFormat();

        // require sampling rate = 44,100 Hz
        if (audioFormat.getSampleRate() != StdAudio.SAMPLE_RATE) {
            throw new IllegalArgumentException("StdAudio.read() currently supports only a sample rate of " + StdAudio.SAMPLE_RATE + " Hz\n"
                    + "audio format: " + audioFormat);
        }

        // require 16-bit audio
        if (audioFormat.getSampleSizeInBits() != StdAudio.BITS_PER_SAMPLE) {
            throw new IllegalArgumentException("StdAudio.read() currently supports only " + StdAudio.BITS_PER_SAMPLE + "-bit audio\n"
                    + "audio format: " + audioFormat);
        }

        // require little endian
        if (audioFormat.isBigEndian()) {
            throw new IllegalArgumentException("StdAudio.read() currently supports only audio stored using little endian\n"
                    + "audio format: " + audioFormat);
        }

        byte[] bytes = null;
        try {
            int bytesToRead = ais.available();
            bytes = new byte[bytesToRead];
            int bytesRead = ais.read(bytes);
            if (bytesToRead != bytesRead) {
                throw new IllegalStateException("read only " + bytesRead + " of " + bytesToRead + " bytes");
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("could not read '" + filename + "'", ioe);
        }

        int n = bytes.length;

        // little endian, mono
        if (audioFormat.getChannels() == StdAudio.MONO) {
            double[] data = new double[n/2];
            for (int i = 0; i < n/2; i++) {
                // little endian, mono
                data[i] = ((short) (((bytes[2*i+1] & 0xFF) << 8) | (bytes[2*i] & 0xFF))) / (StdAudio.MAX_16_BIT);
            }
            return data;
        }

        // little endian, stereo
        else if (audioFormat.getChannels() == StdAudio.STEREO) {
            double[] data = new double[n/4];
            for (int i = 0; i < n/4; i++) {
                double left  = ((short) (((bytes[4*i+1] & 0xFF) << 8) | (bytes[4*i] & 0xFF))) / (StdAudio.MAX_16_BIT);
                double right = ((short) (((bytes[4*i+3] & 0xFF) << 8) | (bytes[4*i + 2] & 0xFF))) / (StdAudio.MAX_16_BIT);
                data[i] = (left + right) / 2.0;
            }
            return data;
        }

        // TODO: handle big endian (or other formats)
        else throw new IllegalStateException("audio format is neither mono or stereo");
    }

    /**
     * Saves the double array as an audio file (using .wav or .au format).
     *
     * @param  filename the name of the audio file
     * @param  samples the array of samples
     * @throws IllegalArgumentException if unable to save {@code filename}
     * @throws IllegalArgumentException if {@code samples} is {@code null}
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     * @throws IllegalArgumentException if {@code filename} extension is not {@code .wav}
     *         or {@code .au}
     */
    public static void save(String filename, double[] samples) {
        if (filename == null) {
            throw new IllegalArgumentException("filenameis null");
        }
        if (samples == null) {
            throw new IllegalArgumentException("samples[] is null");
        }

        // assumes 16-bit samples with sample rate = 44,100 Hz
        // use 16-bit audio, mono, signed PCM, little Endian
        AudioFormat format = new AudioFormat(StdAudio.SAMPLE_RATE, 32, StdAudio.MONO, StdAudio.SIGNED, StdAudio.LITTLE_ENDIAN);
        byte[] data = new byte[2 * samples.length];
        for (int i = 0; i < samples.length; i++) {
            int temp = (short) (samples[i] * StdAudio.MAX_16_BIT);
            data[2*i] = (byte) temp;
            data[2*i + 1] = (byte) (temp >> 8);   // little endian
        }

        // now save the file
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, samples.length);
            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
            }
            else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
            }
            else {
                throw new IllegalArgumentException("file type for saving must be .wav or .au");
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("unable to save file '" + filename + "'", ioe);
        }
    }

    // get an AudioInputStream object from a file
    private static AudioInputStream getAudioInputStreamFromFile(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        try {
            // first try to read file from local file system
            File file = new File(filename);
            if (file.exists()) {
                return AudioSystem.getAudioInputStream(file);
            }

            // resource relative to .class file
            InputStream is1 = StdAudio.class.getResourceAsStream(filename);
            if (is1 != null) {
                return AudioSystem.getAudioInputStream(is1);
            }

            // resource relative to classloader root
            InputStream is2 = StdAudio.class.getClassLoader().getResourceAsStream(filename);
            if (is2 != null) {
                return AudioSystem.getAudioInputStream(is2);
            }

            // give up
            else {
                throw new IllegalArgumentException("could not read '" + filename + "'");
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("could not read '" + filename + "'", e);
        }
        catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("file of unsupported audio format: '" + filename + "'", e);
        }
    }

    /**
     * Plays an audio file (in .wav, .mid, or .au format) in a background thread.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if unable to play {@code filename}
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public static synchronized void play(final String filename) {
        new Thread(new Runnable() {
            public void run() {
                AudioInputStream ais = getAudioInputStreamFromFile(filename);
                stream(ais);
            }
        }).start();
    }

    // https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
    // play a wav or aif file
    // javax.sound.sampled.Clip fails for long clips (on some systems), perhaps because
    // JVM closes (see remedy in loop)
    private static void stream(AudioInputStream ais) {
        SourceDataLine line = null;
        int BUFFER_SIZE = 4096; // 4K buffer

        try {
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            byte[] samples = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
                line.write(samples, 0, count);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (line != null) {
                line.drain();
                line.close();
            }
        }
    }
}
