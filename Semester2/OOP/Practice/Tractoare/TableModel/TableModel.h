//
// Created by Titieni Paul on 26.06.2024.
//

#ifndef TRACTOARE_TABLEMODEL_H
#define TRACTOARE_TABLEMODEL_H


#include <QAbstractTableModel>
#include <utility>
#include <vector>
#include <map>
#include "../Domain/Tractor.h"
#include <QBrush>

using namespace std;

class TableModel : public QAbstractTableModel{
private:
//Q_OBJECT
    vector<Tractor> tractoare;
    string tip;
public:
    TableModel(vector<Tractor> tr) : tractoare(std::move(tr)){}

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

    QVariant headerData(int section, Qt::Orientation orientation,int role) const override;

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;

    int columnCount(const QModelIndex& parent = QModelIndex()) const override;

    void setTractoare(vector<Tractor> trs);

    void setTip(string tip);

};


#endif //TRACTOARE_TABLEMODEL_H
