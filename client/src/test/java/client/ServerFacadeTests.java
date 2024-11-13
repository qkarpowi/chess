package client;

import exception.ResponseException;
import model.LoginData;
import model.UserData;
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

        var authData = facade.Register(user);
        Assertions.assertEquals(authData.username(), user.username());
    }

    @Test
    public void registerUserFail() throws Exception {
        UserData user = new UserData("", "", "");
        ServerFacade facade = new ServerFacade("http://localhost:8082");

        Assertions.assertThrows(Exception.class, () -> {
            facade.Register(user);
        });
    }

    @Test
    public void loginSucceeds() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");
        String username = "usernameLogin";
        String password = "passwordLogin";

        UserData userData = new UserData(username, password, "email@email.com");
        facade.Register(userData);
        facade.logout();
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
        facade.Register(userData);
        facade.logout();
    }

    @Test
    public void logoutFails() throws Exception {
        ServerFacade facade = new ServerFacade("http://localhost:8082");

        Assertions.assertThrows(Exception.class, facade::logout);
    }

}
