package com.example.demo.cassandraCommands;

import java.util.Map;

public interface Command {

	Object execute(Map<String,Object> body);
}
