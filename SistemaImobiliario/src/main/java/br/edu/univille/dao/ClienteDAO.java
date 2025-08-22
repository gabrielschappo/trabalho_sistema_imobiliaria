package br.edu.univille.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO extends BaseDAO {

    public void inserir(Cliente cliente) {
        long proximoId = 1;
        String sqlMaxId = "SELECT MAX(id) FROM cliente";
        try (var con = con();
             var preMaxId = con.prepareStatement(sqlMaxId)) {
            ResultSet rs = preMaxId.executeQuery();
            if (rs.next()) {
                proximoId = rs.getLong(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        cliente.setId(proximoId);

        String sql = "INSERT INTO cliente(id, nome, cpf, telefone, email) VALUES (?, ?, ?, ?, ?)";
        try (var con = con();
             var pre = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pre.setLong(1, cliente.getId());
            pre.setString(2, cliente.getNome());
            pre.setString(3, cliente.getCpf());
            pre.setString(4, cliente.getTelefone());
            pre.setString(5, cliente.getEmail());
            pre.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE id = ?";
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            pre.setString(1, cliente.getNome());
            pre.setString(2, cliente.getCpf());
            pre.setString(3, cliente.getTelefone());
            pre.setString(4, cliente.getEmail());
            pre.setLong(5, cliente.getId());
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deletarPeloId(long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            pre.setLong(1, id);
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Optional<Cliente> obterPeloId(long id) {
        String sql = "SELECT id, nome, cpf, telefone, email FROM cliente WHERE id = ?";
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            pre.setLong(1, id);
            var rs = pre.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Cliente> obterTodos() {
        String sql = "SELECT id, nome, cpf, telefone, email FROM cliente ORDER BY nome";
        List<Cliente> lista = new ArrayList<>();
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            var rs = pre.executeQuery();
            while (rs.next()) {
                lista.add(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public List<Cliente> obterClientesComMaisContratos() {
        String sql = "SELECT c.*, COUNT(ca.id) as total_contratos " +
                "FROM cliente c " +
                "LEFT JOIN contrato_aluguel ca ON c.id = ca.id_cliente " +
                "GROUP BY c.id, c.nome, c.cpf, c.telefone, c.email " +
                "ORDER BY total_contratos DESC";
        List<Cliente> lista = new ArrayList<>();
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            var rs = pre.executeQuery();
            while (rs.next()) {
                lista.add(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Cliente mapRowToCliente(java.sql.ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));
        return cliente;
    }
}
