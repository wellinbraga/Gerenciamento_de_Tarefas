package br.com.stackmob.ui;

import br.com.stackmob.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public class BoardMenu {

    private final BoardEntity entity;
    public void execute() {
        Scanner sc = new Scanner(System.in).useDelimiter("\n");

        System.out.printf("Bem vindo ao board %s, selecione a operação desenada", entity.getId());
        int optional = -1;

        do{
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

            switch (optional){
                case 1 -> createdCard();
                case 2 -> moveCardToNextColun() ;
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

        } while (optional != 9 );

        sc.close();
    }



    private void createdCard() {
    }

    private void moveCardToNextColun() {
        
    }

    private void blockCard() {
        
    }

    private void unblockCard() {
    }

    private void cancelCard() {
    }

    private void showBoard() {
    }

    private void showColumn() {
        
    }

    private void showCard() {
    }
}
