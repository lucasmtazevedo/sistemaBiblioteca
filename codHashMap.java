package ATP;

import java.util.*;

class Grafo {
    // Mapa para armazenar os livros por título
    private Map<String, Livro> livros;
    // Mapa para armazenar as recomendações de cada livro
    private Map<String, Queue<Livro>> recomendacoes;

    // Construtor da classe Grafo
    public Grafo() {
        livros = new HashMap<>();
        recomendacoes = new HashMap<>();
    }

    public List<Livro> encontrarCaminhoMaisCurto(Livro origem, int limite) {
        // Mapeia os livros para suas distâncias a partir do livro de origem
        Map<Livro, Integer> distancias = new HashMap<>();

        // Inicializa a fila de prioridade com a origem
        PriorityQueue<Livro> fila = new PriorityQueue<>(Comparator.comparingInt(distancias::get));
        fila.add(origem);
        distancias.put(origem, 0);

        // Enquanto a fila não estiver vazia
        while (!fila.isEmpty()) {
            // Remove o livro com menor distância da fila
            Livro atual = fila.poll();
            int distanciaAtual = distancias.get(atual);

            // Verifica os vizinhos do livro atual
            for (Livro vizinho : recomendacoes.getOrDefault(atual.titulo, new LinkedList<>())) {
                // Calcula a distância até o vizinho
                int novaDistancia = distanciaAtual + 1; // Como não há pesos, a distância é sempre 1

                // Se a nova distância for menor do que a distância registrada
                if (!distancias.containsKey(vizinho) || novaDistancia < distancias.get(vizinho)) {
                    // Atualiza a distância
                    distancias.put(vizinho, novaDistancia);
                    // Adiciona o vizinho à fila para continuar explorando seus vizinhos
                    fila.add(vizinho);
                }
            }
        }

        List<Livro> recomendacoes = new ArrayList<>(distancias.keySet());
        recomendacoes.sort(Comparator.comparingInt(distancias::get));

        return recomendacoes.subList(0, Math.min(limite, recomendacoes.size()));
    }
    // Método para adicionar um livro ao grafo
    public void adicionarLivro(Livro livro) {
        livros.put(livro.titulo, livro);
        recomendacoes.put(livro.titulo, new LinkedList<>());
    }
    // Método para adicionar uma recomendação entre dois livros
    public void adicionarRecomendacao(Livro origem, Livro destino) {
        Livro livroOrigem = livros.get(origem.titulo);
        Livro livroDestino = livros.get(destino.titulo);

        if (livroOrigem != null && livroDestino != null) {
            Queue<Livro> recomendacoesOrigem = recomendacoes.getOrDefault(origem.titulo, new LinkedList<>());
            recomendacoesOrigem.add(destino);
            recomendacoes.put(origem.titulo, recomendacoesOrigem);

            Queue<Livro> recomendacoesDestino = recomendacoes.getOrDefault(destino.titulo, new LinkedList<>());
            recomendacoesDestino.add(origem);
            recomendacoes.put(destino.titulo, recomendacoesDestino);
        }
    }
    // Método para imprimir o grafo, mostrando as recomendações de cada livro
    public void imprimirGrafo() {
        for (String titulo : livros.keySet()) {
            System.out.print("O livro '" + titulo + "' recomenda: ");

            Queue<Livro> recomendacoesLivroAtual = recomendacoes.getOrDefault(titulo, new LinkedList<>());
            for (Livro livro : recomendacoesLivroAtual) {
                System.out.print("'" + livro.titulo + "'");

                if (!livro.equals(recomendacoesLivroAtual.peek()))
                    System.out.print(", ");
            }
            System.out.println();
        }
    }

}
class Usuario {
    String nome;
    Stack<Livro> historicoNavegacao;

    // Construtor da classe Usuario
    public Usuario (String nomeUsuario) {
        this.nome = nomeUsuario;
        this.historicoNavegacao = new Stack<>();
    }

    public void addLivroToHistorico(Livro livroToBeAdded) {
        historicoNavegacao.push(livroToBeAdded);
    }

    public void mostrarHistoricoNavegacao() {
        System.out.println("Mostrando o histórico de navegação: ");
        while (!this.historicoNavegacao.isEmpty()) {
            System.out.println(this.historicoNavegacao.pop().toString());
        }
    }
}

class Livro {
    String titulo;
    String autor;
    int anoDePublicacao;

    Map<String, Queue<Usuario>> listaEspera;

    // Construtor da classe Livro
    public Livro (String novoTitulo, String novoAutor, int anoPublicacao) {
        this.titulo = novoTitulo;
        this.autor = novoAutor;
        this.anoDePublicacao = anoPublicacao;
        this.listaEspera = new HashMap<>();
    }

    // Método toString para representar o livro como uma string
    @Override
    public String toString(){
        return titulo + "," + autor + "," + anoDePublicacao;
    }

    public void addUsuarioToListaEspera(Usuario usuarioToBeAdded){
        Queue<Usuario> filaEspera = listaEspera.getOrDefault(titulo, new LinkedList<>());
        filaEspera.add(usuarioToBeAdded);
        listaEspera.put(titulo, filaEspera);
    }

