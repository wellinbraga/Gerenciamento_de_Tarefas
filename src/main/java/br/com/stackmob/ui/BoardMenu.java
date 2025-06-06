package br.com.stackmob.ui;

import br.com.stackmob.dto.BoardColumnInfoDTO;
import br.com.stackmob.dto.BoardDetailsDTO;
import br.com.stackmob.persistence.entity.BoardColumnEntity;
import br.com.stackmob.persistence.entity.BoardColumnKindEnum;
import br.com.stackmob.persistence.entity.BoardEntity;
import br.com.stackmob.persistence.entity.CardEntity;
import br.com.stackmob.service.BoardColumnQueryService;
import br.com.stackmob.service.BoardQueryService;
import br.com.stackmob.service.CardQueryService;
import br.com.stackmob.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static br.com.stackmob.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {
    final Scanner sc = new Scanner(System.in).useDelimiter("\n");
    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desenada\n", entity.getId());
            int optional = -1;

            do {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver colunas com cards");
                System.out.println("8 - Ver Card");
                System.out.println("9 - Voltar para o menu  anterior");
                System.out.println("10 - Sair");

                optional = sc.nextInt();

                switch (optional) {
                    case 1 -> createdCard();
                    case 2 -> moveCardToNextColun();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando ao menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção invalida, informe uma opção do menu!");
                }

            } while (optional != 9);

            sc.close();
        }catch (SQLException e)  {
            e.printStackTrace();
            System.exit(0);
        }

    }

    private final BoardEntity entity;

    private void createdCard() throws SQLException{
        CardEntity card = new CardEntity();
        System.out.println("informe o título do card");
        card.setTitle(sc.next());
        System.out.println("informe a descrição do card");
        card.setDescription(sc.next());
        card.setBoardColumn(entity.getInitialColumn());
        try(Connection connetion = getConnection()){
            new CardService(connetion).insert(card);

        }

    }

    private void moveCardToNextColun() throws SQLException {
        System.out.println("Informe o ID do card que deseja mover");
        Long cardId = sc.nextLong();
        List<BoardColumnInfoDTO> boardColumns = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try(Connection connection = getConnection()){
            new CardService(connection).moveToNextColumn(cardId, boardColumns);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        
    }

    private void blockCard() throws SQLException {
        System.out.println("Informe o ID do card que deseja bloquear");
        Long cardId = sc.nextLong();
        System.out.println("Informe o motivo do bloqueio");
        String reason = sc.next();
        try(Connection connection = getConnection()){
            List<BoardColumnInfoDTO> boardColumns = entity.getBoardColumns().stream()
                    .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                    .toList();
            new CardService(connection).block(cardId, reason, boardColumns);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void unblockCard() throws SQLException {
        System.out.println("Informe o ID do card que deseja desbloquear");
        Long cardId = sc.nextLong();
        System.out.println("Informe o motivo do desbloqueio");
        String reason = sc.next();
        try(Connection connection = getConnection()){
            new CardService(connection).unblock(cardId, reason);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cancelCard() throws SQLException  {
        System.out.println("Informe o ID do card que deseja cancelar");
        Long cardId = sc.nextLong();
        BoardColumnEntity cancelColumn = entity.getCancelColumn();
        List<BoardColumnInfoDTO> boardColumnInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try(Connection connection = getConnection()){
            new CardService(connection).cancel(cardId, cancelColumn.getId(), boardColumnInfo);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try(Connection connection = getConnection()){
            Optional<BoardDetailsDTO> optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent( b-> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach( c->
                    System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardAmout())
                );
            });
        }
    }

    private void showColumn() throws  SQLException {
        List<Long> columnsIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        Long selectedColumn = -1L;
        while(!columnsIds.contains(selectedColumn)){
            System.out.printf("Escolha uma coluna do board %s\n", entity.getId());
            entity.getBoardColumns().forEach(c -> System.out.printf("%s - [%s]\n",c.getId(),c.getName()));
            selectedColumn = sc.nextLong();
        }
        try(Connection connetion = getConnection();){
            Optional<BoardColumnEntity> column = new BoardColumnQueryService(connetion).findById(selectedColumn);
            column.ifPresent(co -> {
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card %s - s%\nDescrição: %s",
                        ca.getId(),ca.getTitle(),ca.getDescription()));
            });
        }
    }

    private void showCard() throws SQLException{
        System.out.println("Informe o ID do card que deseja visualizar");
        Long selectedCardId = sc.nextLong();
        try(Connection connection = getConnection()){
            new CardQueryService(connection).findById(selectedCardId).ifPresentOrElse(
                    c -> {
                        System.out.printf("Card %s - %s\n", c.id(), c.title());
                        System.out.printf("Descrição %s\n", c.description());
                        System.out.println(c.blocked() ? "Está bloqueado. Motivo: " + c.blockReason() : "Não está bloqueado!");
                        System.out.printf("Já foi bloqueado %s vezes\n", c.blockAmount());
                        System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                    }, () -> System.out.printf("Não existe um card com o ID %s\n", selectedCardId));

        }
    }
}
