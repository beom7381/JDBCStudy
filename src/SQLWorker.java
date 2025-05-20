import java.io.Closeable;
import java.sql.*;

public class SQLWorker implements Closeable {
    private String _url;
    private String _accessId;
    private String _accessPassword;

    private boolean _isConnect;
    private Connection _connection;
    private Statement _statement;
    private ResultSet _resultSet;


    public SQLWorker(String url, String accessId, String accessPassword){
        setUrl(url);
        setAccessId(accessId);
        setAccessPassword(accessPassword);
    }

    public void connect() {
        try{
            _connection = DriverManager.getConnection(_url, _accessId, _accessPassword);
            _statement = _connection.createStatement();
            _resultSet = null;

            _isConnect = true;
        }
        catch (SQLException e){
            clear();

            _isConnect = false;
        }
    }

    public boolean excute(QueryType type, String query){
        boolean isSuccess = false;

        try {
            switch (type){
                case SELECT:
                    _resultSet = _statement.executeQuery(query);
                    _resultSet.next();
                    break;
                case INSERT:
                case DELETE:
                case UPDATE:
                    int result = _statement.executeUpdate(query);

                    if(result == 0){
                        throw new SQLException("Nothing Changed");
                    }
                    break;
            }

            isSuccess = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isSuccess;
    }

    public void setUrl(String url) {
        this._url = url;
    }

    public void setAccessPassword(String accessPassword) {
        this._accessPassword = accessPassword;
    }

    public void setAccessId(String accessId) {
        this._accessId = accessId;
    }

    public ResultSet getResult(){
        return _resultSet;
    }

    public boolean isConnect(){
        return _isConnect;
    }

    private void clear(){
        _connection = null;
        _statement = null;
        _resultSet = null;
    }

    @Override
    public void close() {
        try {
            _resultSet.close();
            _statement.close();
            _connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public enum QueryType {
        SELECT,
        UPDATE,
        DELETE,
        INSERT
    }
}