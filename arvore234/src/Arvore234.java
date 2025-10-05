public class Arvore234 {

    static final int T = 2;
    static final int MAX_CHAVES = 3;
    static final int MAX_FILHOS = 4;

    static class No {
        int qtdChaves; // número de chaves
        int[] chaves = new int[MAX_CHAVES];
        No[] filhos = new No[MAX_FILHOS];
        boolean folha = true;

        No() { qtdChaves = 0; }
    }

    private No raiz;

    public Arvore234() { raiz = null; }

    // Busca iterativa
    public boolean buscar(int valor) {
        return buscar(raiz, valor) != null;
    }

    private No buscar(No no, int valor) {
        if (no == null) return null;
        int i = 0;
        while (i < no.qtdChaves && valor > no.chaves[i]) i++;
        if (i < no.qtdChaves && valor == no.chaves[i]) return no;
        if (no.folha) return null;
        return buscar(no.filhos[i], valor);
    }

    // Inserção
    public void inserir(int valor) {
        if (raiz == null) {
            raiz = new No();
            raiz.chaves[0] = valor;
            raiz.qtdChaves = 1;
            return;
        }
        if (raiz.qtdChaves == MAX_CHAVES) {
            No novo = new No();
            novo.folha = false;
            novo.filhos[0] = raiz;
            dividirFilho(novo, 0);
            raiz = novo;
            inserirNaoCheio(novo, valor);
        } else {
            inserirNaoCheio(raiz, valor);
        }
    }

    private void dividirFilho(No pai, int indice) {
        No cheio = pai.filhos[indice]; // nó cheio (3 chaves)
        No novo = new No();
        novo.folha = cheio.folha;
        novo.qtdChaves = T - 1; // 1 (t-1)

        // copia as últimas t-1 chaves de cheio para novo
        for (int j = 0; j < T - 1; j++) {
            novo.chaves[j] = cheio.chaves[j + T];
        }

        // se não for folha, copiar os últimos t filhos
        if (!cheio.folha) {
            for (int j = 0; j < T; j++) {
                novo.filhos[j] = cheio.filhos[j + T];
            }
        }

        cheio.qtdChaves = T - 1; // reduz cheio para t-1 chaves

        // inserir novo como filho do pai
        for (int j = pai.qtdChaves; j >= indice + 1; j--) {
            pai.filhos[j + 1] = pai.filhos[j];
        }
        pai.filhos[indice + 1] = novo;

        // mover chave do meio de cheio para pai
        for (int j = pai.qtdChaves - 1; j >= indice; j--) {
            pai.chaves[j + 1] = pai.chaves[j];
        }
        pai.chaves[indice] = cheio.chaves[T - 1];
        pai.qtdChaves += 1;
    }

    private void inserirNaoCheio(No no, int valor) {
        int i = no.qtdChaves - 1;
        if (no.folha) {
            // inserir na posição correta dentro de chaves
            while (i >= 0 && valor < no.chaves[i]) {
                no.chaves[i + 1] = no.chaves[i];
                i--;
            }
            no.chaves[i + 1] = valor;
            no.qtdChaves += 1;
        } else {
            while (i >= 0 && valor < no.chaves[i]) i--;
            i++;
            if (no.filhos[i].qtdChaves == MAX_CHAVES) {
                dividirFilho(no, i);
                if (valor > no.chaves[i]) i++;
            }
            inserirNaoCheio(no.filhos[i], valor);
        }
    }


    public void remover(int valor) {
        if (raiz == null) return;
        remover(raiz, valor);
        if (raiz.qtdChaves == 0) {
            if (!raiz.folha) raiz = raiz.filhos[0];
            else raiz = null;
        }
    }

    private void remover(No no, int valor) {
        int indice = encontrarIndiceChave(no, valor);

        if (indice < no.qtdChaves && no.chaves[indice] == valor) {
            if (no.folha) {
                // caso 1: chave está em uma folha
                for (int i = indice + 1; i < no.qtdChaves; i++) no.chaves[i - 1] = no.chaves[i];
                no.qtdChaves--;
            } else {
                // caso 2: chave está em nó interno
                No filhoEsq = no.filhos[indice];
                No filhoDir = no.filhos[indice + 1];
                if (filhoEsq.qtdChaves >= T) {
                    int predecessor = obterPredecessor(filhoEsq);
                    no.chaves[indice] = predecessor;
                    remover(filhoEsq, predecessor);
                } else if (filhoDir.qtdChaves >= T) {
                    int sucessor = obterSucessor(filhoDir);
                    no.chaves[indice] = sucessor;
                    remover(filhoDir, sucessor);
                } else {
                    // ambos têm t-1 chaves -> merge
                    mesclar(no, indice);
                    remover(filhoEsq, valor); // agora filhoEsq contém chave combinada
                }
            }
        } else {
            // chave não está presente neste nó
            if (no.folha) {
                return; // não encontrada
            }
            boolean flag = (indice == no.qtdChaves);
            No filho = no.filhos[indice];
            if (filho.qtdChaves == T - 1) {
                preencher(no, indice);
            }
            if (flag && indice > no.qtdChaves) {
                remover(no.filhos[indice - 1], valor);
            } else {
                remover(no.filhos[indice], valor);
            }
        }
    }

    private int encontrarIndiceChave(No no, int valor) {
        int indice = 0;
        while (indice < no.qtdChaves && no.chaves[indice] < valor) indice++;
        return indice;
    }

    private int obterPredecessor(No no) {
        No atual = no;
        while (!atual.folha) atual = atual.filhos[atual.qtdChaves];
        return atual.chaves[atual.qtdChaves - 1];
    }

    private int obterSucessor(No no) {
        No atual = no;
        while (!atual.folha) atual = atual.filhos[0];
        return atual.chaves[0];
    }

    private void preencher(No no, int indice) {
        if (indice != 0 && no.filhos[indice - 1].qtdChaves >= T) {
            emprestarDoAnterior(no, indice);
        } else if (indice != no.qtdChaves && no.filhos[indice + 1].qtdChaves >= T) {
            emprestarDoProximo(no, indice);
        } else {
            if (indice != no.qtdChaves) mesclar(no, indice);
            else mesclar(no, indice - 1);
        }
    }

    private void emprestarDoAnterior(No no, int indice) {
        No filho = no.filhos[indice];
        No irmao = no.filhos[indice - 1];

        for (int i = filho.qtdChaves - 1; i >= 0; i--) filho.chaves[i + 1] = filho.chaves[i];
        if (!filho.folha) {
            for (int i = filho.qtdChaves; i >= 0; i--) filho.filhos[i + 1] = filho.filhos[i];
        }
        filho.chaves[0] = no.chaves[indice - 1];
        if (!filho.folha) filho.filhos[0] = irmao.filhos[irmao.qtdChaves];
        no.chaves[indice - 1] = irmao.chaves[irmao.qtdChaves - 1];
        filho.qtdChaves += 1;
        irmao.qtdChaves -= 1;
    }

    private void emprestarDoProximo(No no, int indice) {
        No filho = no.filhos[indice];
        No irmao = no.filhos[indice + 1];

        filho.chaves[filho.qtdChaves] = no.chaves[indice];
        if (!filho.folha) filho.filhos[filho.qtdChaves + 1] = irmao.filhos[0];
        no.chaves[indice] = irmao.chaves[0];
        for (int i = 1; i < irmao.qtdChaves; i++) irmao.chaves[i - 1] = irmao.chaves[i];
        if (!irmao.folha) {
            for (int i = 1; i <= irmao.qtdChaves; i++) irmao.filhos[i - 1] = irmao.filhos[i];
        }
        filho.qtdChaves += 1;
        irmao.qtdChaves -= 1;
    }

    private void mesclar(No no, int indice) {
        No filho = no.filhos[indice];
        No irmao = no.filhos[indice + 1];

        filho.chaves[T - 1] = no.chaves[indice];
        for (int i = 0; i < irmao.qtdChaves; i++) filho.chaves[i + T] = irmao.chaves[i];
        if (!filho.folha) {
            for (int i = 0; i <= irmao.qtdChaves; i++) filho.filhos[i + T] = irmao.filhos[i];
        }
        for (int i = indice + 1; i < no.qtdChaves; i++) no.chaves[i - 1] = no.chaves[i];
        for (int i = indice + 2; i <= no.qtdChaves; i++) no.filhos[i - 1] = no.filhos[i];

        filho.qtdChaves += irmao.qtdChaves + 1;
        no.qtdChaves--;
    }

    // Impressão simples por níveis
    public void imprimir() {
        imprimirRec(raiz, 0);
    }

    private void imprimirRec(No no, int nivel) {
        if (no == null) return;
        System.out.print("[nível " + nivel + "] ");
        System.out.print("chaves: ");
        for (int i = 0; i < no.qtdChaves; i++) System.out.print(no.chaves[i] + " ");
        System.out.println();
        if (!no.folha) {
            for (int i = 0; i <= no.qtdChaves; i++) imprimirRec(no.filhos[i], nivel + 1);
        }
    }


    public static void main(String[] args) {
        Arvore234 arvore = new Arvore234();
        int[] valores = {50, 40, 60, 30, 20, 10, 70, 80, 90, 85, 75, 65, 55};
        for (int v : valores) {
            System.out.println("Inserindo: " + v);
            arvore.inserir(v);
            arvore.imprimir();
            System.out.println("---------------------------");
        }

        System.out.println("Busca 60: " + arvore.buscar(60));
        System.out.println("Busca 99: " + arvore.buscar(99));

        int[] remov = {50, 30, 70, 85, 20, 10, 60};
        for (int r : remov) {
            System.out.println("Removendo: " + r);
            arvore.remover(r);
            arvore.imprimir();
            System.out.println("---------------------------");
        }
    }
}