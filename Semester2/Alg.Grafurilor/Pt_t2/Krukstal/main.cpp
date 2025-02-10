#include <fstream>
#include <iostream>
#include <algorithm>
#include <vector>
using namespace std;

ifstream fin("input.txt");
ofstream fout("output.txt");

struct muchie{
    int x,y,w;
};

class Graf{
private:
    vector<muchie> muchii;
    vector<int> set;
    int V,E;
public:
    /*
     * Constructor
     * param v: reprezinta numarul de varfuri din graf
     * param e: reprezinta numarul de muchii din graf
     */
    Graf(int v, int e) : V{ v },E{ e }{
        set.resize(V);
        init();
    }

    /*
     * Functie de initializare a multimilor initiale pentru fiecare nod
     * ---fiecare nod formeaza la inceput propria lui multime---
     */
    void init(){
        for(int i=0;i<V;i++)
            set[i]=i;
    }

    /*
     * Functia de gasire a multimii din care face parte un varf dat
     * param varf: varful pentru care se cauta multimea de care apartine
     * return: returnez numarul multimii de care apartine varful dat
     */
    int find_set(int varf){
        return set[varf];
    }

    /*
     * Functie de adaugare a unei muchii in graf
     * param x: varful incident spre exterior al muchiei
     * param y: varful incident spre interior al muchiei
     * param w: ponderea muchiei (x,y)
     */
    void add(int x,int y,int w){
        muchie m{};
        m.x=x;
        m.y=y;
        m.w=w;
        muchii.push_back(m);
    }

    /*
     * Functia care uneste multimiile de care apartin doua varfuri date
     * param u: primul varf
     * param v: al doilea varf
     * ---parcurg toate varfurile din graf iar toate varfurile care se afla
     *    in aceeasi multime v, vor fi "transferate" in multimea de care
     *    apartine u, inclusiv nodul v si astfel se face o reuniune a celor 2 multimi;---
     */
    void uneste(int u,int v){
        for(int i=0;i<V;i++)
            if(i!=v and set[i]==set[v])
                set[i]=set[u];
        set[v]=set[u];
    }

    /*
     * Functia de sortare a muchiilor crescator dupa pondere
     */
    void sortare(){
        sort(muchii.begin(),muchii.end(),[](muchie a,muchie b){
            return a.w<b.w;
        });
    }

    vector<muchie> get_muchii(){
        return muchii;
    }
};

vector<muchie> Krukstal(Graf& graf){
    vector<muchie> rez;
    graf.sortare();
    for(const auto& m : graf.get_muchii())
        if(graf.find_set(m.x)!=graf.find_set(m.y)) {
            rez.push_back(m);
            graf.uneste(m.x, m.y);
        }
    return rez;
}

int main(){
    int n,m;
    fin>>n>>m;
    //creez un graf
    Graf graf(n,m);
    //adaug muchiile
    for(int i=0;i<m;i++){
        int x,y,w;
        fin>>x>>y>>w;
        graf.add(x,y,w);
        graf.add(y,x,w);
    }
    //aplic Krukstal si obtin arborele de acoperire
    auto rez= Krukstal(graf);
    //sortez muchiile arborelui crescator dupa pondere
    sort(rez.begin(),rez.end(),[](muchie a,muchie b){
        return a.w<b.w;
    });
    //afisez ce trebuie
    int sum=0;
    for(const auto& muchie : rez)
        sum+=muchie.w;
    fout<<sum<<"\n";
    fout<<rez.size()<<"\n";
    for(const auto& muchie : rez)
        fout<<muchie.x<<' '<<muchie.y<<"\n";
    return 0;
}