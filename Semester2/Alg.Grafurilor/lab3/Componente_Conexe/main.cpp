#include <iostream>
#include <vector>

using namespace std;

typedef pair<int,int> per;

void dfs(const vector<vector<int>>& graf,vector<bool>& v,int nod){
    v[nod]= true;
    for(const auto& u : graf[nod])
        if(!v[u])
            dfs(graf,v,u);
}

int main() {
    int n,m;
    cin>>n>>m;
    vector<vector<int>> graf(n);
    for(int i=1;i<=m;i++){
        int u,v;
        cin>>u>>v;
        graf[u].push_back(v);
        graf[v].push_back(u);
    }
    vector<bool> v(n, false);
    int nr_componente=0;
    for(int i=1;i<=n;i++){
        if(!v[i]){
            nr_componente++;
            dfs(graf,v,i);
        }
    }
    cout<<"Numarul de componente conexe este: "<<nr_componente;
    return 0;
}
/*
4 2
1 2
2 3
 */
