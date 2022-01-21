package server;

public interface AuthService {
    /*
    Получение никнейма по логину и паролю
    возвращает никнейм, если учетная запись существует
    null , если логин/пароль не обнаружено
     */
    String getNicknameByLoginPassword(String login, String password);
}
