-module(test).
-export([start/0, testDeathsAndBirths/0, testMigrations/0, testElections/0]).
        

start() ->
  Result = foo([1, 2, 3], [-1, -2, -3]),
  io:format("Result = ~p~n", [Result]).


foo(List1, List2) ->
    AddTwo = fun addTwo/2,
    lists:zipwith(AddTwo, List1, List2).


addTwo(A, B) ->
  A + B.


testDeathsAndBirths() ->
  FertilityMap = [0, 1,
                  1, 0],
  AgentMap = [[], [{{2,2,3}, 5}, {{2,2,3}, 2}, {{2,2,3}, 0}],
              [{{3,2,3}, 5}, {{3,2,3}, 2}, {{3,2,3}, 0}], []],

  simulation:deathsAndBirths(FertilityMap, AgentMap).

testMigrations() ->
  FertilityMap = [0, 1,
                  1, 0],
  AgentMap = [[], [{{2,2,3},3},{{2,2,3},1},{{2,2,3},0}],
              [{{3,2,3},3},{{3,2,3},1},{{3,2,3},0}], []],
  Rows = 2,
  Cols = 2, 

  simulation:migrations(FertilityMap, AgentMap, Rows, Cols).

testElections() ->
  AgentMap = [[], [{{3,2,3},0},{{3,2,3},1},{{2,2,3},3},{{2,2,3},1}],
              [{{2,2,3},0},{{3,2,3},3}], []],

  ColorMap = [{230, 255, 255}, {2,2,3},
              {3,2,3}, {230, 255, 255}],
              
  simulation:elections(AgentMap, ColorMap).                        



