#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

ifstream fin("graf.txt");

void afisare_mat(const vector<vector<int>>& graf){
    for(const auto& vect : graf) {
        for (const auto &el: vect)
            cout << el << ' ';
        cout << "\n";
    }
}

void afisare_list(const vector<vector<int>>& list,int n){
    for(int i=0;i<n;i++) {
        cout<<i<<": ";
        for (const auto &el: list[i])
            if(el!=-1)
                cout << el << ' ';
        cout << "\n";
    }
}

int main() {
    int n,m;
    fin>>n>>m;
    vector<vector<int>> adiacenta(n,vector<int>(n,0));
    vector<vector<int>> incidenta(n,vector<int>(m,0));
    vector<vector<int>> lista(n);
    for(int i=1;i<=m;i++){
        int x,y;
        fin>>x>>y;
        adiacenta[x][y]=1;
        adiacenta[y][x]=1;
    }

    //trecere de la matrice de adiacenta la matrice de incidenta
    int k=-1;
    for(int i=0;i<n;i++)
        for(int j=i+1;j<n;j++)
            if(adiacenta[i][j]==1){
                k++;
                incidenta[i][k]=1;
                incidenta[j][k]=1;
            }
    //afisare_mat(incidenta);

    //trecere de la matrice de adiacenta la lista de adiacenta
    for(int i=0;i<n;i++)
        for(int j=0;j<n;j++)
            if(adiacenta[i][j]==1)
                lista[i].push_back(j);

    //trecerea de la matrice de incidenta la matrice de adiacenta
    vector<vector<int>> adiacenta2(n,vector<int>(n,0));
    for(int j=0;j<m;j++){
        int x=-1,y;
        for(int i=0;i<n;i++) {
            if (incidenta[i][j] == 1 and x == -1) {
                x = i;
            }
            if (incidenta[i][j] == 1) {
                y = i;
            }
        }
        adiacenta2[x][y]=1;
        adiacenta2[y][x]=1;
    }

    //trecere de la matrice de incidenta la lista de adiacenta
    vector<vector<int>> lista2(n);
    for(int j=0;j<m;j++){
        int x=-1,y;
        for(int i=0;i<n;i++) {
            if (incidenta[i][j] == 1 and x == -1) {
                x = i;
            }
            if (incidenta[i][j] == 1) {
                y = i;
            }
        }
        lista2[x].push_back(y);
        lista2[y].push_back(x);
    }

    //trecere de la lista de adiacenta la matrice de adiacenta
    vector<vector<int>> adiacenta3(n,vector<int>(n,0));
    for(int i=0;i<n;i++)
        for(const auto& j : lista[i])
            adiacenta3[i][j]=1,adiacenta[j][i]=1;

    //trecere de la lista de adiacenta la matrice de incidenta
    vector<vector<int>> incidenta2(n,vector<int>(m,0));
    k=-1;
    for(int i=0;i<n;i++)
        for(const auto& j : lista[i])
            if(i>j)
                k++,incidenta2[i][k]=1,incidenta2[j][k]=1;
    return 0;
}
