package br.edu.univille.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContratoAluguelDAO extends BaseDAO {


    public void inserir(ContratoAluguel contrato) {
        String sqlContrato = "INSERT INTO contrato_aluguel(id, id_cliente, id_imovel, data_inicio, data_fim, valor_aluguel_pactuado, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlImovel = "UPDATE imovel SET disponivel = FALSE WHERE id = ?";
        String sqlMaxId = "SELECT MAX(id) FROM contrato_aluguel";

        Connection con = null;
        try {
            con = con();
            con.setAutoCommit(false); // Inicia a transação

            long proximoId = 1;
            try (var preMaxId = con.prepareStatement(sqlMaxId)) {
                ResultSet rs = preMaxId.executeQuery();
                if (rs.next()) {
                    proximoId = rs.getLong(1) + 1;
                }
            }
            contrato.setId(proximoId);

            try (var preContrato = con.prepareStatement(sqlContrato)) {
                preContrato.setLong(1, contrato.getId()); // ID gerado pelo código
                preContrato.setLong(2, contrato.getIdCliente());
                preContrato.setLong(3, contrato.getIdImovel());
                preContrato.setDate(4, Date.valueOf(contrato.getDataInicio()));
                preContrato.setDate(5, Date.valueOf(contrato.getDataFim()));
                preContrato.setBigDecimal(6, contrato.getValorAluguelPactuado());
                preContrato.setBoolean(7, contrato.isAtivo());
                preContrato.execute();
            }

            try (var preImovel = con.prepareStatement(sqlImovel)) {
                preImovel.setLong(1, contrato.getIdImovel());
                preImovel.execute();
            }

            con.commit(); // Efetiva a transação se tudo deu certo

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<ContratoAluguel> obterAtivos() {
        String sql = "SELECT ca.*, c.nome as cliente_nome, i.endereco as imovel_endereco " +
                "FROM contrato_aluguel ca " +
                "JOIN cliente c ON ca.id_cliente = c.id " +
                "JOIN imovel i ON ca.id_imovel = i.id " +
                "WHERE ca.ativo = TRUE";
        List<ContratoAluguel> lista = new ArrayList<>();
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            var rs = pre.executeQuery();
            while (rs.next()) {
                lista.add(mapRowToContratoComDetalhes(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ContratoAluguel> obterExpirandoEm30Dias() {
        String sql = "SELECT ca.*, c.nome as cliente_nome, i.endereco as imovel_endereco " +
                "FROM contrato_aluguel ca " +
                "JOIN cliente c ON ca.id_cliente = c.id " +
                "JOIN imovel i ON ca.id_imovel = i.id " +
                "WHERE ca.ativo = TRUE AND ca.data_fim BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '30 day')";

        List<ContratoAluguel> lista = new ArrayList<>();
        try (var con = con(); var pre = con.prepareStatement(sql)) {
            var rs = pre.executeQuery();
            while (rs.next()) {
                lista.add(mapRowToContratoComDetalhes(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private ContratoAluguel mapRowToContratoComDetalhes(java.sql.ResultSet rs) throws SQLException {
        ContratoAluguel contrato = new ContratoAluguel();
        contrato.setId(rs.getLong("id"));
        contrato.setIdCliente(rs.getLong("id_cliente"));
        contrato.setIdImovel(rs.getLong("id_imovel"));
        contrato.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        contrato.setDataFim(rs.getDate("data_fim").toLocalDate());
        contrato.setValorAluguelPactuado(rs.getBigDecimal("valor_aluguel_pactuado"));
        contrato.setAtivo(rs.getBoolean("ativo"));

        Cliente cliente = new Cliente();
        cliente.setId(contrato.getIdCliente());
        cliente.setNome(rs.getString("cliente_nome"));
        contrato.setCliente(cliente);

        Imovel imovel = new Imovel();
        imovel.setId(contrato.getIdImovel());
        imovel.setEndereco(rs.getString("imovel_endereco"));
        contrato.setImovel(imovel);

        return contrato;
    }
}
