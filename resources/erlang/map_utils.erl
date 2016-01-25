-module(map_utils).
-export([hasAvailableSpace/3, hasAvailableSpace/2, getAvailableSpace/3,
 getAvailableSpace/2, setListElement/3, updateColorMap/2, elementsAt/2,
 removeListElement/2, removeListElements/2, convertIndex2RowCol/3, 
 convertRowCol2Index/3, findNeighbours/4, isOn2DMap/3, isOnMap/2, removeNotOnLand/4]).


hasAvailableSpace(FertilityMap, AgentMap, Index) ->
  getAvailableSpace(lists:nth(Index, FertilityMap), lists:nth(Index, AgentMap)).

hasAvailableSpace(Fertility, Agents) ->
  getAvailableSpace(Fertility, Agents) > 0.


getAvailableSpace(FertilityMap, AgentMap, Index) ->
  getAvailableSpace(lists:nth(Index, FertilityMap), lists:nth(Index, AgentMap)).

getAvailableSpace(Fertility, Agents) ->
  AgentsSizeLimit = parameters:getParameter(max_agents_cell_limit) div 
    parameters:getParameter(max_fertility) * Fertility,
  AgentsSizeLimit - length(Agents).


setListElement(List, Value, Index) ->
  lists:sublist(List, Index - 1) ++ [Value] ++ lists:nthtail(Index, List).


removeListElement(List, Index) ->
  lists:sublist(List, Index - 1) ++ lists:nthtail(Index, List).


removeListElements(List, Indexes) ->
  removeElements(List, lists:reverse(lists:sort(Indexes))).


removeElements(List, []) ->
  List;

removeElements(List, [LastIndex | T]) ->
  removeElements(removeListElement(List, LastIndex), T).     


updateColorMap(ColorMap, AgentMap) ->
  GetDominantColor = fun getDominantColor/2,
  lists:zipwith(GetDominantColor, ColorMap, AgentMap).
  % TODO: hues'brightness


getDominantColor(Color, []) ->
  Color;
getDominantColor(_, Agents) ->
  % TODO
  [{Color, _} | _] = Agents,
  Color.


convertIndex2RowCol(Index, _, Cols) ->
  R = Index div Cols + 1,
  C = Index - (R - 1) * Cols,
  {R, C}.


convertRowCol2Index({R, C}, _, Cols) ->
  (R - 1) * Cols + C.


findNeighbours(Index, Rows, Cols, FertilityMap) ->
  {R, C} = convertIndex2RowCol(Index, Rows, Cols),
  Candidates = [{R-1, C-1}, {R-1, C}, {R-1, C+1}, {R, C+1}, 
    {R+1, C+1}, {R+1, C}, {R+1, C-1}, {R, C-1}],  
  ValidCandidates = lists:filter(fun(RC) ->  isOn2DMap(RC, Rows, Cols) end, Candidates),
  removeNotOnLand(ValidCandidates, Rows, Cols, FertilityMap).


isOn2DMap({R, C}, Rows, Cols) ->
  (R >= 1) and (R =< Rows) and (C >= 1) and (C =< Cols);

isOn2DMap(Index, Rows, Cols) ->
  {R, C} = convertIndex2RowCol(Index, Rows, Cols),
  isOn2DMap({R, C}, Rows, Cols).


isOnMap(Index, Map) ->
  (Index >= 1) and (Index =< length(Map)).  


removeNotOnLand(RCCandidates, Rows, Cols, FertilityMap) ->
  IndexCandidates = lists:map(fun(RC) -> convertRowCol2Index(RC, Rows, Cols) end,
    RCCandidates),
  IndexCandidatesSorted = lists:sort(IndexCandidates),
  Fertilities = elementsAt(IndexCandidatesSorted, FertilityMap),
  CandidatesWithFertilities = lists:zip(IndexCandidatesSorted, Fertilities),
  lists:filter(fun({_, Fertility}) -> Fertility =/= 0 end, CandidatesWithFertilities).


elementsAt(SortedValidUniqueIndexes, List) ->
  elementsAt(SortedValidUniqueIndexes, List, 1, []).

elementsAt([], _, _, Elements) ->
  lists:reverse(Elements);

elementsAt(SortedValidUniqueIndexes, List, I, Elements) ->
  [NextIndex | IndexesT] = SortedValidUniqueIndexes,
  [Element | ListT] = List,
  if
    NextIndex =:= I -> 
      elementsAt(IndexesT, ListT, I + 1, [Element] ++ Elements);
    NextIndex =/= I -> 
      elementsAt(SortedValidUniqueIndexes, ListT, I + 1, Elements)
  end.



  










