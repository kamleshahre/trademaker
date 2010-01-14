-module(server).-compile(export_all).

start() -> register(server,spawn(fun loop/0)).
loop() ->
    receive
        {Sender,Data} ->
            Sender ! Data,
            loop();
        shutdown -> ok
    end.

