package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
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
        var userDAO = new MemoryUserDAO();

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
        //Delete data
        var result = databaseService.clear();
        if(result.isSuccess()){
            res.status(200);
            return "";
        } else {
            res.status(result.getStatuscode());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", String.format("Error: %s", result.getMessage()));
            return jsonObject.toString();
        }
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var result = userService.register(user);

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

    private Object loginUser(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), LoginData.class);
        Result<AuthData> result = userService.login(user.username(), user.password());

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

    private Object logoutUser(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");
        var result = userService.logout(authtoken);
        if(result.isSuccess()){
            res.status(200);
            return "";
        } else{
            res.status(result.getStatuscode());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", String.format("Error: %s", result.getMessage()));
            return jsonObject.toString();
        }
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");

        var result = gameService.getGames(authtoken);
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

    private Object createGame(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");
        var gameData = new Gson().fromJson(req.body(), GameData.class);

        var result = gameService.createGame(authtoken, gameData.gameName());
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
    private Object joinGame(Request req, Response res) throws ResponseException {
        var authtoken = req.headers("authorization");
        var joinGame = new Gson().fromJson(req.body(), JoinGame.class);

        var result = gameService.joinGame(authtoken, joinGame.playerColor(), joinGame.gameID());
        if(result.isSuccess()){
            res.status(200);
            return "";
        } else {
            res.status(result.getStatuscode());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", String.format("Error: %s", result.getMessage()));
            return jsonObject.toString();
        }
    }
}
