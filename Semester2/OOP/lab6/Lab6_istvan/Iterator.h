#ifndef LAB6_ISTVAN_ITERATOR_H
#define LAB6_ISTVAN_ITERATOR_H

#include "MyVector.h"

template<typename TElem>
class MyVectorIterator {
    friend class MyVector<TElem>;
private:
    MyVectorIterator() noexcept : curent{0}{}
    MyVectorIterator<TElem>& operator=(const MyVectorIterator<TElem>& it) = default;
    MyVectorIterator(MyVector<TElem>& v): vector{v}{}
    MyVectorIterator(MyVector<TElem>& v, const int& curent) noexcept: vector{v}, curent{curent} {}

    int curent;
    MyVector<TElem>& vector;
public:

    void prim();

    void urmator();

    TElem& operator*();

    MyVectorIterator<TElem>& operator++();

    bool operator==(const MyVectorIterator<TElem>& it);

    bool operator!=(const MyVectorIterator<TElem>& it);

};

template<typename TElem>
void MyVectorIterator<TElem>::prim(){
    curent=0;
}

template<typename TElem>
void MyVectorIterator<TElem>::urmator(){
    curent++;
}

template<typename TElem>
TElem& MyVectorIterator<TElem>::operator*() {
    return vector[curent];
}

template<typename TElem>
MyVectorIterator<TElem>& MyVectorIterator<TElem>::operator++(){
    curent++;
    return *this;
}


template<typename TElem>
bool MyVectorIterator<TElem>::operator==(const MyVectorIterator<TElem>& it) {
    if(curent==it->curent)
        return true;
    return false;
}

template<typename TElem>
bool MyVectorIterator<TElem>::operator!=(const MyVectorIterator<TElem>& it) {
    if(curent==it.curent)
        return false;
    return true;
}


#endif //LAB6_ISTVAN_ITERATOR_H
