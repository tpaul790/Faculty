#ifndef LAB6_PRODUS_H
#define LAB6_PRODUS_H

#include <string>
#include <utility>
#include <iostream>

using std::string;

class Produs{
private:
    string nume;
    string tip;
    int pret{};
    string producator;
    static int cnt_copieri;
public:
    /*
     * Constructorul clasei Produs
     * prama:
     *  -1: nume, reprezinta numele produsului
     *  -2: tip, reprezinta tipul produsului
     *  -3: pret,reprezinta pretul produsului
     *  -4: producator,reprezinta producatorul produsului
     */
    Produs(string nume, string tip, int pret, string producator) :nume{ std::move(nume) }, tip{std::move( tip )}, pret{ pret }, producator{std::move( producator )}{}

    Produs()=default;

    Produs(const Produs& ot){
        nume=ot.nume;
        tip=ot.tip;
        pret=ot.pret;
        producator=ot.producator;
        cnt_copieri++;
    }

    /*
     * Functia este un geter de nume pentru un produs dat
     * param:-
     * return: returneaza numele produsului curent
     */
    string get_nume() const;

    /*
     * Functia este un geter de tip pentru un produs dat
     * param:-
     * return: returneaza tipul produsului curent
     */
    string get_tip() const;

    /*
     * Functia este un geter de pret pentru un produs dat
     * param:-
     * return: returneaza pretul produsului curent
     */
    int get_pret() const;

    /*
     * Functia este un geter de producator pentru un produs dat
     * param:-
     * return: returneaza producatorul produsului curent
     */
    string get_producator() const;

    /*
     * Functia este un seter de nume pentru un produs dat
     * param:nou, reprezinta numele nou al produsului curent
     * return:-
     */
    void set_nume(string nou);

    /*
     * Functia este un seter de tip pentru un produs dat
     * param:nou, reprezinta tip nou al produsului curent
     * return:-
     */
    void set_tip(string nou);

    /*
     * Functia este un seter de pret pentru un produs dat
     * param:nou, reprezinta pretul nou al produsului curent
     * return:-
     */
    void set_pret(int nou);

    /*
     * Functia este un seter de producator pentru un produs dat
     * param:nou, reprezinta producatorul nou al produsului curent
     * return:-
     */
    void set_producator(string nou);

    static int get_cnt(){
        return cnt_copieri;
    }

    bool operator==(const Produs& ot) const;

    bool operator!=(const Produs& ot);
};

#endif //LAB6_PRODUS_H
