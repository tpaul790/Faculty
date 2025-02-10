#include <fstream>
#include <queue>
#include <iostream>

using namespace std;

ifstream fin("graf.txt");

void reset_vector(int v[],int n){
    for(int i=1;i<=n;i++)
        v[i]=0;
}

void DFS(int a[][101],int v[],int n,int nod){
    cout<<nod<<' ';
    v[nod]=1;
    for(int i=1;i<=n;i++)
        if(v[i]==0 && a[nod][i]==1)
            DFS(a,v,n,i);
}

void BFS(int a[][101],int v[],int n,int nod){
    queue<int> q;
    v[nod]=1;
    q.push(nod);
    while(!q.empty()){
        int u=q.front();
        cout<<u<<' ';
        q.pop();
        for(int i=1;i<=n;i++)
            if(v[i]==0 && a[u][i]==1){
                q.push(i);
                v[i]=1;
            }
    }
}

void inchidere_tranzitiva(int a[][101],int n){
    for(int i=1;i<=n;i++)
        a[i][i]=1;
    for(int i=1;i<=n;i++) {
        for (int j = 1; j <= n; j++)
            for (int k = 1; k <= n; k++)
                if (a[i][j] == 0 && a[k][j] == 1 && a[i][k] == 1)
                    a[i][j] = 1;
    }
    for(int i=1;i<=n;i++,cout<<endl)
        for(int j=1;j<=n;j++)
            cout<<a[i][j]<<' ';
}

void Moore(int a[][101],int v[],int p[], int d[], int n, int nod){
    queue<int> q;
    for(int i=1;i<=n;i++)
        if(i!=nod)
            d[i]=100000;
    q.push(nod);
    v[nod]=1;
    while(!q.empty()){
        int u=q.front();
        q.pop();
        for(int i=1;i<=n;i++)
            if(!v[i] and a[i][u]==1 and d[i]==100000){
                q.push(i);
                v[i]=1;
                p[i]=u;
                d[i]=d[u]+1;
            }
    }
}

int main(){
    int a[101][101],v[101],p[101],d[101],x,n;
    int tr[101][101];
    fin>>n>>x;
    int z,y;
    while(fin>>z>>y){
        fin>>z>>y;
        a[z][y]=a[y][z]=1;
        tr[z][y]=1;
    }
    //dfs
    cout<<"DFS\n";
    reset_vector(v,n);
    cout<<"Nordurile parcurse prin dfs de la nodul "<<x<<" sunt: ";
    DFS(a,v,n,x);
    cout<<endl;
    cout<<endl;

    //bfs
    cout<<"BFS\n";
    reset_vector(v,n);
    cout<<"Nordurile parcurse prin bfs de la nodul "<<x<<" sunt: ";
    BFS(a,v,n,x);
    cout<<endl;

    //inchiderea tranzitiva
    cout<<endl;
    cout<<"MATRICE TRANZITIVA\n";
    inchidere_tranzitiva(tr,n);
    cout<<endl;

    //algoritmul lui Moore
    cout<<"MOORE\n";
    reset_vector(v,n);
    reset_vector(p,n);
    reset_vector(d,n);
    Moore(a,v,p,d,n,x);
    cout<<"Vectorul de parinti este: ";
    for(int i=1;i<=n;i++)
        cout<<p[i]<<' ';
    cout<<endl;
    cout<<"Vectorul de distante pornind de la nodul "<<x<<" este: ";
    for(int i=1;i<=n;i++)
        cout<<d[i]<<' ';
    return 0;
}