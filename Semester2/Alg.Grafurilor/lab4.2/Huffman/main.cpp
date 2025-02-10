#include <iostream>
#include <fstream>
#include <map>
#include <utility>
#include <vector>
#include <queue>

using namespace std;

ifstream fin("input.txt");
ifstream fin2("input2.txt");
ofstream fout("output.txt");
ofstream fout2("output2.txt");

class Nod;

typedef Nod* PNod;

class Nod{
public:
    int frecventa;
    char info;
    PNod stanga,dreapta;
    Nod(int frecv,char inf) : frecventa(frecv),info(inf),stanga(nullptr),dreapta(nullptr){};
};

void sterge_arbore(PNod radacina){
    if(radacina != nullptr){
        sterge_arbore(radacina->stanga);
        sterge_arbore(radacina->dreapta);
        delete radacina;
    }
}

void coduri(PNod radacina,string sir,map<char,string>& huffman){
    if(radacina == nullptr)
        return;
    if(radacina->stanga == nullptr and radacina->dreapta == nullptr)
        huffman[radacina->info]=std::move(sir);
    coduri(radacina->stanga,sir+"0",huffman);
    coduri(radacina->dreapta,sir+"1",huffman);
}

PNod CodificareHuffman(const string& text){
    map<char,int> frecv;
    for(char ch : text){
        frecv[ch]++;
    }
    priority_queue<pair<pair<int,char>,PNod>,vector<pair<pair<int,char>,PNod>>,greater<pair<pair<int,char>,PNod>>> pq;
    for(const auto& per : frecv){
        auto nou = new Nod{per.second,per.first};
        pq.push(std::make_pair(std::make_pair(nou->frecventa,nou->info),nou));
    }
    while(pq.size()!=1){
        auto stanga=pq.top();
        //cout<<stanga->info<<' '<<stanga->frecventa<<endl;
        pq.pop();
        auto dreapta=pq.top();
        //cout<<dreapta->info<<' '<<dreapta->frecventa<<endl;
        pq.pop();
        PNod nou=new Nod{stanga.first.first+dreapta.first.first,'\0'};
        nou->stanga=stanga.second;
        nou->dreapta=dreapta.second;
        //cout<<nou->info<<' '<<nou->frecventa<<endl<<endl;
        pq.push(make_pair(make_pair(nou->frecventa,nou->info),nou));
    }
    auto radacina=pq.top().second;

    map<char,string> huffman;
    coduri(radacina,"",huffman);
    for(const auto& per : frecv){
        fout<<per.first<<' '<<per.second<<endl;
    }
    for(const auto& ch : text){
        fout<<huffman[ch];
    }

    return radacina;
}

void DecodificareHuffman(PNod radacina,const string& sir){
    string decodif;
    PNod curent=radacina;
    for(const auto& bit : sir){
        if(bit=='0')
            curent=curent->stanga;
        else
            curent=curent->dreapta;
        if(curent->stanga == nullptr and curent->dreapta == nullptr) {
            fout2 << curent->info;
            curent = radacina;
        }
    }

}

int main() {
    string msg;
    fin>>msg;
    auto radacina = CodificareHuffman(msg);

    string cod;
    fin2>>cod;
    DecodificareHuffman(radacina,cod);

    sterge_arbore(radacina);
    return 0;
}
