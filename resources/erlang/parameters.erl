-module(parameters).
-export([getParameter/1]).


getParameter(turns) -> 300;
getParameter(turn_time) -> 10;
getParameter(civilizations_nr) -> 5;
getParameter(max_init_civil_state) -> 10;
getParameter(max_agents_cell_limit) -> 70;
getParameter(max_fertility) -> 7;
getParameter(death_time) -> 50;
getParameter(migration_cause) -> 5;
% getParameter(migration_cause) -> 8;
getParameter(migration_percent) -> 20;
% getParameter(migration_percent) -> 50;
getParameter(travel_percent) -> 10.
% getParameter(travel_percent) -> 20.