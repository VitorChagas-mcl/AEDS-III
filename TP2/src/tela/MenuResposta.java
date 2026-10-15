package tela;

import auxiliar.Leitura;

public class MenuResposta{
 
    public void telaBuscarPergunta() throws Exception{
        String opcao;
        do{
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Buscar Pergunta ");
            System.out.println("\n(A) Perguntas mais recentes");
            System.out.println("(B) Buscar Pergunta por palavra-chave");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");

            opcao = Leitura.Teclado().trim().toUpperCase();

            switch (opcao) {
                case "A":
                    telaListarPerguntas();
                    break;
                case "B":
                    break;
                case "R":
                    break;
                default:
                    System.out.println("\nOpção inválida!");
            }
        }while(!opcao.equalsIgnoreCase("R"));
    }

    public void telaListarPerguntas() throws Exception{
        String opcao;
        do{
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Buscar Pergunta > Perguntas mais recentes");
            System.out.println("\n(A) Página anterior");
            System.out.println("(B) Próxima página");
            System.out.println("\n(R) Retornar");
            System.out.print("\nOpção: ");
            opcao = Leitura.Teclado().trim().toUpperCase();
        }while(!opcao.equalsIgnoreCase("R"));
    }   
}