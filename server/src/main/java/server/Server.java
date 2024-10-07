package server;

import spark.*;
import com.google.gson.Gson;
import exception.ResponseException;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApplication);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    private Object clearApplication(Request req, Response res) throws ResponseException {
        //TODO Delete data
        res.status(200);
        return "";
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        //TODO register user
        res.status(200);
        return "";
    }
    private Object loginUser(Request req, Response res) throws ResponseException {
        //TODO login user
        res.status(200);
        return "";
    }
    private Object logoutUser(Request req, Response res) throws ResponseException {
        //TODO logout user
        res.status(200);
        return "";
    }
    private Object listGames(Request req, Response res) throws ResponseException {
        //TODO list games
        res.status(200);
        return "";
    }
    private Object createGame(Request req, Response res) throws ResponseException {
        //TODO create game
        res.status(200);
        return "";
    }
    private Object joinGame(Request req, Response res) throws ResponseException {
        //TODO join game
        res.status(200);
        return "";
    }

}
