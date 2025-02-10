#include <iostream>
#include <fstream>

using namespace std;

ifstream fin("in.txt");

int main(){
    //EX1q
    //declarari
    int n,m=0;
    int ma[101][101],mi[101][101],la[101][101];
    fin>>n;
    int i=0,j=0;

    //construire matricile
    while(fin>>i>>j){
        m++;
        ma[i][j]=ma[j][i]=1;
        mi[i][m]=mi[j][m]=1;
    }
    for(int i=1;i<=n;i++){
        int poz=1;
        for(int j=1;j<=n;j++)
            if(ma[i][j]==1)
                la[i][poz++]=j;
    }
            
    //afisare matrici
    cout<<"Matricea de adiacenta:"<<endl;
    for(int i=1;i<=n;i++,cout<<endl)
        for(int j=1;j<=n;j++)
            cout<<ma[i][j]<<' ';
    cout<<endl;
    cout<<"Matricea de incidenta:"<<endl;
    for(int i=1;i<=n;i++,cout<<endl)
        for(int j=1;j<=m;j++)
            cout<<mi[i][j]<<' ';
    cout<<endl;
    cout<<"Lista de adiacenta:"<<endl;
    for(int i=1;i<=n;i++,cout<<endl){
        cout<<i<<": ";
        for(int j=1;j<=n;j++)
            if(la[i][j]!=0)
                cout<<la[i][j]<<' ';
    }
        
    cout<<endl;

    //EX2
    //a) Nodurile izolate
    bool exista=false;
    int ni[101],k=0;
    for(int i=1;i<=n;i++){
        bool ok=true;
        for(int j=1;j<=n;j++)
            if(la[i][j]!=0)
                ok=false;
        if(ok==true){
            exista=true;
            ni[++k]=i;
        }
    }
    if(!exista)
        cout<<"Nu exista noduri izolate";
    else{
        cout<<"Nodurile izolate sunt:";
        for(int i=1;i<=k;i++)
            cout<<ni[i]<<' ';
    }
    cout<<endl<<endl;

    //b)este graful regular?
    int one_sum=-1;
    bool reg=true;
    for(int i=1;i<=n;i++){
        int sum=0;
        for(int j=1;j<=n;j++)
            sum+=ma[i][j];
        if(one_sum==-1)
            one_sum=sum;
        else if(one_sum!=sum)
            reg=false;
    }
    if(reg)
        cout<<"Graful este regulat";
    else
        cout<<"Graful nu este regulat";

    cout<<endl<<endl;

    //c) Matricea distantelor
    cout<<"Matricea distantelor:"<<endl;
    for(int k=1;k<=n;k++)
        for(int i=1;i<=n;i++)
            for(int j=1;j<=n;j++)
                if(ma[i][j]!=0 && ma[i][k]!=0 && ma[k][j]!=0)
                    ma[i][j]=min(ma[i][j],ma[i][k]+ma[k][j]);
                else if(ma[i][k]!=0 && ma[k][j]!=0 && i!=j)
                     ma[i][j]=ma[i][k]+ma[k][j];
    for(int i=1;i<=n;i++,cout<<endl)
        for(int j=1;j<=n;j++)
            cout<<ma[i][j]<<' ';
    cout<<endl<<endl;

    //d) conexitate
    bool conex=true;
    for(int i=1;i<=n;i++)
        for(int j=1;j<i;j++)
            if(ma[i][j]==0)
                conex=false;
    if(conex)
        cout<<"Graful este conex";
    else
        cout<<"Graful nu este conex";
    cout<<endl;
    return 0;
}