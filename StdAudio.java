
import javax.sound.sampled.AudioFormat;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


/**
 *  <i>Standard audio</i>. This class provides a basic capability for
 *  creating, reading, and saving audio.
 *  <p>
 *  The audio format uses a sampling rate of 44,100 Hz, 16-bit, monaural.
 *
 *  <p>
 *  For additional documentation, see <a href="https://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class StdAudio {

    /**
     *  The sample rate: 44,100 Hz for CD quality audio.
     */
    static final int SAMPLE_RATE = 44100;

    private static final int BYTES_PER_SAMPLE = 2;                // 16-bit audio
    static final int BITS_PER_SAMPLE = 16;                // 16-bit audio
    static final double MAX_16_BIT = Short.MAX_VALUE;     // 32,767
    private static final int SAMPLE_BUFFER_SIZE = 4096;

    static final int MONO   = 1;
    static final int STEREO = 2;
    static final boolean LITTLE_ENDIAN = false;
    static final boolean SIGNED        = true;


    private static SourceDataLine line;   // to play the sound
    private static byte[] buffer;         // our internal buffer
    private static int bufferSize = 0;    // number of samples currently in internal buffer

    private StdAudio() {
        // can not instantiate
    }

    // static initializer
    static {
        init();
    }

    // open up an audio stream
    private static void init() {
        try {
            // 44,100 Hz, 16-bit audio, mono, signed PCM, little endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, MONO, SIGNED, LITTLE_ENDIAN);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);

            // the internal buffer is a fraction of the actual buffer size, this choice is arbitrary
            // it gets divided because we can't expect the buffered data to line up exactly with when
            // the sound card decides to push out its samples.
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3];
        }
        catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

        // no sound gets made before this call
        line.start();
    }

    /**
     * Closes standard audio.
     */
    public static void close() {
        line.drain();
        line.stop();
    }

    /**
     * Writes one sample (between -1.0 and +1.0) to standard audio.
     * If the sample is outside the range, it will be clipped.
     *
     * @param  a the sample to play
     * @throws IllegalArgumentException if the sample is {@code Double.NaN}
     */
    static void play(double a) {
        if (Double.isNaN(a)) throw new IllegalArgumentException("sample is NaN");

        // clip if outside [-1, +1]
        if (a < -1.0) a = -1.0;
        if (a > +1.0) a = +1.0;

        // convert to bytes
        short s = (short) (MAX_16_BIT * a);
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8);   // little endian

        // send to sound card if buffer is full
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }



}
