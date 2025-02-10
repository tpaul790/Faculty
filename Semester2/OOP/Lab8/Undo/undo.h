#ifndef LAB6_UNDO_H
#define LAB6_UNDO_H

#include "../Domain/produs.h"
#include "../Repo/repo.h"


class ActiuneUndo {
public:
    virtual void do_undo()=0;
    virtual ~ActiuneUndo()=default;
};

class AdaugaUndo : public ActiuneUndo{
private:
    Produs produs_adaugat;
    RepoAbstract& repo;
public:
    AdaugaUndo(Produs& p, RepoAbstract& repo) : produs_adaugat{p}, repo{ repo }{}
    void do_undo() override{
        repo.sterge(produs_adaugat);
    }
};

class StergeUndo : public ActiuneUndo{
private:
    Produs produs_sters;
    RepoAbstract& repo;
public:
    StergeUndo(Produs& p, RepoAbstract& repo) : produs_sters{ p }, repo{ repo }{}

    void do_undo() override{
        repo.store(produs_sters);
    }
};

class ModificaUndo : public ActiuneUndo{
private:
    Produs produs_vechi;
    Produs produs_nou;
    RepoAbstract& repo;
public:
    ModificaUndo(Produs& vechi, Produs& nou, RepoAbstract& repo) : produs_vechi{ vechi }, produs_nou{ nou }, repo{ repo }{}

    void do_undo() override{
        repo.modifica(produs_nou,produs_vechi);
    }
};


#endif //LAB6_UNDO_H
