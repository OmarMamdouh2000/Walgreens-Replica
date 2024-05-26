package com.example.Commands;

import java.util.Map;

public interface Command {

    Object execute(Map<String,Object> data) throws Exception;

}
