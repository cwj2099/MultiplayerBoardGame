# Justification for handling state
Below, describe where you stored each of the following states and justify your answers with design principles/goals/heuristics/patterns. Discuss the alternatives and trade-offs you considered during your design process.

## Players
Stored in Game State

## Current player
Stored in Game State

## Worker locations
Stored in worker instances as a vector2

## Towers
Stored in Game Space within a 2D array

## Winner
Technically stored in Game State, but Game Mode decide that

## Design goals/principles/heuristics considered
Decoupling and delegation is considered a lot. I seperate game state and game mode so we can have a seperate win condition, move rules etc when it comes to god cards. I also make building unit generation a factory.

## Alternatives considered and analysis of trade-offs
Building unit factory is actually not nessary for current game design since there is no limitation on components number. Removing it can make the current structure more clear, while harder to implement limited version. Also, if I coupled workers and building, I can remove the game space and let them communicate directly, but that will be a lot coupling when it comes to moving a worker so I did not do that