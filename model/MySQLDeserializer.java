package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MySQLDeserializer<T> {
    T deserialize(ResultSet resultSet) throws SQLException;
}
