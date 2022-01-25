package server;

public interface AuthService {
    /*
    Получение никнейма по логину и паролю
    возвращает никнейм, если учетная запись существует
    null , если логин/пароль не обнаружено
     */
    String getNicknameByLoginPassword(String login, String password);
    /*
    Регистрация нового пользователя
    при успешной регистрации вернет true
     */
    boolean registration(String login, String password, String nickname);


}
