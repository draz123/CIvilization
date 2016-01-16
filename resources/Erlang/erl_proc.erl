-module(erl_proc).
-export([start/0,pong/0,printL/1]).
    
pong() ->
    receive
        stop ->
            io:format("Pong finished...~n",[]);
		[] ->
			io:format("otrzymano pusta liste", []);
        [PingId|Lista] ->
			%PingId = hd(Lista),
			%Len = length(Lista),
            %io:format("Chuj~n",[]),
            io:format("Odebrana lista ma: ~p elementow~n", [length(Lista)]),
			%printL(Lista),
			Koniec = [1,2,3,4],
			PingId ! Koniec,  %wyœlij "koniec" do procesu o identyfikatorze PingId (odebrany na gorze od procesu z Javy)
            pong()
    end.
 
start() ->
        register(pong,spawn(hello_world,pong,[])).
		
printL([H|T]) ->
	%io:format("rozmiar = ~p~n", [length(T)]),
	io:format("~s~n", [H]),
	printL(T);
printL([]) -> 
	io:format("This is the end...~n", []).