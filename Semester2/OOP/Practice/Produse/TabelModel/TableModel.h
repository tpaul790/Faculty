//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_TABLEMODEL_H
#define PRODUSE_TABLEMODEL_H

#include <QAbstractTableModel>
#include <utility>
#include <vector>
#include <map>
#include "../Domain/Produs.h"
#include <QBrush>

using namespace std;

class TableModel : public QAbstractTableModel{
private:
//Q_OBJECT
    vector<Produs> produse;
    int slider;
public:
    TableModel(vector<Produs> ps) : produse(std::move(ps)){}

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

    QVariant headerData(int section, Qt::Orientation orientation,int role) const override;

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;

    int columnCount(const QModelIndex& parent = QModelIndex()) const override;

    void setProduse(vector<Produs> produs);

    void setSlider(int s);

};


#endif //PRODUSE_TABLEMODEL_H
