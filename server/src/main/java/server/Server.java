package server;

import com.google.gson.JsonObject;
import model.AuthData;
import service.DatabaseService;
import service.GameService;
import service.UserService;
import spark.*;
import com.google.gson.Gson;
import exception.ResponseException;

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
        try{
            databaseService.clear();
            res.status(200);
            return "";
        } catch (Exception e) {
            res.status(500);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", e.getMessage());
            return jsonObject.toString();
        }
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        //TODO proper error handling
        var user = new Gson().fromJson(req.body(), model.UserData.class);
        AuthData authData;
        try{
            authData = userService.register(user);
        } catch (Exception e) {
            throw new ResponseException(400, "User already exists");
        }
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object loginUser(Request req, Response res) throws ResponseException {
        //TODO proper error handling
        var user = new Gson().fromJson(req.body(), model.LoginData.class);

        try{
            AuthData authData = userService.login(user.username(), user.password());
            res.status(200);
            return new Gson().toJson(authData);
        } catch (Exception e) {
            res.status(500);
            return "Error";
        }

    }
    private Object logoutUser(Request req, Response res) throws ResponseException {
        //TODO proper error handling
        var authtoken = req.headers("authorization");
        try{
            userService.logout(authtoken);
            res.status(200);
            return "";
        } catch (Exception e) {
            res.status(500);
            return "Error";
        }
    }
    private Object listGames(Request req, Response res) throws ResponseException {
        //TODO proper error handling
        var authtoken = req.headers("authorization");
        try{
            var games = gameService.getGames(authtoken);
            res.status(200);
            return new Gson().toJson(games);
        } catch (Exception e) {
            res.status(500);
            return "Error";
        }
    }
    private Object createGame(Request req, Response res) throws ResponseException {
        //TODO proper error handling
        var authtoken = req.headers("authorization");
        var gameData = new Gson().fromJson(req.body(), model.GameData.class);
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
