-module(simulation).
-export([startSimulation/7, killAgents/2, killAgent/3, collectAliveAgentsAndIncLifeTime/2,
 deathsAndBirths/2, births/2, breedAgents/2, makeLove/3, updateColorMap/3, migrations/4,
 elections/2]).


startSimulation(Turns, FertilityMap, AgentMap, ColorMap, Rows, Cols, JavaId) ->
  io:format("Sending data to Java!~n"),
  sendDataToJava(JavaId, ColorMap, AgentMap),
  nextTurn(JavaId, Turns, 1, FertilityMap, AgentMap, ColorMap, Rows, Cols, ok).


sendDataToJava(JavaId, ColorMap, AgentMap) ->
  AgentsCount = lists:map(fun(Agents) -> length(Agents) end, AgentMap),
  % JavaId ! lists:zip(ColorMap, AgentsCount).
  JavaId ! {ColorMap, AgentsCount}.


nextTurn(JavaId, Turns, I, FertilityMap, AgentMap, ColorMap, Rows, Cols) 
when I =/= Turns + 1 ->
  io:format("Simulation: turn ~p~n", [I]),
  AgentMap_ = deathsAndBirths(FertilityMap, AgentMap),
  NewAgentMap = migrations(FertilityMap, AgentMap_, Rows, Cols),
  NewColorMap = elections(NewAgentMap, ColorMap),
  io:format("Sending data to Java!~n"),
  sendDataToJava(JavaId, NewColorMap, NewAgentMap),
  nextTurn(JavaId, Turns, I + 1, FertilityMap, NewAgentMap, NewColorMap, Rows, Cols);

nextTurn(_, Turns, I, _, _, _, _, _) when I =:= Turns + 1 ->
  {stop}.  


deathsAndBirths(FertilityMap, AgentMap) ->      
  AgentMap_ = deaths(AgentMap),
  births(AgentMap_, FertilityMap).


deaths(AgentMap) ->
  PIDs = lists:map(fun(Agents) -> spawn(simulation, killAgents,
    [self(), Agents]) end, AgentMap),

  collectUpdatedAgents(PIDs, []).
  

collectUpdatedAgents([], UpdatedAgents) ->
   lists:reverse(UpdatedAgents);

collectUpdatedAgents([PIDsHead | PIDsTail], UpdatedAgents) ->
  receive
    {PIDsHead, Agents} ->
      collectUpdatedAgents(PIDsTail, [Agents] ++ UpdatedAgents)
  end.     


killAgents(DeathsPID, Agents) ->
  % Agents = [{{1,2,3}, 5}, {{1,2,3}, 4}, {{1,2,3}, 3}],
  DeathTime = parameters:getParameter(death_time) div parameters:getParameter(turn_time),
  PIDs = lists:map(fun(Agent) -> spawn(simulation, killAgent, 
    [self(), Agent, DeathTime]) end, Agents),

  AliveAgents = collectAliveAgentsAndIncLifeTime(PIDs, []),
  DeathsPID ! {self(), AliveAgents}.
  

collectAliveAgentsAndIncLifeTime([], AliveAgents) ->
  lists:reverse(AliveAgents);

collectAliveAgentsAndIncLifeTime([PIDsHead | PIDsTail], AliveAgents) ->
  receive
    {PIDsHead, {dead}} ->
      collectAliveAgentsAndIncLifeTime(PIDsTail, AliveAgents);
    {PIDsHead, {Color, LifeTime}} -> 
      collectAliveAgentsAndIncLifeTime(PIDsTail, [{Color, LifeTime + 1}] ++ AliveAgents)
  end.


killAgent(KillAgentsPID, Agent, DeathTime) ->
  {_, LifeTime} = Agent,
  if
    LifeTime >= DeathTime ->
      KillAgentsPID ! {self(), {dead}};
    true -> 
      KillAgentsPID ! {self(), Agent}
  end.


births(AgentMap, FertilityMap) ->
  % io:format("~p~n~p~n", [AgentMap, FertilityMap]),
  AgentsAndFertilityMap = lists:zip(AgentMap, FertilityMap),
  % io:format("~p~n", [AgentsAndFertilityMap]),
  PIDs = lists:map(fun(CellData) -> spawn(simulation, breedAgents,
    [self(), CellData]) end, AgentsAndFertilityMap),

  collectUpdatedAgents(PIDs, []).


