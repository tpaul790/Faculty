#include <iostream>
#include <fstream>
#include <vector>
#include <climits>
#include <algorithm>
#include <queue>

void pompare(int u, int v, std::vector<std::vector<int>>& gr, std::vector<int>& e) {
    int delta = std::min(e[u], gr[u][v]);
    e[u] -= delta;
    e[v] += delta;
    gr[u][v] -= delta;
    gr[v][u] += delta;
}

void inaltare(int u, std::vector<std::vector<int>>& gr, std::vector<int>& h) {
    int minh = INT_MAX;
    for (int i = 0; i < gr.size(); i++) {
        if (gr[u][i] != 0)
            minh = std::min(minh, h[i]);
    }
    h[u] = 1 + minh;
}

void descarcare(int u, std::vector<std::vector<int>>& gr, std::vector<int>& h, std::vector<int>& e, std::vector<std::queue<int>>& l) {
    auto vecini = l[u];
    while (e[u] > 0) {
        if (vecini.empty()) {
            inaltare(u, gr, h);
            vecini = l[u];
        }
        else {
            int v = vecini.front();
            vecini.pop();
            if (gr[u][v] > 0 && h[u] == h[v] + 1) {
                pompare(u, v, gr, e);
            }
        }
    }
}

int main() {
    std::ifstream fin("input.txt");
    std::ofstream fout("output.txt");

    int n, m;
    fin >> n >> m;
    std::vector<std::vector<int>> gr(n,std::vector<int>(n,0));
    std::vector<std::queue<int>> l;
    l.resize(n);
    for (int i = 0; i < m; i++) {
        int x, y, c;
        fin >> x >> y >> c;
        l[x].push(y);
        l[y].push(x);
        gr[x][y] = c;
    }
    std::vector<int> h(n,0), e(n,0);
    h[0] = n;
    for (int i = 0; i < n; i++) {
        if (gr[0][i] != 0) {
            e[i] = gr[0][i];
            e[0] = e[0] - gr[0][i];
            gr[i][0] = gr[0][i];
            gr[0][i] = 0;
        }
    }
    std::queue<int> q;
    for (int i = 1; i < n - 1; i++)
        q.push(i);

    while (!q.empty()) {
        int x = q.front();
        q.pop();
        int h_vechi = h[x];
        descarcare(x, gr, h, e, l);
        if (h[x] > h_vechi)
            q.push(x);
    }
    fout << e[n - 1];
    fin.close();
    fout.close();
    return 0;
}