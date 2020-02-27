# RModel
A generator for an animation that renders reality in terms of perceptual phenomena and awareness

@@@@@@@@@@@@@@@
R MODEL NOTES
@@@@@@@@@@@@@@@

REALITY MODEL

A model of reality
a stream of sensory impressions, observed, filtered by circle-of-awareness (...curated and mapped by intellect...???????????)

A bubble, colored, gently trembling, humming.. grows from nothing, expanding and fading into reality
It grows for a bit, sits there pulsating for a bit, then shrinks away.

A number (5? 100?) of objects like this. Clumps of them. 
All different colors (sight=green, sound==yellow, ....) and sizes (intensity/visibility)...
Packed and/or scattered into the viewport, grouped by type more or less

Sortof a fast-motion flowerbed (in florida. no cold season)

---
There is a movable viewport onto this seething flowerbed too. 
The circle of awareness. 
It is fuzzy-edged, thus rendering the the fuzzy edge of awareness. 
We might sharpen it to illustrate concentration.

Phenomena within the circle are visible. 
Phenomena outside it are invisible. 
Consider the fuzzy edge too 

----

will the bubble squish around? It would be more natural.
get a bubble-packing algorithm
the bubbles will be circles. They will grow, shrink... maybe vibrate
They tend to bump and press on each other, so we have that kind of physics going on.

################################
HOW WE'RE GONNA DO IT, 05/31/2019 
################################
Verlet physics blobs
attracted to center of viewport
growing out of nothing, lingering, vibrating, possibly humming, then dwindling to nothing
  geometrically : a blob grows from a point, pressing against its neighbors if necessary, stops growing, maintains its size for a while, then shrinks to nothing
  graphically : some kind of fade-in. Then strobes and stuff.



################################
OPTION
THE DEPTHS OF A PHENOMENON
################################

If we render *concentrating your awareness upon a phenomenon*, we will look very closely at a single phenomenon. What will be revealed when we look closely like that? What will the graphics be like?

1) More phenomena within it? Aome kind of pattern of phenomena>
2) can we use forsythia in this?

################################
OPTION
TEXTUAL NARRATIVE STREAM PARALLEL TO THE GRAPHICS
################################

So you have this slow graceful froth of bubbles growing and shrinking. Different colors, rates, sizes, wiggles, tones, etc.

Along the bottom you could have a written narrative flowing by, explaining what we're seeing.

---Example 1

  GRAPHICS
      A yellow vibrating disk with a brassy tone grows into existence, hangs out for 5 seconds, then dwindles and fades till it's gone.

  ACCOMPANYING TEXTSTREAM NARRATIVE
      Standing by the road, a car approaches from the left, passes, and leaves to the right.

In this case the graphics describe the sound of that car (tho they could describe its visual impression too I suppose).

---Example 2

  GRAPHICS
     The circle of awareness suddenly moves to center an focus upon a particular green bubble
     
  ACCOMPANYING TEXTSTREAM NARRATIVE
     A naked woman suddenly appears 

-------------
IMPLEMENTATION
-------------

Our process would become

  1) create random event (at appropriate frequency, form, etc)
  2) goto 1
  
 



