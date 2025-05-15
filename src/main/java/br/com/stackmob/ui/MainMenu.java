package br.com.stackmob.ui;

import br.com.stackmob.persistence.entity.BoardColumnEntity;
import br.com.stackmob.persistence.entity.BoardColumnKindEnum;
import br.com.stackmob.persistence.entity.BoardEntity;
import br.com.stackmob.service.BoardQueryService;
import br.com.stackmob.service.BoardService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static br.com.stackmob.persistence.config.ConnectionConfig.getConnection;
import static br.com.stackmob.persistence.entity.BoardColumnKindEnum.*;

public class MainMenu {

    private Scanner sc = new Scanner(System.in);

    public void execute(){
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        int optional = -1;

        do{
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            optional = sc.nextInt();

            switch (optional){
                case 1 -> createdBoard();
                case 2 -> selectedBoard() ;
                case 3 -> deletedBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção invalida, informe uma opção do menu!");
            }

        } while (optional != 4 );

        sc.close();
    }

    private void createdBoard() {
        BoardEntity entity = new BoardEntity();
        System.out.println("Informe o nome do seu Board");
        entity.setName(sc.next());

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informa a quantidade colunas do seu Board. (Minimo 3 e Maximo 8)");
        int additionalColumns = sc.nextInt();

        int i  = 0;
        do{
            if(i == 0){
                createdBoard("Informe o nome da coluna inicial do board", INITIAL, 0, columns);
            } else if (i == additionalColumns){
                createdBoard("Informe o nome da coluna final do board", FINAL, i, columns);
            } else {
                createdBoard("Informe o nome da coluna do board", PENDING, i, columns);
            }

            i++;
        }while (i <= additionalColumns);

        createdBoard("Informe o nome da coluna de cancelamento do board", CANCEL, additionalColumns + 1, columns);

        entity.setBoardColumns(columns);
        try (Connection connection = getConnection()){
            BoardService boardService =  new BoardService(connection);
            boardService.insert(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void createdBoard(String text, BoardColumnKindEnum kind, int order, List<BoardColumnEntity> columns) {
        System.out.println(text);
        String nameColumns = sc.next();
        BoardColumnEntity boardColumn = createdColum(nameColumns, kind, order);
        columns.add(boardColumn);
    }

    private void selectedBoard() {
        System.out.println("Informe o id do board que deseja selecionar");
        Long id = sc.nextLong();
        try(Connection connection = getConnection()){
            BoardQueryService boardService = new BoardQueryService(connection);
            Optional<BoardEntity> listBoard = boardService.findById(id);
            listBoard.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.printf("Não foi encontrato um board com id %s\n",id));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }

    private void deletedBoard() {
        System.out.println("Informe o id do board que será excluido");
        Long id = sc.nextLong();
        try(Connection connection = getConnection()){
            BoardService boardService = new BoardService(connection);
            if(boardService.delete(id)){
                System.out.printf("O board %s foi excluido\n", id);
            } else {
                System.out.printf("Não foi encontrato um board com id %s\n",id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private BoardColumnEntity createdColum(String name, BoardColumnKindEnum kind, int order){
        BoardColumnEntity boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setOrder(order);
        boardColumn.setKind(kind);
        return boardColumn;
    }
}
