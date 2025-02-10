
#ifndef PRACTIC_TABLEMODEL_H
#define PRACTIC_TABLEMODEL_H


#include <QAbstractTableModel>
#include <utility>
#include <vector>
#include <map>
#include "../Domain/Parcare.h"
#include <QBrush>

using namespace std;

class TableModel : public QAbstractTableModel{
private:
    vector<Parcare> parcari;
public:
    TableModel(vector<Parcare> pr) : parcari(std::move(pr)){}
    /*
     * Constructorul modelului care primeste un vector de parcari
     */

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;
    /*
     * Functia care cere datele care se vad
     */

    QVariant headerData(int section, Qt::Orientation orientation,int role) const override;
    /*
     * Functia care creeaza un header
     */

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;
    /*
     * functia care returneaza numarul de linii al tabelului
     */

    int columnCount(const QModelIndex& parent = QModelIndex()) const override;
    /*
     * functia care returneaza nuarul de coloane ale tabelului
     */
    void setParcari(vector<Parcare> prs);

};


#endif //PRACTIC_TABLEMODEL_H
