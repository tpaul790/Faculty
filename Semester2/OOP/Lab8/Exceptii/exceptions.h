#ifndef LAB6_EXCEPTIONS_H
#define LAB6_EXCEPTIONS_H
#include <string>
#include <utility>
using std::string;

class Exception{
private:
    string msg;
public:
    /*
     * Constructorul unei exceptii
     * param:
     *  -1: msg, reprezinta mesajul de exceptie
     *  return:-
     */
    explicit Exception(string msg): msg{std::move(msg )}{}

    /*
     * Functia reprezinta un get er de mesaj pentru exceptia curenta
     * param:-
     * return: mesajul exceptiei curente
     */
    string get_mesaj(){
        return msg;
    }
};

#endif //LAB6_EXCEPTIONS_H
