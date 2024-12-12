package ru.KIRPI4.chinaparser;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class JsExecutor {
    private static final String ENGINE_NAME = "graal.js";
    private static final ScriptEngine ENGINE = new ScriptEngineManager().getEngineByName(ENGINE_NAME);

    public static <T> T execute(Reader reader, String function, Object... args) throws IOException, ScriptException, NoSuchMethodException {
        ENGINE.eval(reader);
        var invocable = (Invocable) ENGINE;

        return (T)invocable.invokeFunction(function, args);
    }

    public static <T> T execute(String path, String function, Object... args) throws IOException, ScriptException, NoSuchMethodException {
        return execute(Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8), function, args);
    }
}