    public void mostrarListaEspera(){
        System.out.println("Mostrando a fila de espera: ");
        Queue<Usuario> filaEspera = listaEspera.getOrDefault(titulo, new LinkedList<>());
        for (Usuario usuario : filaEspera) {
            System.out.println(usuario.nome);
        }
    }
}

public class codHashMap {
    public static void main(String[] args) {
        // Inicializa o grafo de livros
        Grafo grafoDeLivros = new Grafo ();

        // Cria usuários
        Usuario userA = new Usuario("Lucas");
        Usuario userB = new Usuario("Jessica");
        Usuario userC = new Usuario("Beatriz");
        Usuario userD = new Usuario("Jairo");
        Usuario userE = new Usuario("André");
        Usuario userF = new Usuario("Roseli");

        // Cria livros
        Livro livro1 = new Livro("The last story", "Nancy Jooyoun", 2020);
        Livro livro2 = new Livro("Crescendo", "Becca Fitzpatrick", 2011);
        Livro livro3 = new Livro("Silencio","Becca Fitzpatrick", 2011);
        Livro livro4 = new Livro("Ninguém é de Ninguém","Zibia Gasparetto",2012);
        Livro livro5 = new Livro("Harry Potter e o Enigma do Principe", "J. K. Rowling", 2005);
        Livro livro6 = new Livro("A Culpa é das Estrelas", "John Green", 2010);
        Livro livro7 = new Livro("Pai Rico, Pai Pobre","Robert Kiyosaki", 1997);
        Livro livro8 = new Livro("Quem é você, Alasca","John Green",2011);
        Livro livro9 = new Livro("A Lista Negra","Jennifer Brown",2009);
        Livro livro10 = new Livro("Fallen","Lauren Kate",2009);


        // Adiciona usuários à lista de espera de alguns livros
        livro1.addUsuarioToListaEspera(userA);
        livro1.addUsuarioToListaEspera(userB);
        livro1.addUsuarioToListaEspera(userC);
        livro1.addUsuarioToListaEspera(userD);
        livro1.addUsuarioToListaEspera(userF);
        livro2.addUsuarioToListaEspera(userE);
        livro2.addUsuarioToListaEspera(userB);
        livro6.addUsuarioToListaEspera(userC);
        livro6.addUsuarioToListaEspera(userD);

        // Adiciona um livro ao histórico de navegação de um usuário
        userA.addLivroToHistorico(livro1);

        // Mostra a lista de espera de alguns livros e o histórico de navegação de um usuário
        livro1.mostrarListaEspera();
        livro2.mostrarListaEspera();
        livro6.mostrarListaEspera();
        userA.mostrarHistoricoNavegacao();


        // Adiciona todos os livros ao grafo
        grafoDeLivros.adicionarLivro(livro1);
        grafoDeLivros.adicionarLivro(livro2);
        grafoDeLivros.adicionarLivro(livro3);
        grafoDeLivros.adicionarLivro(livro4);
        grafoDeLivros.adicionarLivro(livro5);
        grafoDeLivros.adicionarLivro(livro6);
        grafoDeLivros.adicionarLivro(livro7);
        grafoDeLivros.adicionarLivro(livro8);
        grafoDeLivros.adicionarLivro(livro9);
        grafoDeLivros.adicionarLivro(livro10);

        // Adiciona recomendações entre alguns livros no grafo
        grafoDeLivros.adicionarRecomendacao(livro1, livro2);
        grafoDeLivros.adicionarRecomendacao(livro1, livro3);
        grafoDeLivros.adicionarRecomendacao(livro2, livro4);
        grafoDeLivros.adicionarRecomendacao(livro3, livro4);
        grafoDeLivros.adicionarRecomendacao(livro4, livro5);
        grafoDeLivros.adicionarRecomendacao(livro6, livro7);
        grafoDeLivros.adicionarRecomendacao(livro8, livro9);
        grafoDeLivros.adicionarRecomendacao(livro4, livro8);
        grafoDeLivros.adicionarRecomendacao(livro3, livro5);
        grafoDeLivros.adicionarRecomendacao(livro9, livro10);

        // Imprime o grafo, mostrando as recomendações de cada livro
        grafoDeLivros.imprimirGrafo();

        // Obtém um livro de origem para obter as recomendações
        Livro livroOrigem = livro1;
        Livro livroOrigem2 = livro2;

        // Encontra as recomendações com base na menor distância a partir do livro de origem
        List<Livro> recomendacoes = grafoDeLivros.encontrarCaminhoMaisCurto(livroOrigem, 5);
        List<Livro> recomendacoes2 = grafoDeLivros.encontrarCaminhoMaisCurto(livroOrigem2, 5);

        // Exibe as recomendações
        System.out.println("Recomendações com base na menor distância a partir de " + livroOrigem.titulo + ":");
        for (Livro livro : recomendacoes) {
            System.out.println("- " + livro.titulo);
        }
        System.out.println("\nRecomendações com base na menor distância a partir de " + livroOrigem2.titulo + ":");
        for (Livro livro : recomendacoes2) {
            System.out.println("- " + livro.titulo);
        }
    }
}
