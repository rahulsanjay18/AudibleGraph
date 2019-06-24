# AudibleGraph

Question to be answered: Is it possible to be able to distinguish between graphs via audio instead of by visuals?

So far, the answer seems to be: Yes.

Methodology: Using [this](https://introcs.cs.princeton.edu/java/stdlib/StdAudio.java.html) standard library from princeton, I used discrete math stuff to try to model what a graph of some polynomial would sound like, and found noticeable differences. SOME modifications have been made to the original StdAudio file for the sake of seperating out classes to better reflect functionality.

The graphs had their independent variable axis be time (t), and their dependent variable axis be frequency (f). The graph can only be heard in the 1st quadrant, as with this model of audio, it cannot be done any other way. Also, it always starts at the "t=0" mark. Currently, it only supports 3 different functions, but the hope is to get all polynomials working on it. 

Take a look at the following audio samples:

## Constant audio:
Set to a constant f=440
<audio controls>
<source src="graph_samples/constant.wav" type="audio/wav">
</audio>

## Linear audio:
Set to the equation f=2t + 440
<audio controls>
<source src="graph_samples/linear.wav" type="audio/wav">
</audio>

## Quadratic audio:
Set to the equation f = t^2 + 440
<audio controls>
<source src="graph_samples/quadratic.wav" type="audio/wav">
</audio>

It seems to be clear that there is a discernable difference between constant, linear, and quadratic waves.

Some issues:

- There is a weird popping sound occurring between what I believe is each note.
- no support for polynomials of degrees higher than 2

These will be addressed (especially the second one).
