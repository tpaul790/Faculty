#include <fstream>
#include <vector>
#include <string>
#include <queue>


using namespace std;

ifstream fin("input.txt");
ofstream fout("output.txt");

bool bfs(const vector<vector<int>>& graf,vector<int>& parinte,int s,int t){
    auto n = graf.size();
    vector<bool> vizitat(n,false);

    queue<int> q;
    q.push(s);
    vizitat[s]= true;
    parinte[s]=-1;
    while(!q.empty()){
        auto u = q.front();
        q.pop();

        for(int i=0;i<n;i++)
            if(!vizitat[i] and graf[u][i]>0) {
                q.push(i);
                parinte[i]= u;
                vizitat[i]= true;
            }
    }
    return vizitat[t];
}

int FordFulkerson(const vector<vector<int>>& graf,int s, int t){
    vector<vector<int>> graf_r=graf;
    int n = graf.size();
    int flux_max = 0;
    vector<int> parinte(n);
    while(bfs(graf_r,parinte,s,t)){
        int v = t;
        int cost_min=INT_MAX;
        while(v != s){
            int u = parinte[v];
            cost_min = min(cost_min,graf_r[u][v]);
            v = parinte[v];
        }

        v = t;
        while(v != s){
            int u = parinte[v];
            graf_r[u][v]-=cost_min;
            graf_r[v][u]+=cost_min;
            v = parinte[v];
        }
        flux_max+=cost_min;
    }

    return flux_max;
}

int main() {
    int n,m,e;
    fin>>n>>m>>e;
    vector<vector<int>> graf(n+m+1,vector<int>(n+m+1,0));
    for(int i=1;i<=e;i++){
        int x,y;
        fin>>x>>y;
        graf[x][y+n]=1;
    }
    for(int i=1;i<n;i++)
        graf[0][i] = 1;
    for(int i=n=1;i<n+m+1;i++)
        graf[i][n+m+1] = 1;
    fout<<FordFulkerson(graf,0,n+m+1);
    return 0;
}
