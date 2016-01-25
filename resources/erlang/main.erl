-module(main).
-export([start/0]).
start() ->
  register(main, self()),

  {JavaId, Rows, Cols, FertilityMap} = getFertilityMap(),
  AgentMap_ = createAgentMap(FertilityMap),
  AgentMap = setCivilizations(FertilityMap, AgentMap_),
  ColorMap_ = createColorMap(FertilityMap),
  io:format("Updating ColorMap!~n"),
  ColorMap = simulation:updateColorMap(AgentMap, ColorMap_, []),
  simulation:startSimulation(parameters:getParameter(turns), FertilityMap, AgentMap, 
    ColorMap, Rows, Cols, JavaId),
  io:format("End of simulation!~n", []).


getFertilityMap() ->
  receive
    [JavaId, Rows, Cols | FertilityMap] ->
      io:format("FertilityMap ~px~preceived, length(FertilityMap) = ~p~n", 
        [Rows, Cols, length(FertilityMap)]),
      {JavaId, Rows, Cols, FertilityMap}
  end.


createColorMap(FertilityMap) ->
  createColorMap(FertilityMap, []).

createColorMap([], ColorMap) ->
  UpdatedColorMap = lists:reverse(ColorMap),
  io:format("ColorMap created, length(ColorMap) = ~p~n", [length(ColorMap)]),
  UpdatedColorMap;

createColorMap(FertilityMap, ColorMap) ->
  [Fertility | T] = FertilityMap, 
  case Fertility of
    0 ->
      createColorMap(T, [{230, 255, 255} | ColorMap]);
    _ ->
      createColorMap(T, [{255, 255, 255} | ColorMap])
  end.  


createAgentMap(FertilityMap) ->
  AgentMap = [[] || _ <- lists:seq(1, length(FertilityMap))],
  io:format("AgentMap created, length(AgentMap) = ~p~n", [length(AgentMap)]),
  AgentMap.


setCivilizations(FertilityMap, AgentMap) ->
  UpdatedAgentMap = setCivilizations(FertilityMap, AgentMap, 1),
  io:format("All civilisations set!~n", []),
  UpdatedAgentMap.

setCivilizations(FertilityMap, AgentMap, I) -> 
  CivilsNr = parameters:getParameter(civilizations_nr),
  if 
    I > CivilsNr ->
      AgentMap;
    true ->
      setNextCivilisation(FertilityMap, AgentMap, I)
  end.


setNextCivilisation(FertilityMap, AgentMap, I) ->
  Index = setStartPosition(FertilityMap),
  io:format("Civilisation's~p StartPosition = ~p~n", [I, Index]),
    %ensure same index isn't drawn more than once
  % Color = {random:uniform(50)+200, random:uniform(50)+200, random:uniform(50)+200},
  Color = {random:uniform(50), random:uniform(50), random:uniform(50)},

  io:format("Civilisation~p's Color = ~p~n", [I, Color]),     
    %ensure same color isn't drawn more than once
  LifeTime = 0,
  AvailableSpace = map_utils:getAvailableSpace(FertilityMap, AgentMap, Index),

  MaxInitCivilState = parameters:getParameter(max_init_civil_state),
  if 
    AvailableSpace < MaxInitCivilState ->
      AgentsNumber = AvailableSpace;
    true ->
      AgentsNumber = MaxInitCivilState
  end,
  Agents = [{Color, LifeTime} || _ <- lists:seq(1, AgentsNumber)],
  % io:format("Created initial population of ~p agents of Color = ~p and LifeTime = ~p~n",
    % [length(Agents), Color, LifeTime]),
  UpdatedAgentMap = map_utils:setListElement(AgentMap, Agents, Index),

  setCivilizations(FertilityMap, UpdatedAgentMap, I + 1).


setStartPosition(FertilityMap) -> 
  Index = random:uniform(length(FertilityMap)),
  case lists:nth(Index, FertilityMap) of
    0 ->
      % io:format("A position on water's been drawn, trying again.~n", []),
      setStartPosition(FertilityMap);
    _ ->
      Index
  end.    