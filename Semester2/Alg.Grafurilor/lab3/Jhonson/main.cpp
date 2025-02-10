#include <iostream>
#include <vector>
#include <queue>
#include <fstream>

using namespace std;

#define inf INT_MAX

bool Bellman_Ford(const vector<vector<int>>& graf,vector<int>& dist){
    unsigned int n=graf.size();
    for(int i=0;i<n;i++)
        dist[i]=inf;
    dist[n-1]=0;
    for(int k=0;k<n-1;k++)
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                if(graf[i][j]!=inf and dist[i]!=inf and dist[j]>dist[i]+graf[i][j])
                    dist[j]=dist[i]+graf[i][j];
    for(int i=0;i<n;i++)
        for(int j=0;j<n;j++)
            if(graf[i][j]!=inf and dist[i]!=inf and dist[j]>dist[i]+graf[i][j])
                return false;
    return true;
}

void Dijkstra(const vector<vector<int>>& graf,vector<int>& dist,const int& s){
    unsigned int n = graf.size();
    for(int i=0;i<n;i++)
        dist[i]=inf;
    dist[s]=0;
    priority_queue<pair<int,int>,vector<pair<int,int>>,greater<pair<int,int>>> pq;
    pq.push({0,s});
    while(!pq.empty()){
        int u=pq.top().second;
        int d=pq.top().first;
        pq.pop();

        if (d > dist[u]) continue;

        for(int i=0;i<n;i++)
            if(graf[u][i]!=inf and dist[i]>dist[u]+graf[u][i]){
                dist[i]=dist[u]+graf[u][i];
                pq.push({dist[i],i});
            }
    }
}

vector<vector<int>> Jhonson(vector<vector<int>>& graf,vector<vector<int>>& ponderare){
    unsigned int n=graf.size();

    //rezerv spatiu n+1 pentru nodul sursa al lui Bellman Ford
    vector<vector<int>> graf_modif(n+1,vector<int>(n+1,inf));

    //compiez din graful meu tot inafara de a modifica nodul sursa a lui Bellman Ford
    for(int i=0;i<n;i++)
        for(int j=0;j<n;j++)
            graf_modif[i][j]=graf[i][j];
    //trag muchiile de la nodul sursa la toate celelalte
    for(int i=0;i<n;i++)
        graf_modif[n][i]=0;

    //aplic bellman ford pentru ultimul nod care este legat de toate celelalte prin noduri de pondere 0
    vector<int> h(n+1);
    if(!Bellman_Ford(graf_modif,h)){
        cout<<"S-a detectat un ciclu infinit\n";
        return {};
    }
    //daca am trecut inseamana ca a mers bellman ford si pot face reponderarea
    for(int i=0;i<n;i++)
        for(int j=0;j<n;j++)
            if(graf[i][j]!=inf)
                graf[i][j]+=(h[i]-h[j]),ponderare[i][j]=graf[i][j];

    //aplic dijkstra pentru fiecare nod din graf
    vector<vector<int>> drumuri_min(n,vector<int>(n,inf));
    for(int i=0;i<n;i++){
        vector<int> dist(n);
        Dijkstra(graf,dist,i);
        //operatia inversa reponderarii
        for(int j=0;j<n;j++)
            if(dist[j]!=inf)
                drumuri_min[i][j]=dist[j]+h[j]-h[i];
    }
    return drumuri_min;
}

int main(int argc,char** argv) {
    if(argc!=3) {
        cout << "Numarul insuficient de argumente\n";
        return 0;
    }
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int V,E;
    fin>>V>>E;
    vector<vector<int>> graf(V,vector<int>(V,inf));
    while(E){
        int i,j,w;
        fin>>i>>j>>w;
        graf[i][j]=w;
        E--;
    }
    vector<int> dist(V);
    vector<vector<int>> ponderare(V,vector<int>(V,inf));
    vector<vector<int>> drumuri_min= Jhonson(graf,ponderare);
    if(!drumuri_min.empty()) {
        for (int i = 0; i < V; i++)
            for (int j = 0; j < V; j++)
                if (ponderare[i][j] != inf)
                    fout << i << ' ' << j << ' ' << ponderare[i][j] << "\n";

        for (const auto &vect: drumuri_min) {
            for (const auto &el: vect) {
                if (el == inf)
                    fout << "inf" << ' ';
                else
                    fout << el << ' ';
            }
            fout << "\n";
        }
    }else
        fout<<-1;
    return 0;
}
/*
4 5
0 1 5
1 2 -3
2 0 -1
2 3 7
3 1 -5
 */