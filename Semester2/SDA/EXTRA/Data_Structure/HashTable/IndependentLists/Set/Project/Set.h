#pragma once
class Iterator;
class Nod;

typedef int TElem;
typedef Nod* PNod;

class Nod{
    friend class Set;
    friend class Iterator;
private:
    TElem  e;
    PNod urm;
public:
    Nod(TElem e){
        this->e = e;
    }
};

class Set {
	friend class Iterator;
    friend class Nod;
private:
    PNod* l;
    int m;
    int size;
    int d(int k) const;
    int hash(int k) const;
public:
    Set();

    bool adauga(TElem e);

    bool sterge(TElem e);

    bool cauta(TElem elem) const;


    int dim() const;

    bool vida() const;

    Iterator iterator() const;

    ~Set();

};

