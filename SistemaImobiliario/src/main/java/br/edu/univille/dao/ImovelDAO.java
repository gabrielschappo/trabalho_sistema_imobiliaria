package br.edu.univille.dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImovelDAO extends BaseDAO {

    public void inserir(Imovel imovel) {
        String sql = "INSERT INTO imovel(id, endereco, tipo, quartos, banheiros, valor_aluguel_base, disponivel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var con = con();
             var pre = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pre.setLong(1, imovel.getId());
            pre.setString(2, imovel.getEndereco());
            pre.setString(3, imovel.getTipo());
            pre.setInt(4, imovel.getQuartos());
            pre.setInt(5, imovel.getBanheiros());
            pre.setBigDecimal(6, imovel.getValorAluguelBase());
            pre.setBoolean(7, imovel.isDisponivel());
            pre.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Imovel imovel) {
        String sql = "UPDATE imovel SET endereco = ?, tipo = ?, quartos = ?, banheiros = ?, valor_aluguel_base = ?, disponivel = ? WHERE id = ?";
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            pre.setString(1, imovel.getEndereco());
            pre.setString(2, imovel.getTipo());
            pre.setInt(3, imovel.getQuartos());
            pre.setInt(4, imovel.getBanheiros());
            pre.setBigDecimal(5, imovel.getValorAluguelBase());
            pre.setBoolean(6, imovel.isDisponivel());
            pre.setLong(7, imovel.getId());
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPeloId(long id) {
        String sql = "DELETE FROM imovel WHERE id = ?";
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            pre.setLong(1, id);
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Imovel> obterPeloId(long id) {
        String sql = "SELECT id, endereco, tipo, quartos, banheiros, valor_aluguel_base, disponivel FROM imovel WHERE id = ?";
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            pre.setLong(1, id);
            var rs = pre.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToImovel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Imovel> obterTodos() {
        String sql = "SELECT id, endereco, tipo, quartos, banheiros, valor_aluguel_base, disponivel FROM imovel ORDER BY endereco";
        List<Imovel> lista = new ArrayList<>();
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            var rs = pre.executeQuery();
            while (rs.next()) {
                lista.add(mapRowToImovel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Imovel> obterDisponiveis() {
        String sql = "SELECT id, endereco, tipo, quartos, banheiros, valor_aluguel_base, disponivel FROM imovel WHERE disponivel = TRUE ORDER BY endereco";
        List<Imovel> lista = new ArrayList<>();
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            var rs = pre.executeQuery();
            while (rs.next()) {
                lista.add(mapRowToImovel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Imovel mapRowToImovel(java.sql.ResultSet rs) throws SQLException {
        Imovel imovel = new Imovel();
        imovel.setId(rs.getLong("id"));
        imovel.setEndereco(rs.getString("endereco"));
        imovel.setTipo(rs.getString("tipo"));
        imovel.setQuartos(rs.getInt("quartos"));
        imovel.setBanheiros(rs.getInt("banheiros"));
        imovel.setValorAluguelBase(rs.getBigDecimal("valor_aluguel_base"));
        imovel.setDisponivel(rs.getBoolean("disponivel"));
        return imovel;
    }
}
