#include <iostream>
#include <queue>
#include <vector>
#define inf INT_MAX
using namespace std;
typedef pair<int ,int> per;

bool bellman_ford(const vector<vector<per>>& graf,vector<int>& dist, const int& s){
    int n=graf.size();
    for(int i = 0 ; i < n ; i++)
        dist[i]=inf;
    dist[s]=0;


    for(int i=0;i<n;i++)
        for(int u=0;u<n;u++)
            for(auto muchie : graf[u]){
                int v=muchie.first;
                int w=muchie.second;
                if(dist[u]!=inf and dist[v]>dist[u]+w)
                    dist[v]=dist[u]+w;
            }
    for(int u=0;u<n;u++)
        for(auto muchie : graf[u]){
            int v=muchie.first;
            int w=muchie.second;
            if(dist[u]!=inf and dist[v]>dist[u]+w)
                return false;
        }
    return true;

}

void dijkstra(const vector<vector<per>>& graf,vector<int>& dist ,const int& s){
    unsigned int n=graf.size();
    for(int i = 0 ; i < n ; i++)
        dist[i]=inf;
    dist[s]=0;
    priority_queue<per,vector<per>,greater<per>> pq;
    pq.push({0,s});
    while(!pq.empty()){
        int u=pq.top().second;
        int d=pq.top().first;
        pq.pop();
        if(d>dist[u])
            continue;

        for(auto& muchie : graf[u]){
            int v=muchie.first;
            int w=muchie.second;
            if(dist[v]>dist[u]+w) {
                dist[muchie.first] = dist[u] + w;
                pq.push({dist[v],v});
            }
        }

    }
}

vector<vector<int>> jhonson(vector<vector<per>>& graf){
    int n=graf.size();
    vector<vector<per>> graf_modif(n+1);
    for(int u = 0; u < n; u++)
        for(auto p : graf[u]){
            graf_modif[u].push_back(p);
            graf_modif[n].push_back({u,0});
        }
    vector<int> h(n+1);
    if(!bellman_ford(graf_modif,h,n))
        return {};

    for(int u=0;u<n;u++)
        for(auto& muchie : graf[u]){
            int v=muchie.first;
            int& w=muchie.second;
            w=w+h[u]-h[v];
        }
    vector<vector<int>> drum_min(n,vector<int>(n,inf));
    for(int u = 0; u < n; u++){
        vector<int> dist(n);
        dijkstra(graf,dist,u);
        for(int i = 0; i < n; i++)
            if(dist[i]!=inf)
                drum_min[u][i]=dist[i]+h[i]-h[u];
    }
    return drum_min;
}

int main() {
    int n, m;
    cin >> n >> m;
    vector<vector<per>> graf(n);
    for(int i = 0; i < m; i++){
        int x, y, w;
        cin >> x >> y >> w;
        graf[x].push_back({y,w});
    }

    vector<vector<int>> drum_min= jhonson(graf);
    //parcurg toate muchiile din graf si le afisez ( dupa ce sa apelat jhonson, in graf sunt muchiile reponderate
    for(int u=0;u<n;u++) {
        for (auto &muchie: graf[u]) {
            int v = muchie.first;
            int w = muchie.second;
            cout << u << ' ' << v << ' ' << w << "\n";
        }
    }

    for(auto linie : drum_min) {
        for(int i =0 ; i < n ; i++)
            if(linie[i]==inf)
                cout<<"inf"<<" ";
            else
                cout<<linie[i]<<' ';
        cout<<'\n';
    }
    return 0;
}

/*
3 4
0 2 6
1 0 -2
1 2 5
2 0 -3



5 7 0
0 2 4
0 3 1
1 0 1
1 2 4
2 0 1
2 3 1
3 4 1
 */
