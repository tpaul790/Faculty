#include "cos.h"

void CosProduse::populare(int dim,vector<Produs> v){
    std::uniform_int_distribution<> dist(0,v.size()-1);
    while(dim) {
        auto seed = std::chrono::system_clock::now().time_since_epoch().count();
        std::shuffle(v.begin(), v.end(), std::default_random_engine(seed));
        std::mt19937 mt{std::random_device{}()};
        int rndNr = dist(mt);
        this->store(v[rndNr]);
        dim--;
    }
    notify();
}

void CosProduse::save_to_file(string file_name){
    file_name+=".csv";
    int cnt=0;
    ofstream out(file_name, ios::trunc);
//    if (!out.is_open()) {
//        throw Exception("Nu se poate deschide fisierul: " + file_name);
//    }
    for (const auto& product : cos) {
        cnt++;
        out<< cnt <<", "<< product.get_nume() << ", " << product.get_pret() << ", " << product.get_tip() << ", " << product.get_producator() << '\n';
    }
    out.close();
}
