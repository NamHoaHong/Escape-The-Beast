# *Escape the Beast*

This is a typing game for those who wants to improve their typing skills. It makes practicing to type a fun experience. 
I want to develop this game because I am currently trying to improve my typing speed for better productivity, and I also
think that this is a fundamental skill for everybody. Thus, I believe that this game would entertain people while giving
them a chance to improve their typing skill.

**About the game:**
- Player starts at the middle of the screen, typing makes them move forward. Player also falls back as time moves.
- A beast chase them down from behind. Getting caught means death.
- Each run is 30 seconds. At the end of each, player is given a score depends on their performance and the statistics 
for that run.
- There will be a score panel where you can see the scores and sort them by date, highest and lowest.
- Previous runs will be saved so that the player can see how much they have improved.

- As a user, when I end a run, I want to be reminded to save my scores and have the option to select to do so or not.
- As a user, when I start the application, I want to be given the option to load my previous scores.

**Phase 4: Task 2**

Sample of the events that occur when program runs:

Wed Apr 10 01:18:21 PDT 2024

Score added to the score list

Wed Apr 10 01:19:01 PDT 2024

Score added to the score list

Wed Apr 10 01:19:42 PDT 2024

Score added to the score list

**Phase 4: Task 3**

If I had more time to work on the project, I might do the refactoring on the Word, WordList and WordTyper class. It
takes a lot of conversion between those 3 class when the program runs, as the WordList reads from the destination file
and converts it to string, while WordTyper only takes inputs as word. Therefore, a process of converting strings to word
is necessary during the game execution. If I could simplify that process, the code would be cleaner and easier to track
and update. Moreover, I would also want to improve on the game's visual, because it does not look very nice at this
moment.