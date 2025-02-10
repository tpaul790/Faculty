#include <fstream>
#include <iostream>
#include <queue>

using namespace std;

constexpr int inf=1000;
constexpr int nil=-1;

struct edge{
    int i;
    int j;
    int w;
}eds[101];

struct dist{
    int d;
    int p;
};

bool Bellman_Ford(dist d[],int n,int s){
    for(int i=0;i<n;i++)
        d[i].d=inf,d[i].p=nil;
    d[s].d=0;

    for(int i=0;i<n-1;i++) {
        for (edge e: eds)
            if (d[e.j].d>d[e.i].d+e.w){
                d[e.j].d=d[e.i].d+e.w;
                d[e.j].p=e.i;
            }
    }
    for (edge e: eds) {
        if (d[e.j].d > d[e.i].d + e.w)
            return false;
    }
    return true;
}

int get_min(dist d[],bool v[],int n){
    int min=inf,vf_min;
    for(int i=0;i<n;i++)
        if(d[i].d<min and !v[i])
            min=d[i].d,vf_min=i;
    return vf_min;
}


int main(int argc,char** argv){
    if(argc!=3)
        cout<<"Argumente insuficiente";
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    int n,m,s;
    fin>>n>>m>>s;
    int k=-1;
    while(m){
        int i,j,w;
        fin>>i>>j>>w;
        eds[++k].i=i;
        eds[k].j=j;
        eds[k].w=w;
        m--;
    }
    dist d[101];
    int v[101];
    bool ok=Bellman_Ford(d,n,s);
    if(!ok) {
        cout << "Ciclu infinit";
        return 0;
    }
    for(int i=0;i<n;i++)
        if(d[i].d==inf)
            fout<<"inf"<<' ';
        else
            fout<<d[i].d<<' ';
    return 0;
}