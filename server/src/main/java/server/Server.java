package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.AuthData;
import model.GameData;
import model.LoginData;
import model.UserData;
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

    public Server(UserService userService, DatabaseService databaseService, GameService gameService) {
        this.userService = userService;
        this.databaseService = databaseService;
        this.gameService = gameService;
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
        //TODO proper error handling
        var authtoken = req.headers("authorization");
        var gameData = new Gson().fromJson(req.body(), GameData.class);
        try{
            var game = gameService.createGame(authtoken, gameData.gameName());
            res.status(200);
            return new Gson().toJson(game.gameID());
        } catch (Exception e) {
            res.status(500);
            return "Error";
        }
    }
    private Object joinGame(Request req, Response res) throws ResponseException {
        //TODO join game

        res.status(200);
        return "";
    }
}
