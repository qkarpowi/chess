package server;

import com.google.gson.JsonObject;
import dataaccess.*;
import model.*;
import service.DatabaseService;
import service.GameService;
import service.UserService;
import spark.*;
import com.google.gson.Gson;
import exception.ResponseException;
import util.Result;

public class Server {
    private final UserService userService;
    private final DatabaseService databaseService;
    private final GameService gameService;

    public Server() {

        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();

        UserDAO userDAO;
        try {
            userDAO = new MySqlUserDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(authDAO, gameDAO);
        this.databaseService = new DatabaseService(authDAO, userDAO, gameDAO);
    }

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

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
    }

    private Object clearApplication(Request req, Response res) throws ResponseException {
        //Delete data
        var result = databaseService.clear();
        return handleResult(result, res);
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var result = userService.register(user);
        return handleResult(result, res);
    }

    private Object loginUser(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), LoginData.class);
        Result<AuthData> result = userService.login(user.username(), user.password());
        return handleResult(result, res);
    }

    private Object logoutUser(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");
        var result = userService.logout(authtoken);
        return handleResult(result, res);
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");

        var result = gameService.getGames(authtoken);
        return handleResult(result, res);
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");
        var gameData = new Gson().fromJson(req.body(), GameData.class);

        var result = gameService.createGame(authtoken, gameData.gameName());
        return handleResult(result, res);
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");
        var joinGame = new Gson().fromJson(req.body(), JoinGame.class);
        var result = gameService.joinGame(authtoken, joinGame.playerColor(), joinGame.gameID());
        return handleResult(result, res);
    }

    private static String handleResult(Result result, Response res) {
        if(result.isSuccess()){
            res.status(200);
            return new Gson().toJson(result.getData());
        } else {
            res.status(result.getStatuscode());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", String.format("Error: %s", result.getMessage()));
            return jsonObject.toString();
        }
    }
}
