//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_EXCEPTION_H
#define PRODUSE_EXCEPTION_H

#include <string>
#include <utility>
using std::string;
class Exception {
private:
    string ers;
public:
    explicit Exception(string e) : ers{std::move(e)} {};

    string get_ers() const{
        return ers;
    }
};


#endif //PRODUSE_EXCEPTION_H
