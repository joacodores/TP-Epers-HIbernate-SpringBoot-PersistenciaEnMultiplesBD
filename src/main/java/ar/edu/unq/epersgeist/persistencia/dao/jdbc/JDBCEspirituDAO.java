package ar.edu.unq.epersgeist.persistencia.dao.jdbc;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public record JDBCEspirituDAO() implements EspirituDAO {

    public Espiritu crear(Espiritu espiritu) {

        return JDBCConnector.getInstance().execute(conn  -> {
            try {
                var ps = prepareInsertQueryStatement(espiritu, conn);
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                espiritu.setId(rs.getLong(1));
                ps.execute();
                return espiritu;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static PreparedStatement prepareInsertQueryStatement(Espiritu espiritu, Connection conn) throws SQLException {
        var ps = conn.prepareStatement("INSERT INTO espiritu (tipo, nivel, nombre) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, espiritu.getTipo());
        ps.setInt(2, espiritu.getNivelDeConexion());
        ps.setString(3, espiritu.getNombre());
        return ps;
    }





    public Espiritu recuperar(Long idDelEspiritu) {

        return JDBCConnector.getInstance().execute( conn -> {
            try {
                var ps = prepareSelectQueryStatement(idDelEspiritu, conn);
                var resultSet = ps.executeQuery();
                return buildEspiritu(idDelEspiritu, resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static PreparedStatement prepareSelectQueryStatement(Long id, Connection conn) throws SQLException {
        var ps = conn.prepareStatement("SELECT tipo, nivelDeConexion, nombre FROM espiritu WHERE id = ?");
        ps.setLong(1, id);
        return ps;
    };

    private static Espiritu buildEspiritu (Long id, ResultSet resultSet) throws SQLException {

        Espiritu espiritu = new Espiritu(
                resultSet.getString("tipo"),
                resultSet.getInt("nivelDeConexion"),
                resultSet.getString("nombre")
        );

        espiritu.setId(id);

        return espiritu;
    }




    public List<Espiritu> recuperarTodos() {

        List<Espiritu> espiritus = new ArrayList<>();
        return JDBCConnector.getInstance().execute( conn -> {
            try {
                var ps = conn.prepareStatement("SELECT tipo, nivelDeConexion, nombre FROM espiritu ORDER BY nombre ASC");
                var resultSet = ps.executeQuery();
                long id = 1;
                while (resultSet.next()) {
                    buildEspiritu(id,resultSet);
                    id++;
                }
                return espiritus;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }


    public void actualizar(Espiritu espiritu) {
        JDBCConnector.getInstance().execute (conn -> {
            try {
                var ps = prepareUpdateQueryStatement(espiritu, conn);
                return ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static PreparedStatement prepareUpdateQueryStatement(Espiritu espiritu, Connection conn) throws SQLException {
        var ps = conn.prepareStatement("UPDATE espiritu SET (tipo = ?, nivel = ?, nombre = ?) WHERE id = ? ");
        ps.setString(1, espiritu.getTipo());
        ps.setInt(2, espiritu.getNivelDeConexion());
        ps.setString(3, espiritu.getNombre());
        ps.setLong(4, espiritu.getId());
        return ps;
    }

    public void eliminar(Long idDelEspiritu) {
        JDBCConnector.getInstance().execute (conn -> {
            try {
                var ps = prepareDeleteQueryStatement(idDelEspiritu, conn);
                return ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private static PreparedStatement prepareDeleteQueryStatement(Long id, Connection conn) throws SQLException {
        var ps = conn.prepareStatement("DELETE FROM espiritu WHERE id =  ? ");
        ps.setLong(1, id);
        return ps;
    }


    public JDBCEspirituDAO() {
        try {
            var uri = getClass().getClassLoader().getResource("createAll.sql").toURI();
            var initializeScript = Files.readString(Paths.get(uri));
            JDBCConnector.getInstance().execute(conn -> {
                try {
                    var ps = conn.prepareStatement(initializeScript);
                    return ps.execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}