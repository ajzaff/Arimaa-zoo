package com.alanjz.arimaa.engine;

import com.alanjz.arimaa.core.Team;
import com.alanjz.arimaa.core.moves.Step;

import java.util.*;

import static java.lang.System.out;
import static com.alanjz.arimaa.engine.AEIEngine.LogLevel.*;

public abstract class AEIEngine implements Runnable {
    private final String engineName;
    private final String engineAuthor;
    private final String engineVersion;
    private final Scanner sc;
    private final Thread th;
    private final Set<EngineCommand> engineCommands;
    private boolean running = true;

    public enum State {
        OPENING_PHASE,
        AEI_OK
    }

    private State state;

    public AEIEngine(String engineName, String engineAuthor, String engineVersion) {
        this.engineName = engineName;
        this.engineAuthor = engineAuthor;
        this.engineVersion = engineVersion;
        this.sc = new Scanner(System.in);
        this.th = new Thread(this);
        this.engineCommands = new HashSet<EngineCommand>();
        setState(State.OPENING_PHASE);

        // Add standard AEI commands.
        addEngineCommand(new EngineCommand("aei", 0, (params) -> {
            if(getState() == State.AEI_OK) {
                log(ERROR, "Command `aei' received after opening phase (after `aeiok').");
                return;
            }
            setState(State.AEI_OK);
            out.println("protocol-version 1");
            out.println("id name " + getEngineName());
            out.println("id author " + getEngineAuthor());
            out.println("id version " + getEngineVersion());
            out.println("aeiok");
        }));
        addEngineCommand(new EngineCommand("isready", 0, (params) -> {
            out.println("readyok");
        }));
        addEngineCommand(new EngineCommand("newgame", 0, (params) -> {
            if(getState() == State.OPENING_PHASE) {
                log(ERROR, "Command `newgame' received in opening phase (before `aei').");
                return;
            }
            newGame();
        }));
        addEngineCommand(new EngineCommand("setposition", 2, (params) -> {
            if(getState() == State.OPENING_PHASE) {
                log(ERROR, "Command `setposition' received in opening phase (before `aei').");
                return;
            }
            Team activeTeam =
                    (params[1].equals("g")? Team.GOLD :
                            params[1].equals("s")? Team.SILVER : null);
            if(activeTeam != null) {
                boolean wellFormed =
                        params[2].matches("\\[[ EMHDCRemhdcr]{64}\\]");
                if(wellFormed) {
                    String allPieces = params[2].substring(1, params[2].length()-2);
                    setPosition(activeTeam, allPieces);
                }
                else {
                    log(ERROR, "Call to `setposition' is not well-formed: invalid board string.");
                }
            }
            else {
                log(ERROR, "Call to `setposition' is not well-formed: unrecognized active team `" + params[1] + "'.");
            }
        }));
        addEngineCommand(new EngineCommand("newgame", 0, (params) -> {
            if(getState() == State.OPENING_PHASE) {
                log(ERROR, "Command `newgame' received in opening phase (before `aei').");
                return;
            }
            newGame();
        }));
        addEngineCommand(new EngineCommand("makemove", 1, 16, (params) -> {
            if(getState() == State.OPENING_PHASE) {
                log(ERROR, "Command `makemove' received in opening phase (before `aei').");
                return;
            }
            // makemove <move> ...
            // i.e. each step separated by a space.
            // move could be "resigns" or "takeback".
            if(params[1].equals("resigns")) {
                resign();
            }
            else if(params[1].equals("takeback")) {
                takeBackMove();
            }
            else {
                Step[] steps = new Step[params.length-1];
                for(int i=1; i < params.length; i++) {
                    Step step;
                    try {
                        step = Step.fromString(params[i], getActiveTeam());
                    }
                    catch (IllegalArgumentException e) {
                        log(ERROR, "In call to `makemove': " + e.getMessage());
                        return;
                    }
                    steps[i-1] = step;
                }
                makeMove(steps);
            }
        }));
        addEngineCommand(new EngineCommand("go", 0, 1, (params) -> {
            if(getState() == State.OPENING_PHASE) {
                log(ERROR, "Command `go' received in opening phase (before `aei').");
                return;
            }
            // go [ponder]
        }));
        addEngineCommand(new EngineCommand("stop", 0, (params) ->  {
            if(getState() == State.OPENING_PHASE) {
                log(ERROR, "Command `stop' received in opening phase (before `aei').");
                return;
            }
            // stop
            stop();
        }));
        addEngineCommand(new EngineCommand("quit", 0, (params) -> {
            if(getState() == State.OPENING_PHASE) {
                log(ERROR, "Command `quit' received in opening phase (before `aei').");
                return;
            }
            // quit
            quit();
        }));

        th.start();
    }

    public abstract void stop();
    public abstract void newGame();
    public abstract void setPosition(Team activeTeam, String allPieces);
    public abstract void makeMove(Step[] steps);
    public abstract void resign();
    public abstract void takeBackMove();
    public abstract Team getActiveTeam();

    protected final void addEngineCommand(EngineCommand e) {
        this.engineCommands.add(e);
    }

    public final void quit() {
        this.running = false;
    }

    public final void quit(int i) {
        log(WARNING, "Quitting with message code " + i + ".");
        this.running = false;
    }

    public final String getEngineName() {
        return engineName;
    }

    public final String getEngineAuthor() {
        return engineAuthor;
    }

    public final String getEngineVersion() {
        return engineVersion;
    }

    public Iterator<EngineCommand> getEngineCommands() {
        return engineCommands.iterator();
    }

    public final boolean isRunning() {
        return running;
    }

    public enum LogLevel {
        LOG(""),
        ERROR("Error:"),
        WARNING("Warning:"),
        DEBUG("Debug:");

        private String start;

        LogLevel(String start) {
            this.start = start;
        }

        public String getStart() {
            return start;
        }

        @Override
        public String toString() {
            return getStart();
        }
    }

    public static void log(LogLevel logLevel, String message) {
        out.println("log " + logLevel.toString() + message);
    }

    public static void log(String message) {
        log(LOG, message);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public final void processCommand(String command) {
        if(command.trim().isEmpty()) return;
        Iterator<EngineCommand> iterator = getEngineCommands();
        String[] tokens = command.split(" ");
        while(iterator.hasNext()) {
            EngineCommand engineCommand = iterator.next();
            if(engineCommand.processCommand(tokens)) {
                return;
            }
        }
        log(ERROR, "Unsupported engine command `" + tokens[0] + "'.");
        quit(1);
    }

    @Override
    public final void run() {
        while(isRunning()) {
            String command = sc.nextLine();
            processCommand(command);
        }
    }
}