breedAgents(BirthsPID, CellData) -> 
  {Agents, Fertility} = CellData,
  % io:format("Agents: ~p~nFertility: ~p~n", [Agents, Fertility]),  
  Newborns = makeLove({Agents, Fertility}, 
    map_utils:getAvailableSpace(Fertility, Agents), []),
  % io:format("Newborns: ~p~n", [Newborns]),
  BirthsPID ! {self(), Agents ++ Newborns}.


makeLove({[], _}, _, Newborns) ->
  lists:reverse(Newborns);

makeLove(_, 0, Newborns) ->
  lists:reverse(Newborns);

makeLove({[AgentsH | AgentsT], Fertility}, AvailableSpace, Newborns) ->
  {Color, LifeTime} = AgentsH,
  Chance = 100 - 20 * LifeTime,
  Draw = random:uniform(100),
  if 
    Draw < Chance ->
      makeLove({AgentsT, Fertility}, AvailableSpace - 1, [{Color, 0}] ++ Newborns);
    true ->
      makeLove({AgentsT, Fertility}, AvailableSpace, Newborns)
  end.


migrations(FertilityMap, AgentMap, Rows, Cols) -> 
  migrations(FertilityMap, AgentMap, Rows, Cols, 1).

migrations(_, AgentMap, _, _, CurrentCell) when CurrentCell == length(AgentMap) + 1 ->
  AgentMap;

migrations(FertilityMap, AgentMap, Rows, Cols, CurrentCell) ->
  % io:format("migrations(~p, ~p, ~p, ~p, ~p)~n", [FertilityMap, AgentMap, 
    % Rows, Cols, CurrentCell]),
  AgentsNumber = length(lists:nth(CurrentCell, AgentMap)),
  % io:format("AgentsNumber = ~p~n", [AgentsNumber]),
  if 
    AgentsNumber =:= 0 ->
      migrations(FertilityMap, AgentMap, Rows, Cols, CurrentCell + 1);
    true ->       
      MigrationCause = parameters:getParameter(migration_cause),
      AvailableSpace = map_utils:getAvailableSpace(FertilityMap, AgentMap, CurrentCell),
      EmigrantsNumber = if 
        AvailableSpace =< MigrationCause -> parameters:getParameter(migration_percent) 
          * AgentsNumber div 100;
        true ->
          0
        end,
      Neighbours = map_utils:findNeighbours(CurrentCell, Rows, Cols, FertilityMap),
      % io:format("Neighbours = ~p~n", [Neighbours]),
      AgentsAfterEmigration = migrate(FertilityMap, AgentMap, CurrentCell, 
        Neighbours, EmigrantsNumber), 
      TravellersNumber = parameters:getParameter(travel_percent) 
        * length(AgentsAfterEmigration) div 100,
      AgentsAfterTravels = migrate(FertilityMap, AgentsAfterEmigration, CurrentCell, 
        Neighbours, TravellersNumber),
      migrations(FertilityMap, AgentsAfterTravels, Rows, Cols, CurrentCell + 1)
  end.


migrate(_, AgentMap, _, Neighbours, _) when length(Neighbours) =:= 0->
  AgentMap;

migrate(FertilityMap, AgentMap, CurrentCell, Neighbours, MigrantsNumber_) ->
  % io:format("MigrantsNumber = ~p~n", [MigrantsNumber]),
  CurrentCellAgents = lists:nth(CurrentCell, AgentMap),
  % io:format("CurrentCellAgents = ~p~n", [CurrentCellAgents]),
  CurrentCellAgentsLength = length(CurrentCellAgents),
  MigrantsIndexes = lists:usort([random:uniform(CurrentCellAgentsLength)
    || _ <- lists:seq(1, MigrantsNumber_)]),
  MigrantsNumber = length(MigrantsIndexes),
  Migrants = map_utils:elementsAt(MigrantsIndexes, CurrentCellAgents),
  NeighboursLength = length(Neighbours),
  MigrantsDestinations = [element(1, lists:nth(random:uniform(NeighboursLength),
    Neighbours)) || _ <- lists:seq(1, MigrantsNumber)],
  % io:format("MigrantsAndTheirDestinations = ~p~n", [MigrantsAndTheirDestinations]),
  CurrentCellAgentsWithoutMigrants = 
    map_utils:removeListElements(CurrentCellAgents, MigrantsIndexes),
  UpdatedAgentMap = map_utils:setListElement(AgentMap, 
    CurrentCellAgentsWithoutMigrants, CurrentCell),  
  moveAgents(FertilityMap, UpdatedAgentMap, CurrentCell, 
    lists:zip(Migrants, MigrantsDestinations)).


