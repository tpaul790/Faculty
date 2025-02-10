//
// Created by Titieni Paul on 23.06.2024.
//

#ifndef MELODII1_MELODIE_H
#define MELODII1_MELODIE_H

#include <string>
#include <utility>
using std::string;

class Melodie {
private:
    int id;
    string titlu;
    string artist;
    int rank;
public:
    Melodie(int id, string t, string a, int r) : id{id}, titlu{std::move(t)}, artist{std::move(a)}, rank{r} {};

    int get_id() const{
        /*
         * Se returneaza id ul melodiei
         */
        return id;
    }

    const string& get_titlu() const{
        /*
         * Se returneaza titlul melodiei
         */
        return titlu;
    }

    const string& get_artist() const{
        /*
         * Se returneaza artistul melodiei
         */
        return artist;
    }

    int get_rank() const{
        /*
         * Se returneaza rank ul melodiei
         */
        return rank;
    }

    void set_titlu(string nou){
        /*
         * Functia seteaza titlul melodiei ca fiind nou
         */
        titlu=nou;
    }

    void set_rank(int nou){
        /*
         * Functia modifica rank ul melodiei in nou
         */
        rank=nou;
    }
};


#endif //MELODII1_MELODIE_H
