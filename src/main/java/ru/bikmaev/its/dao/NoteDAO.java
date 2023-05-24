package ru.bikmaev.its.dao;

import org.springframework.stereotype.Component;
import ru.bikmaev.its.models.Note;

import javax.lang.model.type.ArrayType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class NoteDAO {

    private static int NOTE_COUNT;

    private static final String URL = "jdbc:postgresql://localhost:5432/notepad-db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    // Подключение к базе данных, то есть соединение с помощью JDBC к нашей базе данных postgres
    private static Connection connection;

    // Статический connection будем в статическом блоке инициализаровать
    static {
        try {
            // Вызываем класс forName и подгружаем класс с нашим JDBC драйвером
            // То есть с помощью рефлексии подгружаем класс Driver и удостоверяемся в том, что этот класс ...
            // ... загружен в оперативную память и им можно пользоваться
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Теперь когда есть драйвер для работы с базой данных, мы его используем
        try {
            // В наш connection помещаем то, что нам вернет DriverManager.getConnection
            // Здесь мы должны указать URL, USERNAME, PASSWORD, то есть 3 компонента, которые необходимы для ...
            // ... подключения к базе данных. И с помощью зависимости JDBC, с помощью драйвера мы подключимся ..
            // .. к нашем базе данных
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // Проблемы JDBC:
            // 1. низкоуровневый (все запросы нужно писать вручную - нет абстракций)\
            // 2. любая ошибка связанная с работой с базой данных - это SQLException (ошибка подключения, добавления) ..
            // .. не понятно какая точно ошибка произошла у нас с базой данных
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Теперь когда есть connection (подключение к базе данных)  мы можем реализовать наш первый метод index ..
    // .. и считать из базы данных всех наших людей

    // Посмотреть все заметки
    public List<Note> index() {
        // Лист с людьми, в который будем класть людей, которых мы достанем из нашей базы данных
        List<Note> notes = new ArrayList<>();

        try {
            // Объект Statement это тот объект, который содержит в себе SQL запрос к базе данных
            // То есть на этом объекте connection (на нашем соединении) создаем объект запрос к базе данных
            Statement statement = connection.createStatement();
            // Переменная с запросом
            String SQL = "SELECT * FROM Note";
            // Выполнение запроса, то есть на нашем statement выполняем SQL запрос с помощью executeQuery ..
            // .. мы передаем ему наш SQL запрос и он выполняет его на нашей базе данных
            // При выполнении этого кода у нас вернется несколько строк кода из базы данных
            // Примем их с помощью объекта класса ResultSet
            ResultSet resultSet = statement.executeQuery(SQL);

            // Теперь нужно пройтись по каждой строке и вручную перевести в джава объект
            while(resultSet.next()) {
                Note note = new Note();

                note.setId(resultSet.getInt("id"));
                note.setTitle(resultSet.getString("title"));
                note.setDescription(resultSet.getString("description"));

                notes.add(note);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return notes;
    }

    // Посмотреть конкретную заметку
    public Note show(int id) {
        // Почему? За пределами try его видно не будет и, соот-но, return note; выполнить не удастся
        Note note = null;

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Note WHERE id=?");

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            note = new Note();

            note.setId(resultSet.getInt("id"));
            note.setTitle(resultSet.getString("title"));
            note.setDescription(resultSet.getString("description"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return note;
    }

    // Сохранить заметку
    public void save(Note note) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Note VALUES(1, ?, ?)");

            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getDescription());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Обновить заметку
    public void update(int id, Note updatedNote) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Note SET title=?, description=? WHERE id=?");

            preparedStatement.setString(1, updatedNote.getTitle());
            preparedStatement.setString(2, updatedNote.getDescription());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Удалить заметку
    public void delete(int id) {
        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM Note WHERE id=?");

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}


