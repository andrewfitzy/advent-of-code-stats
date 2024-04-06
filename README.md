# advent-of-code-stats
Package for scraping and processing AOC leaderboards into CSV files

This project will download the leaderboards from Advent of Code for a given year. Once downloaded,
the files will be processed performing the following operations in the given order
1) Extract the completion time for task 1. This is held in a map of user_id to time. 
1) Extract the completion time for task 2. This is held in a map of user_id to time. The completion time for task 2 is
   interesting because it's the time for completing the whole day not just the individual task.
1) Find the intersection of the two maps
1) For each item in the intersection, calculate how long task 2 took (total-task_01)
1) Build the values in to a CSV format

Each day there are 100 entries on the leaderboard for each task but someone that appears on the board for task 1 may not
appear on the board for task 2. The only way to know the individual completion time for task 2 is if a coder appears on
both leaderboards. This means that in the results, there are a variable number of entries, one day there may be 80
developers with a time on both boards and another day there may only be 50.

## Running the application

## Output format