#include <iostream>
#include <vector>
#include <queue>
#define inf INT_MAX

using namespace std;

typedef pair<int,int> per;

vector<int> dijkstra(const vector<vector<per>>& graf,int start){
    unsigned int n=graf.size();
    vector<int> dist(n,inf);
    dist[start]=0;
    vector<int> parinte(n,-1);

    priority_queue<per,vector<per>,greater<per>> pq;

    pq.push({0,start});
    while(!pq.empty()){
        int u = pq.top().second;
        int d = pq.top().first;
        pq.pop();

        if(d>dist[u])
            continue;

        for(const auto& muchie : graf[u]){
            int v = muchie.first;
            int w = muchie.second;
            if(dist[v]>dist[u]+w){
                dist[v]=dist[u]+w;
                parinte[v]=u;
                pq.push({dist[v],v});
            }
        }
    }
    return parinte;
}

int main() {
    int a,b;
    cin>>a>>b;
    int n,m;
    cin>>n>>m;
    vector<vector<per>> graf(n);
    while(m){
        int i,j,w;
        cin>>i>>j>>w;
        graf[i].push_back({j,w});
        m--;
    }
    vector<int> parinte = dijkstra(graf,a);
    vector<int> lant;
    while(parinte[b]!=-1){
        lant.push_back(b);
        b=parinte[b];
    }
    lant.push_back(a);
    reverse(lant.begin(), lant.end());
    cout<<"Drumul minim este: ";
    for(const auto& el : lant)
        cout<<el<<' ';
    return 0;
}
/*
1 5
6 9
1 2 7
1 3 9
1 6 14
2 3 10
2 4 15
3 6 2
3 4 11
4 5 6
6 5 9
 */
