#include <iostream>
#include <fstream>
#include <set>
#include <vector>

using namespace std;

ifstream fin("input.txt");
ofstream fout("output.txt");

vector<int> CodificarePrufer(vector<int> arbore){
    auto n=arbore.size();
    vector<int> prufer;
    //calculez gradul fiecarui nod
    vector<int> grad(n,1);
    for(const auto& p : arbore)
        grad[p]++;
    //pun frunzele intr un set pentru a nu se adauga de mai multe ori aceasi
    //si pentru a-mi fi ordonate crescator
    set<int> frunze;
    for(int i=0;i<n;i++)
        if(grad[i]==1)
            frunze.insert(i);
    //fac cei n-1 pasi necesari obtinerii codificarii prufer
    for(int i=0;i<n-1;i++){
        auto u=*frunze.begin();//cea mai mica frunza
        frunze.erase(u);//sterg frunza
        auto p=arbore[u];//parintele celei mai mici frunze
        prufer.push_back(p);
        grad[p]--;
        if(grad[p]==1)
            frunze.insert(p);
    }
    return prufer;
}

void afisare(const vector<int>& v){
    for(const auto& el : v)
        cout<<el<<' ';
    cout<<endl;
}

vector<int> DecodificarePrufer(vector<int> prufer){
    auto n=prufer.size()+1;
    vector<int> arbore(n,-1); //vectorul de tati al arborelui
    vector<int> grad(n,1);//vectorul care tine gradele fiecarui nod
    set<int> frunze;    //multimea in care vor fi toate valorile care nu se afla in prufer
    auto radacina=prufer.back();
    //calculez gradele
    for(const auto& el : prufer)
        grad[el]++;
    grad[radacina]--;//la radacina se incrementeaza o data fara sa trebuiasca
    //pun frunzele
    for(int i=0;i<n;i++)
        if(grad[i]==1)
            frunze.insert(i);
    //aici voi folosi vectorul grad pe post de frecventa a valorii in codificare
    grad[radacina]++;
    //cei n-1 pasi necesari decodificarii
    for(int i=0;i<n-1;i++){
        //afisare(grad);
        auto frunza=*frunze.begin();//iau cel mai mic nod care nu se afla in codificare
        auto parinte=*prufer.begin();//iau primul nod din codificare
        prufer.erase(prufer.begin());//sterg primul nod din codificare
        prufer.push_back(frunza);//adaug frunza la finalul codificarii
        frunze.erase(frunza);//sterg frunza din set pentru ca acm sigur se afla in codificare
        arbore[frunza]=parinte;//creez vectorul de tati al arborelui
        if(grad[parinte]==2) {          //daca nodul care a iesit din codificare are frecventa 2 inseamna
            frunze.insert(parinte);  //ca el nu mai apare in codificare deci trebuie adaugat la frunze
        }
        grad[parinte]--;    //scad frecventa
    }
    return arbore;
}

int main(int argc,char* argv[]) {
    int n;
    fin>>n;
    vector<int> arbore;
    for(int i=0;i<n;i++){
        int x;
        fin>>x;
        arbore.push_back(x);
    }
    auto prufer= CodificarePrufer(arbore);
    fout<<"Aborele codificat\n";
    fout<<prufer.size()<<endl;
    for(const auto& el : prufer)
        fout<<el<<' ';

    auto arbore_dec= DecodificarePrufer(prufer);
    fout<<"\n\n"<<"Arborele decodificat\n";
    fout<<arbore.size()<<endl;
    for(const auto& el : arbore)
        fout<<el<<' ';
    return 0;
}
