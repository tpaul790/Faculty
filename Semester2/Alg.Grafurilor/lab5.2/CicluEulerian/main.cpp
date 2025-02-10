#include <iostream>
#include <fstream>
#include <vector>
#include <stack>
using namespace std;

vector<pair<int, int>> graf[100]; //salvam si nr muchiei sa putem marca vizitatele
int viz[500];
stack<int> stiva;
int v, m, prim;

//plecam de la ideea ca se da un graf ce are un ciclu eulerian
void Euler()
{
    ofstream g("output.txt");
    int nod, muchie, y;
    stiva.push(prim); //un nod care sigur are legaturi, il punem in stiva
    while (!stiva.empty())
    {
        nod = stiva.top();
        //se opreste in momentul in care nu mai are nodul de inceput legaturi
        if (graf[nod].size() == 0)
        {
            stiva.pop();
            g << nod << " ";
        }
        else
        {
            auto vec1 = graf[nod].front();
            y = graf[nod].front().first; //un nod cu care x are muchie
            muchie = graf[nod].front().second; //numarul muchiei
            auto it = std::find(graf[nod].begin(),graf[nod].end(),vec1);
            graf[nod].erase(it);//sterg aceasta muchie din vecinii lui x
            if (viz[muchie] == 0)
            {
                viz[muchie] = 1; //marcam muchia ca fiind vizitata (sa nu mai trecem prin ea)
                stiva.push(y); //punem nodul y in stiva (sa continuam parcurgerea)
            }
        }
    }
    g.close();
}


int main()
{
    ifstream f("input.txt");
    int x, y, ok = 0;
    f >> v >> m;
    for (int i = 0; i < m; i++)
    {
        f >> x >> y;
        if (ok == 0) { prim = x; ok = 1; } //primul nod ce are o legatura cu restul
        graf[x].emplace_back( y,i ); //salvez si extremitatea si numarul muchiei (ma ajuta sa nu trec de 2 ori printr-o muchie)
        graf[y].emplace_back( x,i );
    }
    Euler();
    f.close();
    return 0;
}