moveAgents(_, AgentMap, _, []) ->
  AgentMap;

moveAgents(FertilityMap, AgentMap, CurrentCell, [{Migrant, Destination} | Tail]) ->
  HasAvailableSpace = map_utils:hasAvailableSpace(FertilityMap, AgentMap, Destination),
  CurrentCellAgents = lists:nth(CurrentCell, AgentMap),
  if
    HasAvailableSpace =:= false ->
      AgentMap_ = map_utils:setListElement(AgentMap, [Migrant] ++ CurrentCellAgents,
        CurrentCell),
      moveAgents(FertilityMap, AgentMap_, CurrentCell, Tail);
    true ->
      DestinationAgents = lists:nth(Destination, AgentMap),
      % Migrant = lists:nth(MigrantIndex, CurrentCellAgents),
      UpdatedDestinationAgents = [Migrant] ++ DestinationAgents,
      AgentMap_ = map_utils:setListElement(AgentMap, UpdatedDestinationAgents,
        Destination),
      moveAgents(FertilityMap, AgentMap_, CurrentCell, Tail)
  end.

  
elections(AgentMap, ColorMap) ->
  updateColorMap(AgentMap, ColorMap, []).

updateColorMap([], [], ColorMap) ->
  lists:reverse(ColorMap); 

updateColorMap([Agents | ATail], [CurrentColor | CTail], NewColorMap) -> 
  ColorsCountHash = countColorOccurrences(Agents),
  % io:format("ColorsCountHash = ~p~n", [ColorsCountHash]),
  DominantColor = selectDominantColor(ColorsCountHash, CurrentColor),
  % io:format("DominantColor = ~p~n", [DominantColor]),
  ColorMap = [DominantColor] ++ NewColorMap,
  updateColorMap(ATail, CTail, ColorMap).


countColorOccurrences(Agents) ->
  countColorOccurrences(Agents, maps:new()).    

countColorOccurrences([], Hash) ->
  Hash;

countColorOccurrences([{Color, _}| Tail], Hash) ->
  UpdatedHash = incColorOccurrence(Color, Hash),
  countColorOccurrences(Tail, UpdatedHash).
  

incColorOccurrence(Color, Hash) ->
  Result = maps:find(Color, Hash),
  case Result of
    error -> maps:put(Color, 1, Hash);
    {ok, Occurrences} -> maps:update(Color, Occurrences+1, Hash)
  end.


selectDominantColor(Hash, CurrentColor) ->
  List = maps:to_list(Hash),
  selectDominantColor(List, CurrentColor, 0).

selectDominantColor([], DominantColor, _) ->
  DominantColor;

selectDominantColor([{Color, Occurrences} | Tail], DominantColor, MaxOccurrences) ->
  if
    Occurrences > MaxOccurrences ->
      selectDominantColor(Tail, Color, Occurrences);
    Occurrences =< MaxOccurrences ->
      selectDominantColor(Tail, DominantColor, MaxOccurrences)
  end.


nextTurn(JavaId, Turns, I, FertilityMap, AgentMap, ColorMap, Rows, Cols, ok) 
when I =/= Turns + 1 ->
  io:format("Simulation: turn ~p~n", [I]),
  io:format("Sending data to Java!~n"),
  sendDataToJava(JavaId, ColorMap, AgentMap),
  nextTurn(JavaId, Turns, I + 1, FertilityMap, AgentMap, ColorMap, Rows, Cols, ok);

nextTurn(_, Turns, I, _, _, _, _, _, ok) when I =:= Turns + 1 ->
  {stop}.  