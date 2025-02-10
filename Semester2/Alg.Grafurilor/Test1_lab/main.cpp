#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
#include <cmath>
#define inf 1000

using namespace std;

ifstream fin("graf.txt");

void dijkstra(const vector<vector<pair<int,float>>>& graf,vector<float>& dist ,const int& s){
    dist[s]=0;
    priority_queue<pair<float,int>,vector<pair<float,int>>,greater<pair<float,int>>> pq;
    pq.push({0,s});
    while(!pq.empty()){
        int u=pq.top().second;
        float d=pq.top().first;
        pq.pop();
        if(d>dist[u])
            continue;

        for(auto& muchie : graf[u]){
            int v=muchie.first;
            float w=muchie.second;
            if(dist[v]>dist[u]+w) {
                dist[muchie.first] = dist[u] + w;
                pq.push({dist[v],v});
            }
        }

    }
}

double pondere(int x1, int y1, int x2, int y2){
    double pondere = sqrt(pow(x2-x1, 2) + pow(y2-y1, 2));
    return pondere;
}

int main(){
    vector<pair<int, int>> coord;
    int n, m, s, dest;
    cout << "Introduceti sursa si destinatia: ";
    cin >> s >> dest;
    fin >> n >> m;
    vector<vector<pair<int,float>>> graf(n+1);
    vector<float> dist(n+1,inf);
    int x, y;
    for (int i = 0; i < n; i++){
        fin >> x >> y;
        coord.push_back({x,y});
    }
    int u,v;
    for(int i=0;i<m;i++){
        fin>>u>>v;
        graf[u].push_back({v, pondere(coord[u-1].first,coord[u-1].second,coord[v-1].first,coord[v-1].second)});
    }
    dijkstra(graf,dist,s);
    cout<<dist[dest];
}