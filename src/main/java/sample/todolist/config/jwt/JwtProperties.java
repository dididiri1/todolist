package sample.todolist.config.jwt;

public interface JwtProperties {
    String SECRET = "kangmin";
    int EXPIRATION_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}