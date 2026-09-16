package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

/**
 * Kapselt die Verbindung zur Oracle-Testdatenbank sowie den Aufruf
 * von Prozeduren und Funktionen.
 */
public class Database {

    private String username = "USERNAME"; // TODO: echten Benutzernamen der Testdatenbank eintragen
    private String password = "PASSWORD"; // TODO: echtes Passwort der Testdatenbank eintragen
    private String verbinder = "jdbc:oracle:thin:@localhost:1521:XE"; // TODO: echte Verbindungs-URL eintragen

    private Connection conn;

    public Database() {
    }

    /**
     * Öffnet die Verbindung zur Datenbank.
     * @throws SQLException
     */
    public void open() throws SQLException {
        conn = DriverManager.getConnection(verbinder, username, password);
    }

    /**
     * Schließt die Verbindung zur Datenbank.
     */
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Führt eine Oracle-Prozedur aus.
     * @param procedureString der Prozeduraufruf inklusive Parameter.
     * @throws SQLException
     */
    public void executeProcedure(String procedureString) throws SQLException {
        String sql = "{ call " + procedureString + " }";
        CallableStatement cstmt = null;

        try {
            cstmt = conn.prepareCall(sql);
            cstmt.execute();

        } finally {
            if (cstmt != null) {
                cstmt.close();
            }
        }
    }

    /**
     * Führt eine Oracle-Function aus.
     * @param functionString der Funktionsaufruf inklusive Parameter.
     * @return den Rückgabewert der Funktion.
     * @throws SQLException
     */
    public String executeFunction(String functionString) throws SQLException {
        String returnValue = null;
        String sql = "{ call ? := " + functionString + " }";
        CallableStatement cstmt = null;

        try {
            cstmt = conn.prepareCall(sql);

            cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
            cstmt.execute();
            returnValue = cstmt.getString(1);

        } finally {
            if (cstmt != null) {
                cstmt.close();
            }
        }

        return returnValue;
    }

    public Connection getConn() {
        return conn;
    }

    /**
     * Erstellt ein Statement-Objekt über das Queries ausgeführt werden können.
     * @param sql der Query als String, Parameter als ?
     * @return das PreparedStatement-Objekt für diesen Query
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }
}
