package client;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8082);
        System.out.println("Started test HTTP server on " + port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerUserSucceed() throws Exception {
        UserData user = new UserData("username123456", "password", "email@email.com");
        ServerFacade facade = new ServerFacade("http://localhost:8082");

        var authData = facade.register(user);
        Assertions.assertEquals(authData.username(), user.username());
    }

    @Test
    public void registerUserFail() throws Exception {
        UserData user = new UserData("", "", "");
        ServerFacade facade = new ServerFacade("http://localhost:8082");

        Assertions.assertThrows(Exception.class, () -> {
            facade.register(user);
        });
    }

    @Test
    public void loginSucceeds() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        String username = "usernameLogin";
        String password = "passwordLogin";

        UserData userData = new UserData(username, password, "email@email.com");
        var authdata = facade.register(userData);
        facade.logout(authdata);
        var authData = facade.login(new LoginData(username, password));
        Assertions.assertEquals(authData.username(), username);
    }

    @Test
    public void loginFails() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");

        Assertions.assertThrows(Exception.class, () -> {
            facade.login(new LoginData("fakeusername", "fake"));
        });
    }

    @Test
    public void logoutSucceeds() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        String username = "usernameLogout";
        String password = "passwordLogout";

        UserData userData = new UserData(username, password, "email@email.com");
        var authdata = facade.register(userData);
        facade.logout(authdata);
    }

    @Test
    public void logoutFails() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        Assertions.assertThrows(Exception.class, () -> {
            facade.logout(new AuthData("fake", "fake"));
        });
    }

    @Test
    public void listGamesSucceeds() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        String username = "usernameListGames";
        String password = "passwordListGames";

        UserData userData = new UserData(username, password, "email@email.com");
        var authdata = facade.register(userData);
        facade.listGames(authdata);
    }

    @Test
    public void listGamesFails() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        Assertions.assertThrows(Exception.class, () -> {
            facade.listGames(new AuthData("fake", "fake"));
        });
    }

    @Test
    public void createGameSucceeds() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        String username = "usernameCreateGame";
        String password = "passwordCreateGame";

        UserData userData = new UserData(username, password, "email@email.com");
        var authdata = facade.register(userData);
        facade.createGame( new GameCreate("GameTest"),authdata);
    }

    @Test
    public void createGameFails() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        Assertions.assertThrows(Exception.class, () -> {
            facade.createGame(new GameCreate("FailedGame"), new AuthData("fake", "fake"));
        });
    }

    @Test
    public void joinGameSucceeds() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        String username = "usernameJoinGame";
        String password = "passwordJoinGame";

        UserData userData = new UserData(username, password, "email@email.com");
        var authdata = facade.register(userData);
        var gameData = facade.createGame(new GameCreate("TestFakeGame"), authdata);
        facade.joinGame(new JoinGame(ChessGame.TeamColor.WHITE, gameData.gameID()), authdata);
    }

    @Test
    public void joinGameFails() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        Assertions.assertThrows(Exception.class, () -> {
            facade.joinGame( new JoinGame(ChessGame.TeamColor.WHITE, 9), new AuthData("fake", "fake"));
        });
    }




}
