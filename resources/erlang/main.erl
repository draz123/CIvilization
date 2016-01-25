-module(erl_proc).
-export([start/0, getMap/0, printL/1]).
    
getMap() ->
  receive
    [PingId | Lista] ->
      io:format("Odebrana lista ma: ~p elementow~n", [length(Lista)]),
      DoWyslania = [{random:uniform(255), random:uniform(255), random:uniform(255)} || _ <- lists:seq(1, length(Lista))],
      PingId ! DoWyslania,
      PingId ! stop,
      io:format("getMap sent data and finished...~n", []);
    [] ->
      io:format("otrzymano pusta liste", []);
    stop ->
      io:format("getMap finished...~n", [])
  end.
 
start() ->
    register(getMap, spawn(erl_proc, getMap, [])).
    %spawn(Module,Name,[Args_list]) -> pid()
    %register(Name, PID)
    
printL([H|T]) ->
  %io:format("rozmiar = ~p~n", [length(T)]),
  io:format("~s~n", [H]),
  printL(T);
printL([]) -> 
  io:format("This is the end...~n", []).