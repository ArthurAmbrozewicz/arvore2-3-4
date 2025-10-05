# 🌳 Árvore 2-3-4

Este projeto implementa uma **árvore 2-3-4**, uma estrutura de dados balanceada utilizada para armazenamento eficiente e busca rápida de informações.  
O objetivo é demonstrar o funcionamento de operações básicas como **inserção**, **remoção**, **busca** e **balanceamento** automático da árvore.

---

## 📘 O que é uma Árvore 2-3-4?

A **árvore 2-3-4** é um tipo especial de **árvore B (de ordem 4)**.  
Cada nó pode conter:
- **2, 3 ou 4 filhos**, e
- **1, 2 ou 3 chaves**.

Ela mantém os dados **balanceados automaticamente**, garantindo tempo de busca **O(log n)**.  
É comumente usada em bancos de dados e sistemas de arquivos.

---

## ⚙️ Funcionalidades implementadas

- Inserção ordenada de valores  
- Divisão de nós cheios (split) durante a inserção  
- Remoção com balanceamento automático (merge e redistribuição)  
- Busca por valores  
- Impressão ou visualização da árvore em ordem  
- (Opcional) Interface visual para acompanhar as operações  

---

## 🧠 Operações principais

| Operação | Descrição |
|-----------|------------|
| **Inserir(valor)** | Insere um novo valor na posição correta, dividindo nós se necessário. |
| **Remover(valor)** | Remove um valor da árvore e realiza merges se o nó ficar abaixo da capacidade mínima. |
| **Buscar(valor)** | Retorna o nó onde o valor está armazenado, ou `null` se não encontrado. |
| **Imprimir()** | Exibe a árvore em ordem crescente. |

---
