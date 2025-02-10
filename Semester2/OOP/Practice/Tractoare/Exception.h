//
// Created by Titieni Paul on 26.06.2024.
//

#ifndef TRACTOARE_EXCEPTION_H
#define TRACTOARE_EXCEPTION_H
#include <string>
using std::string;
class Exception {
private:
    string ers;
public:
    Exception(string e) : ers{std::move(e)} {};

    string get_ers() const{
        return ers;
    }
};


#endif //TRACTOARE_EXCEPTION_H
