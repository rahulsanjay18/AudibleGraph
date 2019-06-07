public class Note{
  public double toneFrequency;
  public double duration;

  public Note(double hz, double s){
    this.toneFrequency = hz;
    this.duration = s;
  }

  /**
   * Writes one sample (between -1.0 and +1.0) to standard audio.
   * If the sample is outside the range, it will be clipped.
   *
   * @param  a the sample to play
   * @throws IllegalArgumentException if the sample is {@code Double.NaN}
   */
  public static void playSingleNote(double a) {

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
